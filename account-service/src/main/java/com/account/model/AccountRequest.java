package com.account.model;

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
public class AccountRequest {
    private String userEmail;
    private String accountType;
    private double balance;
    

    // Getters and Setters
}
    
    


