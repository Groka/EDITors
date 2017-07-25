package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.Reservation;
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
		try {
			getByViberId(user.getViberid());
		} catch (NotFoundException e) {
			userRepository.save(user);
			System.out.println("User successfully added");
		}

		return user;
	}

	@Override
	public User getByViberId(String viberid) throws NotFoundException {
		User user = userRepository.findByViberid(viberid);
		if(user == null || !userRepository.exists(user.getId())) throw new NotFoundException();
		return user;
	}
	
	@Override
	public User getByName(String name) throws NotFoundException {
		User user = userRepository.findByName(name);
		if(user == null || !userRepository.exists(user.getId())) throw new NotFoundException();
		return user;
	}

	
	@Override
	public void subscribe(String viberId) throws NotFoundException {
		User user = new User();
		try{
			user = getByViberId(viberId);
			if(!user.getSubscribe()) user.setSubscribe(true);
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
			if(user.getSubscribe()) user.setSubscribe(false);
			userRepository.save(user);
		}catch(NotFoundException e){
			throw e;
		}
	}

	@Override
	public User delete(Long id) {
		User user = userRepository.getOne(id);
		userRepository.delete(user);
		return user;
	}

	@Override
	public User getOne(Long id) {
		return userRepository.findOne(id);
	}

	@Override
	public User update(User user) throws NotFoundException  {
	
			if(!userRepository.exists(user.getId())) throw new NotFoundException();
			User dbuser = userRepository.findOne(user.getId());
			dbuser.setViberid(user.getViberid());
			dbuser.setName(user.getName());
			dbuser.setSubscribe(user.getSubscribe());
			userRepository.save(dbuser);
			return user;
		
	}

}
