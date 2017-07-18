package com.editors.viberbot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.repository.RoomRepository;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room findRoomByName(String name) {
		return roomRepository.findByName(name);
	}

	@Override
	public void saveRoom(Room room) {
		roomRepository.save(room);
	}

}
