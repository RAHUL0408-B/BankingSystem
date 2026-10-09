/*
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
```*/


package com.Banking.Account_Service.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountEventConsumer {

    private final AccountService accountService;

    // Consume transaction completed event
    @KafkaListener(topics = "transaction.completed")
    public void consumeTransactionCompleted(
            @Payload Map<String, Object> payload
    ) {
        try {
            String receiverAccountNumber =
                    (String) payload.get("receiverAccountNumber");

            Object amountValue = payload.get("amount");

            if (receiverAccountNumber == null || amountValue == null) {
                throw new IllegalArgumentException(
                        "Missing receiverAccountNumber or amount in event"
                );
            }

            BigDecimal amount = new BigDecimal(amountValue.toString());

            log.info(
                    "Crediting account: {}, amount: {}",
                    receiverAccountNumber,
                    amount
            );

            accountService.creditBalance(receiverAccountNumber, amount);

            log.info(
                    "Transaction completed successfully for account: {}",
                    receiverAccountNumber
            );

        } catch (Exception e) {
            log.error(
                    "Error while processing transaction.completed event: {}",
                    e.getMessage(),
                    e
            );

            // Rethrow so the configured Kafka error handler can retry
            // or route the message to a dead-letter topic.
            throw e;
        }
    }

    // Consume fraud detected event
    @KafkaListener(topics = "fraud.detected")
    public void consumeFraudDetected(
            @Payload Map<String, Object> payload
    ) {
        try {
            String accountNumber =
                    (String) payload.get("accountNumber");

            if (accountNumber == null) {
                throw new IllegalArgumentException(
                        "Missing accountNumber in fraud event"
                );
            }

            log.warn(
                    "Fraud detected for account: {}",
                    accountNumber
            );

            // Add your fraud-handling logic here.
            // For example, block the account after verifying
            // the fraud event and applicable business rules.

        } catch (Exception e) {
            log.error(
                    "Error while processing fraud.detected event: {}",
                    e.getMessage(),
                    e
            );

            throw e;
        }
    }
}
