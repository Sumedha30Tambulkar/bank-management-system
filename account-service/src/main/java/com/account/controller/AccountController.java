package com.account.controller;

import com.account.model.AccountRequest;
import com.account.model.BalanceUpdateRequest;
import com.account.model.AccountResponse;
import com.account.service.AccountService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    /**
     * Endpoint to create an account for a user.
     */
    @PostMapping("/create")
    public ResponseEntity<AccountResponse> createAccount(@RequestBody AccountRequest request) {
        logger.info("Creating account for user: {}", request.getUserEmail());
        AccountResponse response = accountService.createAccountOrThrow(request);
        logger.info("Account created successfully for user: {}", request.getUserEmail());
        return ResponseEntity.ok(response);
    }

    /**
     * Get balance of a user based on their email.
     */
    @GetMapping("/balance/{userEmail}")
    public ResponseEntity<Double> getBalance(@PathVariable String userEmail) {
        logger.debug("Fetching balance for user: {}", userEmail);
        Double balance = accountService.getBalanceByEmail(userEmail);
        logger.info("Retrieved balance for user {}: {}", userEmail, balance);
        return ResponseEntity.ok(balance);
    }

    /**
     * Update user balance (credit or debit).
     */
    @PutMapping("/balance/update")
    public ResponseEntity<Double> updateBalance(@RequestBody BalanceUpdateRequest request) {
        logger.info("Updating balance for user: {} | Amount: {} | Type: {}",
                    request.getUserEmail(),
                    request.getAmount(),
                    request.isCredit() ? "CREDIT" : "DEBIT");

        Double updatedBalance = accountService.updateBalance(
                request.getUserEmail(),
                request.getAmount(),
                request.isCredit());

        logger.info("Updated balance for user {}: {}", request.getUserEmail(), updatedBalance);
        return ResponseEntity.ok(updatedBalance);
    }
}
