package com.editors.viberbot.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.editors.viberbot.database.entity.Room;

@Repository("roomRepository")
public interface RoomRepository  extends JpaRepository<Room, Long>{
	

}
