package com.transaction.main.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.transaction.main.entity.Transaction;
import com.transaction.main.repository.TransactionRepository;
import com.transaction.main.service.TransactionService;

@RestController
@RequestMapping("api/secure")
public class TransactionController {

    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * Endpoint to perform a transaction from sender to receiver.
     */
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> doTransaction(@RequestBody Transaction transaction) {
        logger.info("Initiating transaction from {} to {} of amount {}",
                    transaction.getSenderAccount(), transaction.getReceiverAccount(), transaction.getAmount());

        try {
            Transaction savedTransaction = transactionService.performTransaction(
                    transaction.getSenderAccount(),
                    transaction.getReceiverAccount(),
                    transaction.getAmount()
            );
            return ResponseEntity.ok(savedTransaction);
        } catch (Exception e) {
            logger.error("Transaction failed: {}", e.getMessage(), e);
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * Endpoint to fetch all sent transactions of a sender.
     */
    @GetMapping("/sent/{senderAccount}")
    public List<Transaction> getSentTransactions(@PathVariable String senderAccount) {
        logger.debug("Fetching sent transactions for account: {}", senderAccount);
        return transactionService.getSentTransactions(senderAccount);
    }

    /**
     * Endpoint to fetch all received transactions of a receiver.
     */
    @GetMapping("/received/{receiverAccount}")
    public List<Transaction> getReceivedTransactions(@PathVariable String receiverAccount) {
        logger.debug("Fetching received transactions for account: {}", receiverAccount);
        return transactionService.getReceivedTransactions(receiverAccount);
    }

    /**
     * Endpoint to get all transaction history for a user (sent + received).
     */
    @GetMapping("/history/{email}")
    public List<Transaction> getAllTransactions(@PathVariable String email) {
        logger.debug("Fetching complete transaction history for email: {}", email);
        return transactionService.getAllTransactionHistory(email);
    }

    /**
     * Test endpoint to verify API is up.
     */
    @GetMapping("/home")
    public String hello() {
        logger.info("Transaction service /home endpoint hit");
        return "Hello and Welcome!";
    }
}
