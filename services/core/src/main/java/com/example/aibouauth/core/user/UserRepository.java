package com.example.aibouauth.core.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User,Integer> {



    Optional<User> findUserByEmail(String email);

    Optional<User> findUserById (Integer id);


}
