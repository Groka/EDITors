package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.database.repository.RoomRepository;

@Service("roomService")
public class RoomServiceImpl implements RoomService {
	
	@Autowired
	private RoomRepository roomRepository;

	@Override
	public Room add(Room room) {
		roomRepository.save(room);
		return room;
	}

	@Override
	public List<Room> findAll() {
		return roomRepository.findAll();
	}

	@Override
	public boolean update(Room room) {
		if(!roomRepository.exists(room.getId())) return false;
		Room dbroom = getOne(room.getId());
		dbroom.setName(room.getName());
		dbroom.setNumber(room.getNumber());
		dbroom.setStartWorkTime(room.getStartWorkTime());
		dbroom.setEndWorkTime(room.getEndWorkTime());
		roomRepository.save(dbroom);
		return true;
	}

	@Override
	public Room getOne(Long id) {
		return roomRepository.findOne(id);
	}

	@Override
	public void delete(Long id) {
		roomRepository.delete(id);
	}
	

}
