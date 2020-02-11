package com.test.userservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import com.test.userservice.entity.AccountEntity;
import com.test.userservice.entity.UserEntity;
import com.test.userservice.exception.DuplicateRecordException;
import com.test.userservice.exception.RecordNotFoundException;
import com.test.userservice.repository.AccountRepository;
import com.test.userservice.repository.UserRepository;
import com.test.userservice.service.UserServiceImpl;
import com.test.userservice.web.controller.UserController;

@SpringBootTest
public class UserControllerTest {

	UserController userController;

	UserServiceImpl userService;

	@Mock
	UserRepository userRepository;

	@Mock
	AccountRepository accountRepository;

	@PostConstruct
	public void setup() throws Exception {
		userController = new UserController();
		userService = new UserServiceImpl();
		ReflectionTestUtils.setField(userService, "userRepository", userRepository);
		ReflectionTestUtils.setField(userService, "accountRepository", accountRepository);
		ReflectionTestUtils.setField(userController, "userService", userService);
	}

	@Test
	@DisplayName("Integration test which will get the empty user info")
	public void testEmptyAllUsersInfo() {
		List<UserEntity> emptyUserList = new ArrayList<UserEntity>();
		when(userRepository.findAll()).thenReturn(emptyUserList);
		assertEquals(userController.getAllUsersInfo().getBody().size(), 0);
	}

	@Test
	@DisplayName("Integration test which will get the all user info")
	public void testAllUsersInfo() {
		List<UserEntity> userList = new ArrayList<UserEntity>();
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		userList.add(userEntity);
		when(userRepository.findAll()).thenReturn(userList);
		assertEquals(userController.getAllUsersInfo().getBody().size(), 1);
	}

	@Test
	@DisplayName("Integration test which will throw no user exists error")
	public void testEmptyUserById() {
		Optional<UserEntity> emptyUser = Optional.ofNullable(null);
		when(userRepository.findById(1L)).thenReturn(emptyUser);
		Assertions.assertThrows(RecordNotFoundException.class, () -> {
			userController.getUserById(1L);
		});
	}

	@Test
	@DisplayName("Integration test which will get the user info")
	public void testUserById() throws RecordNotFoundException {
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
		accountEntity.setAccountBalance(new BigDecimal("12345.678"));
		accountEntity.setUser(userEntity);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.of(userEntity);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		userEntity = userController.getUserById(1L).getBody();
		 accountEntity = (AccountEntity) userEntity.getAccounts().toArray()[0];
		assertEquals(userEntity.getFirstName(), "testFirstName");
		assertEquals(userEntity.getLastName(), "testLastName");
		assertEquals(userEntity.getEmailId(), "test@test.com");
		assertEquals(userEntity.getPhoneNumber(), "1234567890");
		assertEquals(accountEntity.getAccountBalance().toString(), "12345.678");
	}

	@Test
	@DisplayName("Integration test to verify duplicate record")
	public void testCreateDuplicateUser() throws DuplicateRecordException {
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
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.of(userEntity);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		Assertions.assertThrows(DuplicateRecordException.class, () -> {
			userController.createUser(userEntity);
		});
	}

	@Test
	@DisplayName("Integration test to create record")
	public void testCreateUser() throws DuplicateRecordException {
		UserEntity userEntity = new UserEntity();
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		assertEquals(HttpStatus.CREATED, userController.createUser(userEntity).getStatusCode());
	}

	@Test
	@DisplayName("Integration test to update with no record found exception ")
	public void testUpdateUserRecordNotFound() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.ofNullable(null);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		Assertions.assertThrows(RecordNotFoundException.class, () -> {
			userController.updateUser(userEntity);
		});
	}

	@Test
	@DisplayName("Integration test to update user")
	public void testUpdateUser() throws RecordNotFoundException {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.of(userEntity);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		assertEquals(HttpStatus.OK, userController.updateUser(userEntity).getStatusCode());
	}

	@Test
	@DisplayName("Integration test to update specific fields with record not found exception")
	public void testUpdateUserSpecificFieldsRecordNotFound() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.ofNullable(null);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		Assertions.assertThrows(RecordNotFoundException.class, () -> {
			userController.updateSpecificUserFields(1L, "testFirstName", "testLastName", "test@test.com", "1234567890",
					"testaddr");
		});
	}

	@Test
	@DisplayName("Integration test to update user")
	public void testUpdateUserSpecificFields() throws RecordNotFoundException {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.of(userEntity);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		assertEquals(HttpStatus.OK, userController.updateSpecificUserFields(1L, "testFirstName", "testLastName",
				"test@test.com", "1234567890", "testaddr").getStatusCode());
	}

	@Test
	@DisplayName("Integration test to delete record not found exception")
	public void testDeleteUserRecordNotFound() {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.ofNullable(null);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		Assertions.assertThrows(RecordNotFoundException.class, () -> {
			userController.deleteUserById(1L);
		});
	}

	@Test
	@DisplayName("Integration test to update user")
	public void testDeleteUser() throws RecordNotFoundException {
		UserEntity userEntity = new UserEntity();
		userEntity.setUserId(1L);
		userEntity.setFirstName("testFirstName");
		userEntity.setLastName("testLastName");
		userEntity.setEmailId("test@test.com");
		Set<AccountEntity> accountEntityList = new HashSet<AccountEntity>();
		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setAccountId(1L);
		accountEntityList.add(accountEntity);
		userEntity.setAccounts(accountEntityList);
		Optional<UserEntity> optionalEntity = Optional.of(userEntity);
		when(userRepository.findById(1L)).thenReturn(optionalEntity);
		assertEquals(HttpStatus.NO_CONTENT, userController.deleteUserById(1L));
	}

}