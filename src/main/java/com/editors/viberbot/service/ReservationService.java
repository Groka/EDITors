package com.editors.viberbot.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.User;

public interface ReservationService {
	public List<Reservation> getAll();
	public boolean reserve(Reservation reservation);
	public Reservation edit(Reservation reservation) throws NotFoundException;
	public boolean delete(Long id);
	public List<LocalTime> getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date);
	public List<Reservation> getByUser(String viberId);
	public Reservation add(Reservation reservation);
}
