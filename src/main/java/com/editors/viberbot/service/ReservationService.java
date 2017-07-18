package com.editors.viberbot.service;

import java.util.List;

import com.editors.viberbot.database.entity.Reservation;

public interface ReservationService {
	public List<Reservation> getAll();
	public void reserve(Reservation reservation);
	public Reservation edit(Reservation reservation);
	public void delete(Long id);
	
	//public User getByUser(String viberId);
}
