package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.database.repository.UserRepository;

@Service("userService")
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User addUser(User user) {
		userRepository.save(user);
		return user;
	}

	@Override
	public User getByViberId(String viberId) {
		User user = userRepository.findByViberId(viberId);
		//if(userRepository.exists(user.getId())) return Exception
		
		return user;
	}

	@Override
	public void subscribe(String viberId) {
		
		
	}

	@Override
	public void unsubscribe(String viberId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean delete(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

}
