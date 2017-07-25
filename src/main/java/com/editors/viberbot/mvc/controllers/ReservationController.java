package com.editors.viberbot.mvc.controllers;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.service.ReservationService;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
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
	public @ResponseBody String editReservation(@ModelAttribute Reservation reservation) throws NotFoundException{
		reservationService.edit(reservation);
		return "Reservation edited";
	}
	
	@RequestMapping(value = "/reservations/{id}", method = RequestMethod.GET)
	public String deleteReservation(@PathVariable Long id, Model model){
		Reservation reservation = reservationService.getOne(id);
		reservationService.delete(reservation.getId());
		return "redirect:/rooms";	
	}	
	
}
