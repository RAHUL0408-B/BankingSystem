
package com.Banking.Account_Service.controller;

import com.Banking.Account_Service.Service.AccountService;
import com.Banking.Account_Service.dto.AcconutResponse;
import com.Banking.Account_Service.dto.CreateAccountRequest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@Slf4j
@RequiredArgsConstructor
public class accountcontroller {

    private final AccountService accountService;

    // Create Account
    @PostMapping
    public ResponseEntity<AcconutResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request
    ) {
        log.info("Received request to create account");

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accountService.createAccount(request));
    }

    // Get Account Details
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AcconutResponse> getAccount(
            @PathVariable String accountNumber
    ) {
        log.info("Fetching account: {}", accountNumber);

        return ResponseEntity.ok(
                accountService.getAccount(accountNumber)
        );
    }

    // Block Account
    @PutMapping("/{accountNumber}/block")
    public ResponseEntity<String> blockAccount(
            @PathVariable String accountNumber
    ) {
        accountService.blockAccount(accountNumber);

        return ResponseEntity.ok("Account blocked successfully");
    }

    // Deduct Balance
    @PutMapping("/{accountNumber}/deduct")
    public ResponseEntity<String> deductAccount(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount
    ) {
        accountService.deductBalance(accountNumber, amount);

        return ResponseEntity.ok("Amount deducted successfully");
    }

    // Credit Balance
    @PutMapping("/{accountNumber}/credit")
    public ResponseEntity<String> creditAccount(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount
    ) {
        accountService.creditBalance(accountNumber, amount);

        return ResponseEntity.ok("Amount credited successfully");
    }

    // Get Account Balance
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getAccountBalance(
            @PathVariable String accountNumber
    ) {
        return ResponseEntity.ok(
                accountService.getAccountBalance(accountNumber)
        );
    }
}
