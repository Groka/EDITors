package com.editors.viberbot.mvc.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.service.ReservationService;
import com.editors.viberbot.service.RoomService;
import com.editors.viberbot.service.UserService;

import scala.annotation.meta.setter;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoomService roomService;
	
	
	@RequestMapping(value = "/reservations", method = RequestMethod.GET)
	public String showReservations(Model model){
		List<Reservation> reservations = reservationService.getAll();
		model.addAttribute("reservations", reservations);
		return "reservation/list";
	}
	
	
	@RequestMapping(value = "/editreservation/{id}", method = RequestMethod.GET)
	public String editReservationView(@PathVariable Long id, Model model){
		Reservation reservation = reservationService.getOne(id);
		model.addAttribute("reservation", reservation);
		return "reservation/edit";	
	}
	
	@RequestMapping(value = "/editreservation", method = RequestMethod.POST)
	public @ResponseBody String editReservation(@RequestParam String time, @RequestParam String date,

			@RequestParam Long userId,  @RequestParam Long roomId, @RequestParam Long reservationId ) throws NotFoundException{
		
		LocalDate newdate = LocalDate.parse(date);
		LocalTime newtime = LocalTime.parse(time);
		
		User user = userService.getOne(Long.valueOf(userId));
		Room room = roomService.getOne(Long.valueOf(roomId));
		
		Reservation reservation = reservationService.getOne(reservationId);			
		reservation.setRoom(room);
		reservation.setTime(newtime);
		reservation.setUser(user);
		reservation.setDate(newdate);
	
		reservationService.edit(reservation);
		
		return "redirect:/reservations";
	}
	
	@RequestMapping(value = "/reservations/{id}", method = RequestMethod.GET)
	public String deleteReservation(@PathVariable Long id, Model model){
		Reservation reservation = reservationService.getOne(id);
		reservationService.delete(reservation.getId());
		return "redirect:/rooms";	
	}	
	
}
