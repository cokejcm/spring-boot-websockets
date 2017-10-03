package com.demo.app.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.demo.app.domain.security.User;
import com.demo.app.repository.security.UserRepository;

@Component
public class UserService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();
	@Autowired
	UserRepository userRepository;

	@Override
	public final org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException {
		final User user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		org.springframework.security.core.userdetails.User springUser = user.createSpringUser();
		detailsChecker.check(springUser); // Check locked, enabled, expired
		return springUser;
	}

	public final User loadUserAppByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUsername(username);
	}

	public User saveOne(User user) {
		return userRepository.save(user);
	}

	public void deleteOne(User user) {
		userRepository.delete(user);
	}
}
