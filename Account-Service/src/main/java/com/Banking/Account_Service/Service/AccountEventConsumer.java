```java
        package com.Banking.Account_Service.Service;

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
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
@Slf4j
public class AccountEventConsumer {

    private final accuntService accuntService;

    // Create Account
    @PostMapping
    public ResponseEntity<AcconutResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(accuntService.createAccount(request));
    }

    // Get Account
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AcconutResponse> getAccount(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accuntService.getAccount(accountNumber)
        );
    }

    // Get Account Balance
    @GetMapping("/{accountNumber}/balance")
    public ResponseEntity<BigDecimal> getBalance(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accuntService.getBalance(accountNumber)
        );
    }

    // Block Account
    @PutMapping("/{accountNumber}/block")
    public ResponseEntity<String> blockAccount(
            @PathVariable String accountNumber) {

        accuntService.blockAccount(accountNumber);

        return ResponseEntity.ok("Account blocked Successfully");
    }

    // Deduct Balance
    @PutMapping("/{accountNumber}/deduct")
    public ResponseEntity<String> deductBalance(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {

        accuntService.deductBalance(accountNumber, amount);

        return ResponseEntity.ok("Balance deducted Successfully");
    }

    // Credit Balance
    @PutMapping("/{accountNumber}/credit")
    public ResponseEntity<String> creditBalance(
            @PathVariable String accountNumber,
            @RequestParam BigDecimal amount) {

        accuntService.creditBalance(accountNumber, amount);

        return ResponseEntity.ok("Balance Credited Successfully");
    }
}
```