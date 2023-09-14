package com.jorge.controller;

import com.jorge.model.Account;
import com.jorge.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable Long id) {
        Optional<Account> account = accountService.getAccountById(id);
        return account.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        account.setId(id);
        return ResponseEntity.ok(accountService.updateAccount(account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping(value = "/validate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Map<String, List<Map<String, Object>>>> validateAccount(@RequestBody Map<String, Object> requestPayload) {
    	logger.debug("Inside validateAccount method");
        String accountNumber = (String) requestPayload.get("accountNumber");
        @SuppressWarnings("unchecked")
		List<String> providers = (List<String>) requestPayload.getOrDefault("providers", new ArrayList<>());

        Map<String, Boolean> validationResults = accountService.validateAccount(accountNumber, providers);

        List<Map<String, Object>> resultList = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : validationResults.entrySet()) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("provider", entry.getKey());
            resultMap.put("isValid", entry.getValue());
            resultList.add(resultMap);
        }

        Map<String, List<Map<String, Object>>> wrappedResponse = new HashMap<>();
        wrappedResponse.put("result", resultList);

        return ResponseEntity.ok(wrappedResponse);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("I am good!", HttpStatus.OK);
    }


}
