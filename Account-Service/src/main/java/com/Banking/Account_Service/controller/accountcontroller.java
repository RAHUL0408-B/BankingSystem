package com.Banking.Account_Service.controller;

import com.Banking.Account_Service.Entity.Account;
import com.Banking.Account_Service.dto.AcconutResponse;
import com.Banking.Account_Service.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping
@Slf4j
@RequiredArgsConstructor

public class accountcontroller{
    private final AccountService accountService;
    public ResponseEntity<AcconutResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @GetMapping("/{accountnumber}")
    public ResponseEntity<AcconutResponse> getAccount(
            @PathVariable String accuntNumber
    ){
        return ResponseEntity.ok(accountService.getAccount(accountNumber));
    }
    @PutMapping("/{accountNumber}/block")
    public ResponseEntity<String> blockedAccount(
            @PathVariable String accountNumber
    ){
        accountService.blockAccount(accountNumber);
        return ResponseEntity.ok("Account blocked");

    }
    @PutMapping("/{accountNumber}/deduct")
    public ResponseEntity<String> deductAccount(
            @PathVariable String accountNumber
            @RequestParam BigDecimal amount
    ){
        accountService.deductBalance(accountNumber,amount);

        return ResponseEntity.ok("Account deducted");
    }

    @PutMapping("/{accountNumber}/credit")
    public ResponseEntity<String> creditAccount(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount
    ){
        accountService.creditBalance(accountNumber,amount);
        return ResponseEntity.ok("Account credited Successfully");
    }

}