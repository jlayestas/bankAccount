package com.jorge.service;

import com.jorge.model.Account;
import com.jorge.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.lang.InterruptedException;



@Service
public class AccountService {
	
	@Value("${data.provider.url}")
    private String dataProviderUrl;
	
	@Value("#{${data.providers}}")
    private Map<String, String> providerUrls;

    @Autowired
    private AccountRepository accountRepository;
    
    @Autowired
    private ProviderService providerService;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account updateAccount(Account account) {
        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        accountRepository.deleteById(id);
    }
    
    private String getProviderUrl(String providerName) {
        return providerUrls.get(providerName);
    }
    
    public Map<String, Boolean> validateAccount(String accountNumber, List<String> providers) {
        Map<String, Future<Boolean>> futures = new HashMap<>();
        Map<String, Boolean> results = new HashMap<>();

        try {
            for (String provider : providers) {
                Future<Boolean> future = providerService.validateWithProvider(accountNumber, getProviderUrl(provider));
                futures.put(provider, future);
            }

            for (Map.Entry<String, Future<Boolean>> entry : futures.entrySet()) {
                results.put(entry.getKey(), entry.getValue().get());
            }
        } catch (InterruptedException | ExecutionException e) {
            
            throw new RuntimeException("Failed to validate account", e);
        }

        return results;
    }

    
    
}
