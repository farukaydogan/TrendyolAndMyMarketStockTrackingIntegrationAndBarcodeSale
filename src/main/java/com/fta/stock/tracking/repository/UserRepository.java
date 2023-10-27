package com.fta.stock.tracking.repository;

import com.fta.stock.tracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

  Optional<User> findByEmail(String email);


  Optional<User> findById(Integer id);

}
