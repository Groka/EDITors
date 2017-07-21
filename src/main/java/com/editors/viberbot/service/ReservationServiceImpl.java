package com.editors.viberbot.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Service;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.database.repository.ReservationRepository;

@Service("reservationService")

public class ReservationServiceImpl implements ReservationService {
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	RoomService roomService;
	
	@Autowired
	UserService userService;

	@Override
	public List<Reservation> getAll() {
		return reservationRepository.findAll();
	}

	@Override
	public boolean reserve(Reservation reservation) {
		/*
		List<LocalTime> reservations = null;
		try {
			reservations = getFreeRoomCapacitiesOnDate(reservation.getId(), reservation.getDate());
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(reservations.contains(reservation.getTime())) return false;
		*/
		reservationRepository.save(reservation);
		return true;
	}

	@Override
	public boolean delete(Long id) {
		if(!reservationRepository.exists(id)) return false;
		reservationRepository.delete(id);
		return true;
	}

	@Override
	public List<LocalTime> getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date) throws NotFoundException {
		
		Room room = null;
		try {
			room = roomService.getOne(roomId);
		} catch (NotFoundException e) {
			throw e;
		}
		/*
		int hours = room.getStartWorkTime().getHour();
		int minutes = room.getStartWorkTime().getMinute();
		for(; LocalTime.of(hours, 0) != room.getEndWorkTime(); hours++){
			LocalTime tmp = LocalTime.of(hours, minutes);
			result.add(tmp);
		}
		List<Reservation> reservations = getAll();
		for(Reservation r : reservations){
			if(result.contains(r.getTime()) && r.getRoom().getId() == roomId && r.getDate() == date)
				result.remove(r.getTime());
		}
		*/
		List<Reservation> reservations = reservationRepository.findByRoomAndDate(room, date);
		
		List<LocalTime> result = new ArrayList<>();
		//
		int hours = room.getStartWorkTime().getHour();
		int minutes = room.getStartWorkTime().getMinute();
		for(; LocalTime.of(hours, minutes).isBefore(room.getEndWorkTime()); hours++){
			LocalTime tmp = LocalTime.of(hours, minutes);
			result.add(tmp);
		}
		for(Reservation r : reservations){
			if(result.contains(r.getTime()))
				result.remove(r.getTime());
		}
		return result;
	}

	// return list of reservations by user with viberId
	@Override
	public List<Reservation> getByUser(String viberId) {
		User user = new User();
		List<Reservation> reservations = new ArrayList<>();
		try{
			user = userService.getByViberId(viberId);
			reservations.addAll(reservationRepository.findByUser(user));
		}catch(NotFoundException e){
			e.printStackTrace();
		}catch(NullPointerException e){
			System.out.println("(findByUser) Reservations length: " + reservationRepository.findByUser(user).size());
			e.printStackTrace();
		}
		return reservations;
	}

	@Override
	public Reservation edit(Reservation reservation) throws NotFoundException {
		if(!reservationRepository.exists(reservation.getId())) throw new NotFoundException();
		Reservation dbreservation = reservationRepository.findOne(reservation.getId());
		dbreservation.setDate(reservation.getDate());
		dbreservation.setRoom(reservation.getRoom());
		dbreservation.setTime(reservation.getTime());
		reservationRepository.save(dbreservation);
		return reservation;
	}


	@Override
	public Reservation getOne(Long id) {
		Reservation reservation = reservationRepository.findOne(id);
		return reservation;
	}
	

}