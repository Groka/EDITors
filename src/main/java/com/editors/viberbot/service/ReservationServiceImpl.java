package com.editors.viberbot.service;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.io.Console;
import java.time.LocalDate;
import java.time.LocalTime;
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
		List<LocalTime> reservations = getFreeRoomCapacitiesOnDate(reservation.getId(), reservation.getDate());
		if(reservations.contains(reservation.getTime())) return false;
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

	// return list of reservations by user with viberId
	@Override
	public List<Reservation> getByUser(String viberId) {
		User user = new User();
		List<Reservation> reservations = null;
		try{
			user = userService.getByViberId(viberId);
			reservations.addAll(reservationRepository.findByUserId(user.getId()));
		}catch(NotFoundException e){
			System.out.println(e.getMessage());
			
		}
		return reservations;
	}

	@Override
	public Reservation edit(Reservation reservation) throws NotFoundException {
		if(!reservationRepository.exists(reservation.getId())) throw new NotFoundException();
		Reservation dbreservation = reservationRepository.findOne(reservation.getId());
		dbreservation.setDate(reservation.getDate());
		dbreservation.setRoomId(reservation.getRoomId());
		dbreservation.setTime(reservation.getTime());
		reservationRepository.save(dbreservation);
		return reservation;
	}

	@Override
	public Reservation add(Reservation reservation) {
		reservationRepository.save(reservation);
		return null;
	}
	
}
