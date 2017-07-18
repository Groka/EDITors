package com.editors.viberbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.repository.ReservationRepository;

@Service("reservationService")
public class ReservationServiceImpl implements ReservationService {
	@Autowired
	ReservationRepository reservationRepository;

	@Override
	public List<Reservation> getAll() {
		return reservationRepository.findAll();
	}

	@Override
	public void reserve(Reservation reservation) {
		
		
	}

	@Override
	public Reservation edit(Reservation reservation) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}
	
}
