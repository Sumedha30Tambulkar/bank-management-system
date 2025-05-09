package com.account.service;

import com.account.model.AccountRequest;
import com.account.model.AccountResponse;
import com.account.model.Account;
import com.account.repository.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountService.class);

    @Autowired
    private AccountRepository accountRepository;

    /**
     * Creates a new account only if it doesn't already exist.
     */
    public AccountResponse createAccountOrThrow(AccountRequest request) {
        logger.info("Request received to create account for email: {}", request.getUserEmail());

        Optional<Account> existingAccount = accountRepository.findByUserEmail(request.getUserEmail());
        if (existingAccount.isPresent()) {
            logger.warn("Account already exists for email: {}", request.getUserEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account already exists for user.");
        }

        if (request.getBalance() < 1000.0) {
            logger.warn("Initial balance ₹{} is less than required ₹1000 for email: {}", request.getBalance(), request.getUserEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Minimum ₹1000 required to open an account.");
        }

        Account account = new Account();
        account.setUserEmail(request.getUserEmail());
        account.setAccountType(request.getAccountType());
        account.setBalance(request.getBalance());
        account.setCreatedAt(LocalDateTime.now());

        Account saved = accountRepository.save(account);
        logger.info("Account successfully created for email: {}", saved.getUserEmail());

        return mapToResponse(saved);
    }

    /**
     * Returns current balance for the given user email.
     */
    public Double getBalanceByEmail(String userEmail) {
        logger.info("Fetching balance for user: {}", userEmail);

        Account account = accountRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    logger.error("Account not found for user: {}", userEmail);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found for user.");
                });

        logger.debug("Current balance for {} is ₹{}", userEmail, account.getBalance());
        return account.getBalance();
    }

    /**
     * Updates balance of account: adds (credit) or deducts (debit) the amount.
     */
    public Double updateBalance(String userEmail, double amount, boolean credit) {
        logger.info("Updating balance for {}. Amount: ₹{}, Credit: {}", userEmail, amount, credit);

        Account account = accountRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> {
                    logger.error("No account found for user: {}", userEmail);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "No account found for user.");
                });

        double currentBalance = account.getBalance();
        double newBalance = credit ? currentBalance + amount : currentBalance - amount;

        if (!credit && newBalance < 0) {
            logger.warn("Insufficient balance. Tried to debit ₹{} from ₹{} for {}", amount, currentBalance, userEmail);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient balance.");
        }

        account.setBalance(newBalance);
        accountRepository.save(account);

        logger.info("Balance successfully updated for {}. New balance: ₹{}", userEmail, newBalance);
        return newBalance;
    }

    /**
     * Maps Account entity to AccountResponse DTO.
     */
    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setAccountId(account.getAccountId());
        response.setUserEmail(account.getUserEmail());
        response.setAccountType(account.getAccountType());
        response.setBalance(account.getBalance());
        response.setCreatedAt(account.getCreatedAt());
        return response;
    }
}
