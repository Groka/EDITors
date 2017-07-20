package com.editors.viberbot.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	//@Autowired
	//UserService userService;

	@Override
	public List<Reservation> getAll() {
		return reservationRepository.findAll();
	}

	@Override
	public boolean reserve(Reservation reservation) {
		List<LocalTime> reservations = getFreeRoomCapacitiesOnDate(reservation.getId(), reservation.getDate());
		if(reservations.contains(reservation.getTime())) return false;
		reservationRepository.save(reservation);
		return true;
	}

	@Override
	public boolean edit(Reservation reservation) {
		if(!reservationRepository.exists(reservation.getId())) return false;
		Reservation dbreservation = reservationRepository.findOne(reservation.getId());
		dbreservation.setDate(reservation.getDate());
		dbreservation.setRoomId(reservation.getRoomId());
		dbreservation.setTime(reservation.getTime());
		reservationRepository.save(dbreservation);
		return true;
	}

	@Override
	public boolean delete(Long id) {
		if(!reservationRepository.exists(id)) return false;
		reservationRepository.delete(id);
		return true;
	}

	@Override
	public List<LocalTime> getFreeRoomCapacitiesOnDate(Long roomId, LocalDate date) {
		List<LocalTime> result = null;
		Room room = roomService.getOne(roomId);
		int hours = room.getStartWorkTime().getHour();
		int minutes = room.getStartWorkTime().getMinute();
		for(; LocalTime.of(hours, 0) != room.getEndWorkTime(); hours++){
			LocalTime tmp = LocalTime.of(hours, minutes);
			result.add(tmp);
		}
		List<Reservation> reservations = getAll();
		for(Reservation r : reservations){
			if(result.contains(r.getTime()) && r.getRoomId() == roomId && r.getDate() == date)
				result.remove(r.getTime());
		}
		return result;
	}

	@Override
	public User getByUser(String viberId) {
		//Dodati hvatanje usera po viber Idu
		User user = null;
		return user;
	}
	
}
