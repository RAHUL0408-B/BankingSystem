```java
        package com.Banking.Account_Service.Service;

import com.Banking.Account_Service.Entity.Account;
import com.Banking.Account_Service.Entity.AccountType;
import com.Banking.Account_Service.dto.AcconutResponse;
import com.Banking.Account_Service.dto.CreateAccountRequest;
import com.Banking.Account_Service.repository.Accountrepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@Slf4j
public class accuntService {

    private final Accountrepository accountrepository;

    public accuntService(Accountrepository accountrepository) {
        this.accountrepository = accountrepository;
    }

    // Create Account
    public AcconutResponse createAccount(CreateAccountRequest request) {

        log.info("Creating Account for: {}", request.getEmail());

        // Check if account already exists
        if (accountrepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Account already exists: " + request.getEmail()
            );
        }

        Account account = new Account();

        account.setAccountHolderName(request.getAccountHolderName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getInitialDeposit());

        // Generate account number
        account.setAccountNumber(generateAccountNumber());

        // Set daily transaction limit
        account.setDailyTransactionLimit(
                request.getAccountType() == AccountType.SAVINGS
                        ? new BigDecimal("100000")
                        : new BigDecimal("500000")
        );

        Account savedAccount = accountrepository.save(account);

        log.info(
                "Account created successfully: {}",
                savedAccount.getAccountNumber()
        );

        return mapToResponse(savedAccount);
    }

    // Generate Account Number
    private String generateAccountNumber() {

        return "ACC" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase();
    }

    // Convert Entity to Response DTO
    private AcconutResponse mapToResponse(Account account) {

        AcconutResponse response = new AcconutResponse();

        response.setAccountNumber(account.getAccountNumber());
        response.setAccountHolderName(account.getAccountHolderName());
        response.setEmail(account.getEmail());
        response.setPhone(account.getPhone());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setDailyTransactionLimit(
                account.getDailyTransactionLimit()
        );
        response.setCreatedAt(account.getCreatedAt());
        response.setStatus(account.getStatus());

        return response;
    }
}
```