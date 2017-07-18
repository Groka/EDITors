package com.editors.viberbot.service;

import com.editors.viberbot.database.entity.Room;

public interface RoomService {
	public Room findRoomByName(String name);
	public void saveRoom(Room room);
}
