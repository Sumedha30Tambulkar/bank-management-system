package com.example.main.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.dto.DTO.AccountRequest;
import com.dto.DTO.AccountResponse;

@FeignClient(name = "ACCOUNT-SERVICE") // must match account service name in Eureka
public interface AccountClient {

    @PostMapping("/api/accounts/create")
    AccountResponse createAccount(@RequestBody AccountRequest request);
}
