package com.editors.viberbot.service;

import java.util.List;

import com.editors.viberbot.database.entity.User;

public interface UserService {
	public List<User> findAll();
	public User addUser(User user);
	public User getByViberId(String viberId);
	public void subscribe(String viberId);
	public void unsubscribe(String viberId);
	public boolean delete(Long id);
}
