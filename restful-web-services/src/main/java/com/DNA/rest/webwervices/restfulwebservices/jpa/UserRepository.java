package com.DNA.rest.webwervices.restfulwebservices.jpa;

import com.DNA.rest.webwervices.restfulwebservices.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
