package com.demo.app.repository.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.app.domain.security.User;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

	public User findByUsername(String username);

}
