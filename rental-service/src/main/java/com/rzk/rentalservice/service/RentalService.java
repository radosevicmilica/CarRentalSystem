package com.rzk.rentalservice.service;

import com.rzk.rentalservice.dto.RentalSummary;
import com.rzk.rentalservice.dto.UserDto;
import com.rzk.rentalservice.dto.VehicleDto;
import com.rzk.rentalservice.exception.EntityAlreadyExistsException;
import com.rzk.rentalservice.exception.EntityDoesNotExistException;
import com.rzk.rentalservice.exception.InvalidRentalDatesException;
import com.rzk.rentalservice.model.Penalty;
import com.rzk.rentalservice.model.Rental;
import com.rzk.rentalservice.model.RentalPeriod;
import com.rzk.rentalservice.proxy.UserClient;
import com.rzk.rentalservice.proxy.VehicleClient;
import com.rzk.rentalservice.repository.PenaltyRepository;
import com.rzk.rentalservice.repository.RentalPeriodRepository;
import com.rzk.rentalservice.repository.RentalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RentalService {

    private final RentalRepository rr;
    private final RentalPeriodRepository rpr;
    private final PenaltyRepository pr;

    private final UserClient userClient;
    private final VehicleClient vehicleClient;

    public List<Rental> getAllRentals(){
        return rr.findAll();
    }

    public Rental createRental(Rental rental, Long idUser, Long idVehicle){

        if(rental.getEndDate().isBefore(rental.getStartDate())){
            throw new InvalidRentalDatesException("End date cannot be before start date");
        }

        UserDto user = userClient.getUserById(idUser);
        rental.setUserId(idUser);

        VehicleDto vehicleDto = vehicleClient.getVehicleById(idVehicle);
        if(!vehicleDto.getCurrentStatus().equals("AVAILABLE")){
            throw new IllegalStateException("Vehicle is not available for rental");
        }

        rental.setVehicleId(idVehicle);
        Rental savedRental = rr.save(rental);

        vehicleClient.updateStatus(idVehicle, "RENTED");

        return savedRental;
    }

    public Rental getRentalById(Long id){
        return rr.findById(id).orElseThrow(()-> new EntityDoesNotExistException("Rental with id " + id + " not found"));
    }

    public List<Rental> getRentalsByUserId(Long userId){
        return rr.findByUserId(userId);
    }

    public List<RentalPeriod> addRentalPeriod(Long rentalId) {

        Rental rental = getRentalById(rentalId);

        LocalDate start = rental.getStartDate();
        LocalDate end = rental.getEndDate();

        List<RentalPeriod> periods = new ArrayList<>();

        int dayNumber = 1;
        LocalDate currentDate = start;

        while (!currentDate.isAfter(end)) {

            RentalPeriod period = new RentalPeriod();
            period.setRental(rental);
            period.setDayNumber(dayNumber);
            period.setRentalDate(currentDate);

            periods.add(rpr.save(period));

            dayNumber++;
            currentDate = currentDate.plusDays(1);
        }

        return periods;
    }

    public List<RentalPeriod> getRentalPeriods(Long rentalId){
        return rpr.findByRentalId(rentalId);
    }

    public Penalty addPenalty(Long rentalId, Penalty penalty){
        Rental rental = getRentalById(rentalId);
        boolean exists = pr.existsByRentalIdAndType(rentalId, penalty.getType());
        if (exists) {
            throw new EntityAlreadyExistsException("Penalty of type " + penalty.getType() + " already exists for this rental");
        }
        penalty.setRental(rental);
        return pr.save(penalty);
    }

    public List<Penalty> getPenalties(Long rentalId){
        return pr.findByRentalId(rentalId);
    }

    public RentalSummary getRentalSummary(Long rentalId){
        Rental rental = getRentalById(rentalId);
        List<Penalty> penalties = getPenalties(rentalId);

        double totalPenalty = 0.0;
        for (Penalty p : penalties) {
            totalPenalty += p.getAmount();
        }

        double totalPrice = rental.getBasePrice() + totalPenalty;
        return new RentalSummary(rental, totalPrice);
    }

    public List<RentalPeriod> extendRental(Long rentalId, int extraDays) {
        Rental rental = getRentalById(rentalId);
        List<RentalPeriod> addedPeriods = new ArrayList<>();

        List<RentalPeriod> existingPeriods = getRentalPeriods(rentalId);
        int lastDayNumber = 0;
        for (RentalPeriod p : existingPeriods) {
            if (p.getDayNumber() > lastDayNumber) {
                lastDayNumber = p.getDayNumber();
            }
        }

        for (int i = 1; i <= extraDays; i++) {
            int dayNumber = lastDayNumber + i;
            LocalDate newDate = rental.getEndDate().plusDays(i);

            boolean exists = rpr.existsByRentalIdAndDayNumber(rentalId, dayNumber);
            if (exists) {
                throw new EntityAlreadyExistsException("Rental period for day " + dayNumber + " already exists");
            }

            RentalPeriod period = new RentalPeriod();
            period.setRental(rental);
            period.setDayNumber(dayNumber);
            period.setRentalDate(newDate);
            addedPeriods.add(rpr.save(period));
        }

        rental.setEndDate(rental.getEndDate().plusDays(extraDays));
        rr.save(rental);
        return addedPeriods;
    }
}
