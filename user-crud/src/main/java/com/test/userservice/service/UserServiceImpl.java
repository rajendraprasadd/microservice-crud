package com.test.userservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.test.userservice.entity.UserEntity;
import com.test.userservice.exception.DuplicateRecordException;
import com.test.userservice.exception.RecordNotFoundException;
import com.test.userservice.repository.AccountRepository;
import com.test.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	AccountRepository accountRepository;

	public List<UserEntity> getAllUsers() {
		List<UserEntity> UserList = userRepository.findAll();

		if (UserList.size() > 0) {
			return UserList;
		} else {
			return new ArrayList<UserEntity>();
		}
	}

	public UserEntity getUserById(Long id) throws RecordNotFoundException {
		Optional<UserEntity> User = userRepository.findById(id);

		if (User.isPresent()) {
			return User.get();
		} else {
			throw new RecordNotFoundException("No User record exist for given id");
		}
	}

	@Transactional(readOnly = false)
	public UserEntity createUser(UserEntity entity) throws DuplicateRecordException{
		if (null != entity.getUserId()) {
			Optional<UserEntity> User = userRepository.findById(entity.getUserId());
			if (User.isPresent()) {
				throw new DuplicateRecordException("User record exist for given id");
			}
		}
		userRepository.save(entity);
		entity.getAccounts().forEach(acc -> {
			acc.setUser(entity);
			entity.getAccounts().add(acc);
			accountRepository.save(acc);
		});
		return entity;
	}

	public void deleteUserById(Long id) throws RecordNotFoundException {
		Optional<UserEntity> User = userRepository.findById(id);
		if (User.isPresent()) {
			userRepository.deleteById(id);
		} else {
			throw new RecordNotFoundException("No User record exist for given id");
		}
	}

	public UserEntity updateUser(UserEntity user) throws RecordNotFoundException {
		Optional<UserEntity> dbUser = userRepository.findById(user.getUserId());

		if (dbUser.isPresent()) {
			userRepository.save(user);
			user.getAccounts().forEach(acc -> {
				acc.setUser(user);
				user.getAccounts().add(acc);
				accountRepository.save(acc);
			});
			return user;
		} else {
			throw new RecordNotFoundException("No User record exist for given id");
		}
	}

	public UserEntity updateUserFields(Long id, String firstName, String lastName, String email, String phoneNumber,
			String address) throws RecordNotFoundException {
		Optional<UserEntity> user = userRepository.findById(id);

		if (user.isPresent()) {
			UserEntity userEntity = user.get();
			if (!StringUtils.isEmpty(firstName)) {
				userEntity.setFirstName(firstName);
			}
			if (!StringUtils.isEmpty(lastName)) {
				userEntity.setLastName(lastName);
			}
			if (!StringUtils.isEmpty(email)) {
				userEntity.setPhoneNumber(phoneNumber);
			}
			if (!StringUtils.isEmpty(phoneNumber)) {
				userEntity.setAddress(address);
			}
			return userRepository.save(userEntity);
		} else {
			throw new RecordNotFoundException("No User record exist for given id");
		}
	}
}