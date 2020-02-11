package com.test.userservice.service;

import java.util.List;

import com.test.userservice.entity.UserEntity;
import com.test.userservice.exception.DuplicateRecordException;
import com.test.userservice.exception.RecordNotFoundException;

public interface UserService {

	public List<UserEntity> getAllUsers();

	public UserEntity getUserById(Long id) throws RecordNotFoundException;

	public UserEntity createUser(UserEntity entity) throws DuplicateRecordException;

	public String deleteUserById(Long id) throws RecordNotFoundException;

	public UserEntity updateUser(UserEntity user) throws RecordNotFoundException;

	public UserEntity updateUserFields(Long id, String firstName, String lastName, String email, String phoneNumber,
			String address) throws RecordNotFoundException;
}