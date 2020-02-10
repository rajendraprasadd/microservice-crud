package com.test.userservice.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.test.userservice.entity.UserEntity;
import com.test.userservice.exception.DuplicateRecordException;
import com.test.userservice.exception.RecordNotFoundException;
import com.test.userservice.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	UserService service;

	/**
	 * Endpoint to return all users
	 * 
	 * @return List<UserEntity>
	 */
	@GetMapping(value="/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<List<UserEntity>> getAllUsersInfo() {
		List<UserEntity> userList = service.getAllUsers();
		return new ResponseEntity<List<UserEntity>>(userList, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Endpoint to return user details by id
	 * 
	 * @param id
	 * @return List<UserEntity>
	 * @throws RecordNotFoundException
	 */
	@GetMapping(value="/{id}",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserEntity> getUserById(@PathVariable("id") Long id) throws RecordNotFoundException {
		UserEntity userEntity = service.getUserById(id);
		return new ResponseEntity<UserEntity>(userEntity, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Endpoint to create user account
	 * 
	 * @param User
	 * @return UserEntity
	 * @throws DuplicateRecordException 
	 */
	@PostMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity User) throws DuplicateRecordException {
		UserEntity createdUser = service.createUser(User);
		return new ResponseEntity<UserEntity>(createdUser, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Endpoint to update user details
	 * 
	 * @param User
	 * @return UserEntity
	 * @throws RecordNotFoundException
	 */
	@PutMapping(consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserEntity> updateUser(@RequestBody UserEntity User) throws RecordNotFoundException {
		UserEntity updatedUser = service.updateUser(User);
		return new ResponseEntity<UserEntity>(updatedUser, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Endpoint to update few user fields
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param email
	 * @param phoneNumber
	 * @param address
	 * @return UserEntity
	 * @throws RecordNotFoundException
	 */
	@PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<UserEntity> updateSpecificUserFields(@PathVariable("id") Long id,
			@RequestParam(required = false) String firstName, @RequestParam(required = false) String lastName,
			@RequestParam(required = false) String email, @RequestParam(required = false) String phoneNumber,
			@RequestParam(required = false) String address) throws RecordNotFoundException {
		UserEntity updatedUser = service.updateUserFields(id, firstName, lastName, email, phoneNumber, address);
		return new ResponseEntity<UserEntity>(updatedUser, new HttpHeaders(), HttpStatus.OK);
	}

	/**
	 * Endpoint to delete user details by user Id
	 * 
	 * @param id
	 * @return UserEntity
	 * @throws RecordNotFoundException
	 */
	@DeleteMapping("/{id}")
	public HttpStatus deleteUserById(@PathVariable("id") Long id) throws RecordNotFoundException {
		service.deleteUserById(id);
		return HttpStatus.OK;
	}

}