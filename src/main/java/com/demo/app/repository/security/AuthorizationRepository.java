package com.demo.app.repository.security;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.demo.app.domain.security.Authority;

@Repository
public interface AuthorizationRepository extends CrudRepository<Authority, String> {


}
