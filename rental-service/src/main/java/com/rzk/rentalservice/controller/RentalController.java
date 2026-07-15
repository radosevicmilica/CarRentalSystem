package com.rzk.rentalservice.controller;

import com.rzk.rentalservice.dto.RentalSummary;
import com.rzk.rentalservice.model.Penalty;
import com.rzk.rentalservice.model.Rental;
import com.rzk.rentalservice.model.RentalPeriod;
import com.rzk.rentalservice.service.RentalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService service;

    @GetMapping
    public ResponseEntity<List<Rental>> getAllRentals(){
        return new ResponseEntity<>(service.getAllRentals(), HttpStatus.OK);
    }

//    {
//    "startDate": "2026-03-10",
//    "endDate": "2026-03-15",
//    "status": "ACTIVE",
//    "basePrice": 250.0
//}
//    http://localhost:8765/rentals?idUser=2&idVehicle=2
    @PostMapping
//    @RateLimiter(name = "rentalsServiceRateLimiter", fallbackMethod = "rentalServiceFallback")
    public ResponseEntity<Rental> createRental(@Valid @RequestBody Rental rental,
                               @RequestParam Long idUser, @RequestParam Long idVehicle){
        return new ResponseEntity<>(service.createRental(rental, idUser, idVehicle), HttpStatus.CREATED) ;
    }

//    public ResponseEntity<Rental> rentalServiceFallback(RequestNotPermitted ex) {
//        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
//    }

    //http://localhost:8765/rentals/1
    @GetMapping("/{id}")
    public ResponseEntity<Rental> getRentalById(@PathVariable Long id){
        return new ResponseEntity<>(service.getRentalById(id), HttpStatus.OK) ;
    }

    //http://localhost:8765/rentals/byUser/2
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<List<Rental>> getRentalsByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(service.getRentalsByUserId(userId), HttpStatus.OK);
    }

//    http://localhost:8765/rentals/4/periods
    @PostMapping("/{rentalId}/periods") //generise periode na osnovu rentala
    public ResponseEntity<List<RentalPeriod>> generatePeriods(@PathVariable Long rentalId) {
        List<RentalPeriod> periods = service.addRentalPeriod(rentalId);
        return ResponseEntity.ok(periods);
    }

    @GetMapping("/{rentalId}/periods")
    public ResponseEntity<List<RentalPeriod>> getRentalPeriods(@PathVariable Long rentalId){
        return new ResponseEntity<>(service.getRentalPeriods(rentalId), HttpStatus.OK);
    }

//    {
//        "type": "LATE",
//            "amount": 25.0
//    }
//    http://localhost:8765/rentals/4/penalties
    @PostMapping("/{rentalId}/penalties")
    public ResponseEntity<Penalty> addPenalty(@PathVariable Long rentalId, @Valid @RequestBody Penalty penalty){
        return new ResponseEntity<>(service.addPenalty(rentalId, penalty), HttpStatus.OK);
    }

    @GetMapping("/{rentalId}/penalties")
    public ResponseEntity<List<Penalty>> getPenalties(@PathVariable Long rentalId){
        return new ResponseEntity<>(service.getPenalties(rentalId), HttpStatus.OK);
    }

    @GetMapping("/{rentalId}/summary")
    public ResponseEntity<RentalSummary> getRentalSummary(@PathVariable Long rentalId){
        return new ResponseEntity<>(service.getRentalSummary(rentalId), HttpStatus.OK);
    }

    @PatchMapping("/{rentalId}/extend/{extraDays}")
    public ResponseEntity<List<RentalPeriod>> extendsRental(@PathVariable Long rentalId, @PathVariable int extraDays){
        return new ResponseEntity<>(service.extendRental(rentalId, extraDays), HttpStatus.OK);
    }

    @PatchMapping("/{rentalId}/cancel")
    public ResponseEntity<Rental> cancelRental(@PathVariable Long rentalId){
        return new ResponseEntity<>(service.cancelRental(rentalId), HttpStatus.OK);
    }
}
