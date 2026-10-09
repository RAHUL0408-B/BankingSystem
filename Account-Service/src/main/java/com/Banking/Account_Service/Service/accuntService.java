
package com.Banking.Account_Service.Service;

import com.Banking.Account_Service.Entity.Account;
import com.Banking.Account_Service.Entity.AccountStatus;
import com.Banking.Account_Service.Entity.AccountType;
import com.Banking.Account_Service.dto.AcconutResponse;
import com.Banking.Account_Service.dto.CreateAccountRequest;
import com.Banking.Account_Service.repository.Accountrepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@Slf4j
public class AccountService {

    private final Accountrepository accountrepository;
    private final SecureRandom secureRandom = new SecureRandom();

    public AccountService(Accountrepository accountrepository) {
        this.accountrepository = accountrepository;
    }

    // Create Account
    @Transactional
    public AcconutResponse createAccount(CreateAccountRequest request) {

        log.info("Creating account for: {}", request.getEmail());

        if (accountrepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException(
                    "Account already exists: " + request.getEmail()
            );
        }

        if (request.getAccountType() == null) {
            throw new IllegalArgumentException(
                    "Account type cannot be null"
            );
        }

        BigDecimal initialDeposit = request.getInitialDeposit();

        if (initialDeposit == null ||
                initialDeposit.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "Initial deposit cannot be negative or null"
            );
        }

        Account account = new Account();
        account.setAccountHolderName(request.getAccountHolderName());
        account.setEmail(request.getEmail());
        account.setPhone(request.getPhone());
        account.setAccountType(request.getAccountType());
        account.setBalance(initialDeposit);
        account.setAccountNumber(generateAccountNumber());

        account.setDailyTransactionLimit(
                request.getAccountType() == AccountType.SAVING
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

    // Get Account Details
    public AcconutResponse getAccount(String accountNumber) {

        Account account = accountrepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        return mapToResponse(account);
    }

    // Get Account Balance
    public BigDecimal getAccountBalance(String accountNumber) {

        Account account = accountrepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        return account.getBalance();
    }

    // Block Account
    @Transactional
    public void blockAccount(String accountNumber) {

        log.info("Blocking account: {}", accountNumber);

        Account account = accountrepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        account.setStatus(AccountStatus.BLOCKED);
        accountrepository.save(account);

        log.info("Account blocked successfully: {}", accountNumber);
    }

    // Deduct Balance
    @Transactional
    public void deductBalance(
            String accountNumber,
            BigDecimal amount
    ) {
        validateAmount(amount);

        Account account = accountrepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Account not active: " + accountNumber
            );
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountrepository.save(account);

        log.info(
                "Balance deducted successfully. Remaining balance: {}",
                account.getBalance()
        );
    }

    // Credit Balance
    @Transactional
    public void creditBalance(
            String accountNumber,
            BigDecimal amount
    ) {
        validateAmount(amount);

        Account account = accountrepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Account not found: " + accountNumber
                        )
                );

        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException(
                    "Account not active: " + accountNumber
            );
        }

        account.setBalance(account.getBalance().add(amount));
        accountrepository.save(account);

        log.info(
                "Balance credited successfully. Current balance: {}",
                account.getBalance()
        );
    }

    // Validate Transaction Amount
    private void validateAmount(BigDecimal amount) {

        if (amount == null ||
                amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero"
            );
        }
    }

    // Generate Account Number
    private String generateAccountNumber() {

        String accountNumber;

        do {
            long number = secureRandom.nextLong(100_000_000_000L);
            accountNumber = String.format("%012d", number);
        } while (
                accountrepository.existsByAccountNumber(accountNumber)
        );

        return accountNumber;
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
