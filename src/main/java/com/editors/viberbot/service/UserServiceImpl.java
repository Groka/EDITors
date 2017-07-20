package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
	public User getByViberId(String viberId) throws NotFoundException {
		User user = userRepository.findByViberId(viberId);
		if(userRepository.exists(user.getId())) throw new NotFoundException();
		return user;
	}

	@Override
	public void subscribe(String viberId) throws NotFoundException {
		User user = new User();
		try{
			user = getByViberId(viberId);
			if(!user.isSubscribed()) user.setSubscribe(true);
			userRepository.save(user);
		}catch(NotFoundException e){
			throw e;
		}
	}

	@Override
	public void unsubscribe(String viberId) throws NotFoundException {
		User user = new User();
		try{
			user = getByViberId(viberId);
			if(user.isSubscribed()) user.setSubscribe(false);
			userRepository.save(user);
		}catch(NotFoundException e){
			throw e;
		}
	}

	@Override
	public User delete(Long id) {
		User user = new User();
		user = userRepository.getOne(id);
		userRepository.delete(user);
		return user;
	}

}
