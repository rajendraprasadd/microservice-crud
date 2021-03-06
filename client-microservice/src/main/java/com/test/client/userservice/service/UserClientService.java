package com.test.client.userservice.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.test.client.userservice.entity.UserEntity;

public interface UserClientService {

	public ResponseEntity<List<UserEntity>> getAllUsersInfo();

}