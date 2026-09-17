package com.umarsyed.upimesh.config;

import com.umarsyed.upimesh.model.Account;
import com.umarsyed.upimesh.repository.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner seedDemoAccounts(
            AccountRepository accountRepository) {

        return args -> {
            if (accountRepository.count() == 0) {
                accountRepository.save(
                        new Account(
                                "alice@upi",
                                "Alice",
                                new BigDecimal("5000.00"),
                                "demo-pin-alice"
                        )
                );

                accountRepository.save(
                        new Account(
                                "bob@upi",
                                "Bob",
                                new BigDecimal("2000.00"),
                                "demo-pin-bob"
                        )
                );
            }
        };
    }
}