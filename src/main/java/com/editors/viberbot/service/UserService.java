package com.editors.viberbot.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.editors.viberbot.database.entity.User;

public interface UserService {
	public List<User> findAll();
	public User addUser(User user);
	public User getByViberId(String viberId) throws NotFoundException;
	public void subscribe(String viberId) throws NotFoundException;
	public void unsubscribe(String viberId) throws NotFoundException;
	public User delete(Long id);
}
