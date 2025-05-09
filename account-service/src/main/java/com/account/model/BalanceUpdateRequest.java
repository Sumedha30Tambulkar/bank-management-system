package com.account.model;


import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BalanceUpdateRequest {
    private String userEmail;
    private double amount;
    private boolean credit;

    // Getters and Setters
}




