package com.ssubijana.roleauthorization.mapper;

import com.ssubijana.roleauthorization.domain.User;
import com.ssubijana.roleauthorization.web.presentation.AuthorizationRequest;
import com.ssubijana.roleauthorization.web.presentation.UserResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserMapperTest {

	@Autowired
	private UserMapper userMapper;

	private static final long USER_ID = 1L;

	private static final String USER_NAME = "USER_NAME";

	@Test
	public void toResponseShouldReturnValidUserResponse() {
		User user = User.builder().id(USER_ID).name(USER_NAME).password("USER_PASSWORD").build();

		UserResponse userResponse = userMapper.toResponse(user);

		assertThat(userResponse.getId()).isEqualTo(user.getId());
		assertThat(userResponse.getName()).isEqualTo(user.getName());

	}

	@Test
	public void toDomainShouldReturnValidUser() {
		AuthorizationRequest authorizationRequest = AuthorizationRequest.builder().userName(USER_NAME)
				.password("USER_PASSWORD").build();

		User user = userMapper.toDomain(authorizationRequest);

		assertThat(user.getName()).isEqualTo(authorizationRequest.getUserName());
		assertThat(user.getPassword()).isEqualTo(authorizationRequest.getPassword());
	}
}
