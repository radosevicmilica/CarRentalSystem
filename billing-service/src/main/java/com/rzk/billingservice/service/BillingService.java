package com.rzk.billingservice.service;

import com.rzk.billingservice.dto.PayRequest;
import com.rzk.billingservice.dto.PenaltyDto;
import com.rzk.billingservice.dto.RentalDto;
import com.rzk.billingservice.dto.RentalSummaryDto;
import com.rzk.billingservice.exceptions.BillAlreadyPaidException;
import com.rzk.billingservice.exceptions.EntityAlreadyExistsException;
import com.rzk.billingservice.exceptions.EntityDoesNotExistException;
import com.rzk.billingservice.model.Bill;
import com.rzk.billingservice.model.BillItem;
import com.rzk.billingservice.model.Payment;
import com.rzk.billingservice.proxy.RentalClient;
import com.rzk.billingservice.repository.BillRepository;
import com.rzk.billingservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingService {

    private final BillRepository br;
    private final PaymentRepository pr;

    private final RentalClient rentalClient;

    public Bill createBill(Long rentalId){

        if(br.existsByRentalId(rentalId)){
            throw new EntityAlreadyExistsException("Bill for rental " + rentalId + " already exists");
        }

        RentalSummaryDto summary = rentalClient.getRentalSummary(rentalId);
        RentalDto rental = summary.getRental();

        Bill bill = new Bill();
        bill.setRentalId(rentalId);
        bill.setCreatedAt(LocalDateTime.now());

        List<BillItem> items = new ArrayList<>();

        // base price
        BillItem base = new BillItem();
        base.setDescription("Base rental price");
        base.setAmount(rental.getBasePrice());
        base.setBill(bill);
        items.add(base);

        // penalties
        if(rental.getPenalties() != null){
            for(PenaltyDto p : rental.getPenalties()){

                BillItem penaltyItem = new BillItem();
                penaltyItem.setDescription("Penalty: " + p.getType());
                penaltyItem.setAmount(p.getAmount());
                penaltyItem.setBill(bill);

                items.add(penaltyItem);
            }
        }

        // total
        double total = 0;
        for(BillItem item : items){
            total += item.getAmount();
        }

        bill.setTotalAmount(total);
        bill.setItems(items);

        return br.save(bill);
    }

    public Bill getBillById(Long id) {
        return br.findById(id).orElseThrow(() -> new EntityDoesNotExistException("Bill with id " + id + " not found"));
    }

    public Bill getBillByRentalId(Long rentalId){
        return br.findByRentalId(rentalId)
                .orElse(null);
    }

    public Payment pay(Long billId, PayRequest request){
        Bill bill = br.findById(billId).orElseThrow(()-> new EntityDoesNotExistException("Bill with id: " +billId+ " not found"));

        boolean paid = pr.existsByBillIdAndStatus(billId, "PAID");
        if(paid) {
            throw new BillAlreadyPaidException("Bill already paid");
        }

        Payment payment = new Payment();
        payment.setBill(bill);
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus("PAID");

        return pr.save(payment);
    }

    public Payment getPaymentForBill(Long billId){
        return pr.findByBillId(billId).orElseThrow(() -> new EntityDoesNotExistException("Payment for bill " + billId + " not found"));
    }

    public void deleteBill(Long id){
        Bill bill = br.findById(id)
                .orElseThrow(() -> new EntityDoesNotExistException("Bill with id " + id + " not found"));

        br.delete(bill);
    }
}
