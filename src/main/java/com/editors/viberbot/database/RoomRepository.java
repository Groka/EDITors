package com.editors.viberbot.database;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository("roomRepository")
public interface RoomRepository  extends JpaRepository<Room, Long>{
	
	Room findByName(String name);

}
