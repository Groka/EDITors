package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
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
	public void update(Room room) throws NotFoundException {
		Room dbroom = getOne(room.getId());
		dbroom.setName(room.getName());
		dbroom.setNumber(room.getNumber());
		dbroom.setStartWorkTime(room.getStartWorkTime());
		dbroom.setEndWorkTime(room.getEndWorkTime());
		roomRepository.save(dbroom);
	}

	@Override
	public Room getOne(Long id) throws NotFoundException {
		Room room = roomRepository.findOne(id);
		if(room == null)
			throw new NotFoundException();
		return room;
	}

	@Override
	public void delete(Long id) throws NotFoundException {
		try{
			roomRepository.delete(id);
		}catch(IllegalArgumentException e){
			e.printStackTrace();
		}
	}
	

}
