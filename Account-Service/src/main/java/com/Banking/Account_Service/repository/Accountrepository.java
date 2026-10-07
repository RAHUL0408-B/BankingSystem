package com.Banking.Account_Service.repository;

import com.Banking.Account_Service.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Accountrepository extends JpaRepository<Account,String> {
    boolean existsByEmail(String email);
}
