package com.test.client.userservice.web.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.client.userservice.entity.AccountEntity;
import com.test.client.userservice.entity.UserEntity;
import com.test.client.userservice.service.UserClientService;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserClientController {

	@Autowired
	private UserClientService userClientservice;

	/**
	 * Endpoint to return all users individual total balance
	 * 
	 * @return Map<Long, Object>
	 */
	@GetMapping(value = "/totalaccountbalance", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<Long, Object>> getAllUsersBalanceAverage() {
		ResponseEntity<List<UserEntity>> userResponse = userClientservice.getAllUsersInfo();
		Stream<UserEntity> userStream = userResponse.getBody().parallelStream();
		Map<Long, Object> result = userStream.collect(Collectors.toMap(UserEntity::getUserId, x->x.getAccounts().stream().map(acc->acc.getAccountBalance()).reduce(new BigDecimal(0), (bal1,bal2) -> bal1.add(bal2))));
		return new ResponseEntity<Map<Long, Object>>(result, new HttpHeaders(), HttpStatus.OK);
	}

}