package com.editors.viberbot.service;

import java.util.List;

import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;

public interface RoomService {
	public Room add(Room room);
	public List<Room> findAll();
	public boolean update(Room room);
	public Room getOne(Long id);
	public void delete(Long id);
}
