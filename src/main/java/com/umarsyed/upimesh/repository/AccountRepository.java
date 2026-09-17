package com.umarsyed.upimesh.repository;

import com.umarsyed.upimesh.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository
        extends JpaRepository<Account, String> {
}