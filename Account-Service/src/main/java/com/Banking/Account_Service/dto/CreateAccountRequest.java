package com.Banking.Account_Service.dto;


import com.Banking.Account_Service.Entity.AccountStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CreateAccountRequest {

     @NotBlank(message = "Account Holder name is required")
     private String accountHolderName;

     @NotBlank(message = "Email required")
     private String email;

    @NotNull(message = "phone is required")
    private String phone;
@NotNull(message = "Account type required")
    private String AccountType;


@NotNull(message = "Initial deposite required")
@Positive(message = "Initial deposite must be positive")
private BigDecimal initialDeposit;
}