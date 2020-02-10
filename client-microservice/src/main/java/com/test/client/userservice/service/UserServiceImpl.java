package com.test.client.userservice.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.test.client.userservice.entity.UserEntity;

@Service
public class UserServiceImpl implements UserService {

	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${user-service.baseUrl}") 
	String baseUrl;
	
	RequestEntity request;
	
	@PostConstruct
	private void init( ) throws URISyntaxException {
		request = RequestEntity
			     .get(new URI(baseUrl+"/users/all"))
			     .accept(MediaType.APPLICATION_JSON).build();

	}
	
	@Override
	public ResponseEntity<List<UserEntity>> getAllUsersInfo() {
		return restTemplate.exchange(request,new ParameterizedTypeReference<List<UserEntity>>() {}); 
	}
}