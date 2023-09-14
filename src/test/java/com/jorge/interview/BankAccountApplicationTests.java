package com.jorge.interview;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.jorge.controller.AccountController;
import com.jorge.service.AccountService;
import com.jorge.service.ProviderService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BankAccountApplicationTests {
	
	@InjectMocks
    AccountController accountController;

    @Mock
    AccountService accountService;

    @Mock
    ProviderService providerService;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
    }
    
    

	@Test
	void contextLoads() {
	}
	
	@Test
	public void testValidateAccountHappyPath() {
	    Map<String, Object> requestPayload = new HashMap<>();
	    requestPayload.put("accountNumber", "123456");
	    requestPayload.put("providers", List.of("Provider1", "Provider2"));

	    Map<String, Boolean> serviceResponse = new HashMap<>();
	    serviceResponse.put("Provider1", true);
	    serviceResponse.put("Provider2", true);

	    when(accountService.validateAccount("123456", List.of("Provider1", "Provider2"))).thenReturn(serviceResponse);

	    ResponseEntity<Map<String, List<Map<String, Object>>>> response = accountController.validateAccount(requestPayload);
	    assertEquals(HttpStatus.OK, response.getStatusCode());
	    
	}
	
	@Test
	public void testValidateAccountResponseSpeed() {
	    Map<String, Object> requestPayload = new HashMap<>();
	    requestPayload.put("accountNumber", "123456");
	    requestPayload.put("providers", List.of("Provider1", "Provider2"));

	    Map<String, Boolean> serviceResponse = new HashMap<>();
	    serviceResponse.put("Provider1", true);
	    serviceResponse.put("Provider2", true);

	    when(accountService.validateAccount("123456", List.of("Provider1", "Provider2"))).thenReturn(serviceResponse);

	    long startTime = System.currentTimeMillis();
	    ResponseEntity<Map<String, List<Map<String, Object>>>> response = accountController.validateAccount(requestPayload);
	    long endTime = System.currentTimeMillis();

	    assertTrue((endTime - startTime) < 2000, "Response time should be less than 2 seconds");
	}
	



}
