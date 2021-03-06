package com.editors.viberbot.database.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;

@Repository("reservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByUser(User user);
	List<Reservation> findByRoomAndDate(Room room, LocalDate date);
}
