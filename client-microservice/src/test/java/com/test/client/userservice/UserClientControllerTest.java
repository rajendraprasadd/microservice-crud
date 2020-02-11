package com.test.client.userservice;

import static org.hamcrest.CoreMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.anything;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.test.client.userservice.entity.AccountEntity;
import com.test.client.userservice.entity.UserEntity;
import com.test.client.userservice.service.UserClientServiceImpl;
import com.test.client.userservice.web.controller.UserClientController;

@SpringBootTest
public class UserClientControllerTest {

	UserClientController userClientController;

	UserClientServiceImpl userClientservice;

	@Mock
	RestTemplate restTemplate;

	@PostConstruct
	public void setup() throws Exception {
		userClientController = new UserClientController();
		userClientservice = new UserClientServiceImpl();
		ReflectionTestUtils.setField(userClientservice, "restTemplate", restTemplate);
		ReflectionTestUtils.setField(userClientservice, "request", RequestEntity.get(new URI("/users/all")).accept(MediaType.APPLICATION_JSON).build());
		ReflectionTestUtils.setField(userClientservice, "baseUrl", "");
		ReflectionTestUtils.setField(userClientController, "userClientservice", userClientservice);
	}

	@Test
	public void testGetAllUsersBalanceAverage() throws RestClientException, URISyntaxException {
		List<UserEntity> userEntityList = new ArrayList<>();
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		userEntity.setAddress("testaddress");
		userEntity.setPhoneNumber("1234567890");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntity.setAccountBalance(new BigDecimal("120"));
		accountEntity.setUser(userEntity);
		accountEntityList.add(accountEntity);
		AccountEntity accountEntity2 = new AccountEntity();
		accountEntity2.setAccountId(2L);
		accountEntity2.setAccountBalance(new BigDecimal("80"));
		accountEntity2.setUser(userEntity);
		accountEntityList.add(accountEntity2);
		userEntity.setAccounts(accountEntityList);
		userEntityList.add(userEntity);
		ResponseEntity<List<UserEntity>> responseEntity = new ResponseEntity<List<UserEntity>>(userEntityList, HttpStatus.OK);
		when(restTemplate.exchange(RequestEntity.get(new URI("/users/all")).accept(MediaType.APPLICATION_JSON).build(),new ParameterizedTypeReference<List<UserEntity>>() {})).thenReturn(responseEntity);
		assertEquals(new BigDecimal(200), userClientController.getAllUsersBalanceAverage().getBody().get(1L));
	}

}