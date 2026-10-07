package com.Banking.Account_Service.dto;

import com.Banking.Account_Service.Entity.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class AcconutResponse {
    private String id;
    private String accountNumber;
    private String accountHolderName;
    private String email;
    private String phone;
    private String AccountType;
    private AccountStatus status;
    private BigDecimal balance;
    private BigDecimal DailyTransactionLimit;
    private LocalDateTime createdAt;

}
