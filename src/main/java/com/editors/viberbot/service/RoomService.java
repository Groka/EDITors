package com.editors.viberbot.service;

import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.editors.viberbot.database.entity.Room;

public interface RoomService {
	public Room add(Room room);
	public List<Room> findAll();
	public void update(Room room) throws NotFoundException;
	public Room getOne(Long id) throws NotFoundException;
	public void delete(Long id) throws NotFoundException;
}
