package com.editors.viberbot.mvc.controllers.room;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.repository.RoomRepository;
import com.editors.viberbot.service.RoomService;

@Controller
public class RoomController {
	@Autowired
	private RoomService roomService;
	
	@RequestMapping("/rooms")
	public String listRooms(Model model){
		model.addAttribute("rooms", roomService.findAll());
		System.out.println("Velicina: " + roomService.findAll().size());
		return "room/listrooms";
	}
	
	
	@RequestMapping(value = "/editRoom/{id}", method = RequestMethod.GET)
	public String editRoomView(@PathVariable Long id, Model model){
		Room room = null;
		try {
			room = roomService.getOne(id);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		model.addAttribute("room", room);
		return "room/editRoom";	
	}
	
	@RequestMapping(value = "/editRoom", method = RequestMethod.POST)
	public String editRoom(@ModelAttribute Room room) throws NullPointerException {
		//Room room = roomService.getOne(id);
		try {
			roomService.update(room);
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(room.toString());
		return "redirect:/rooms";
	}
	
	@RequestMapping(value = "/addRoom", method = RequestMethod.GET)
	public String addRoomView(Model model){
		model.addAttribute("rooom", new Room());
		return "room/addRoom";	
	}
	
	@RequestMapping(value = "/addRoom", method = RequestMethod.POST)
	public String addRoom (@RequestParam String name,
			@RequestParam int number,
			@RequestParam String startWorkTime,
			@RequestParam String endWorkTime) throws NullPointerException {
		
		LocalTime newSWT = LocalTime.parse(startWorkTime);
		LocalTime newEWT = LocalTime.parse(endWorkTime);

		Room room = new Room(name, number, newSWT, newEWT);
		roomService.add(room);
		return "redirect:/rooms";
	}
	
	
	@RequestMapping(value = "/rooms/{id}", method = RequestMethod.GET)
	public String deleteRoomView(@PathVariable Long id, Model model){
		try {
			Room room = roomService.getOne(id);
			roomService.delete(room.getId());
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "redirect:/rooms";	
	}	
	
}
