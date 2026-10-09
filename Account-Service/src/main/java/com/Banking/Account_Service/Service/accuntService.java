```java
        package com.Banking.Account_Service.Service;

import com.Banking.Account_Service.Entity.Account;
import com.Banking.Account_Service.Entity.AccountStatus;
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

    public AccountResponse getAccount(String accountNumber){
        Acconut account = accountrepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new RuntimeException("Account not Found"));
        return mapToResponse(account);
    }
    public BigDecimal getAccount(String accountNumber){
        Acconut account = accountrepository.findByAccountNumber(accountNumber).orElseThrow(
                () -> new RuntimeException("Account not Found"));
        return account.getBalance();
    }

    public void BlockedAccount(String accountNumber) {
        log.info("Blocking Account for: {}", accountNumber);
        Account account = accountrepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus(AccountStatus.BLOCKED);
        accountrepository.save(account);
        log.info(
                "Account blocked Successfully: {}",
                accountNumber);
    }
    public void deductBalance(String accountNumber,BigDecimal amount ){
        log.info("deducting balance {} from account: ,amount, accountNumber);
                Account account = accountrepository.findByAccountNumber(accountNumber).orElseThro(() -> new RuntimeException("Account Not Found"));
           if(account.getStatus() != AccountStatus.ACTIVE){
               throw new RuntimeException("Account Not Active" +accountNumber);
           }
           if(account.getBalance().compareTo(amount) < 0){
               throw new RuntimeException("Insufficient Balance");
           }
           account.setBalance(account.getBalance(),subtract(amount));
           accountRepository.save(account);
           log.info(
                   "Account update Successfully: {}",
                   account.getbalance());
    }

    public void creditBalance(String accountNumber,BigDecimal amount){
        log.info("crediting balance {} from account: ,amount, accountNumber);");
        Account account = accountrepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account Not Found"));
        account.setBalance(account.getBalance().add(amount));
        accountrepository
                .save(account);
        log.info("Account credited Successfully: {}",account.getBalance());
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
    //genereate unique AccountNumber

  private string generateAccountNumbeer(){
        String accountNumber;
        do{
            long Number secureRandom.nextlong(100000000000l);
            accountNumber = String.format("%12d",number);
      }while (accountrepository.existByAccountNumber(accountNumber))
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
```