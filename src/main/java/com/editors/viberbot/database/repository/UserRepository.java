package com.editors.viberbot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.editors.viberbot.database.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByViberId(String viberId);
}
