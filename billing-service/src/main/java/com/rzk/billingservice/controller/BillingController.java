package com.rzk.billingservice.controller;

import com.rzk.billingservice.dto.PayRequest;
import com.rzk.billingservice.model.Bill;
import com.rzk.billingservice.model.BillItem;
import com.rzk.billingservice.model.Payment;
import com.rzk.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bills")
public class BillingController {

    private final BillingService service;

    @PostMapping
    public ResponseEntity<Bill> createBill(@RequestParam Long rentalId){
        return new ResponseEntity<>(service.createBill(rentalId), HttpStatus.CREATED);
    }

    //bas id billa
    @GetMapping("/{id}")
    public ResponseEntity<Bill> getBill(@PathVariable Long id){
        return new ResponseEntity<>(service.getBillById(id), HttpStatus.OK);
    }

    @GetMapping("/byRental/{rentalId}")
    public ResponseEntity<Bill> getBillByRentalId(@PathVariable Long rentalId){
        return new ResponseEntity<>(service.getBillByRentalId(rentalId), HttpStatus.OK);
    }

//    {
//      "paymentMethod": "CARD"
//    } http://localhost:8765/bills/5/pay
    @PostMapping("/{billId}/pay")
    public ResponseEntity<Payment> pay(@PathVariable Long billId, @RequestBody PayRequest request){
        return new ResponseEntity<>(service.pay(billId, request), HttpStatus.OK);
    }

    @GetMapping("/{billId}/payment")
    public ResponseEntity<Payment> getPayment(@PathVariable Long billId){
        return new ResponseEntity<>(service.getPaymentForBill(billId), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public void deleteBill(@PathVariable Long id){
        service.deleteBill(id);
    }
}
