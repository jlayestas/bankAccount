package com.jorge.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

import com.jorge.model.ValidateRequest;
import com.jorge.model.ValidateResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

@Service
public class ProviderService {
	
	@Autowired
    private RestTemplate restTemplate;

    @Value("${provider1.url}")
    private String provider1Url;

    @Value("${provider2.url}")
    private String provider2Url;

    public Map<String, Boolean> validateAccount(String accountNumber, List<String> providers) {
        Map<String, Boolean> results = new HashMap<>();


        if (providers.contains("provider1")) {
            results.put("provider1", true);
        }

        if (providers.contains("provider2")) {
            results.put("provider2", false);
        }

        return results;
    }
    
    @Async
    public Future<Boolean> validateWithProvider(String accountNumber, String providerUrl) {
        
        ValidateRequest validateRequest = new ValidateRequest();
        validateRequest.setAccountNumber(accountNumber);

        ValidateResponse validateResponse = restTemplate.postForObject(
                providerUrl,
                validateRequest,
                ValidateResponse.class
        );

        
        boolean isValid = (validateResponse != null && validateResponse.isValid());

        return CompletableFuture.completedFuture(isValid);
    }
}
