package com.editors.viberbot.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;

@Repository("reservationRepository")
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
}
