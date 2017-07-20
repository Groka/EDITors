package com.editors.viberbot.mvc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.editors.viberbot.database.entity.Reservation;
import com.editors.viberbot.service.ReservationService;

@Controller
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;
	
	@RequestMapping(value = "/reservations", method = RequestMethod.GET)
	public String showReservations(Model model){
		List<Reservation> reservations = reservationService.getAll();
		model.addAttribute("reservations", reservations);
		return "reservation/showall";
	}
	
	@RequestMapping(value = "/addReservation", method = RequestMethod.GET)
	public String addReservationView(Model model){
		Reservation reservation = new Reservation();
		model.addAttribute("reservation", reservation);
		return "reservation/add";
	}
	
	@RequestMapping(value = "/addReservation", method = RequestMethod.POST)
	public @ResponseBody String addReservation(@ModelAttribute Reservation reservation){
		reservationService.reserve(reservation);
		return "Reservation added";
	}
	
	
}
