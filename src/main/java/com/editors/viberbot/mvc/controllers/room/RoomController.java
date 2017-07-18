package com.editors.viberbot.mvc.controllers.room;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
	
	/*
	@Autowired
	public void setRoomService(RoomService roomService){
		this.roomService = roomService;
		
		
	}
	*/
	
	
	@RequestMapping("/rooms")
	public String listRooms(Model model){
		model.addAttribute("rooms", roomService.findAll());
		System.out.println("Velicina: " + roomService.findAll().size());
		return "room/listrooms";
	}
	
	
	@RequestMapping(value = "/editRoom/{id}", method = RequestMethod.GET)
	public String editRoomView(@PathVariable Long id, Model model){
		Room room = roomService.getOne(id);
		model.addAttribute("room", room);
		return "room/editRoom";	
	}
	
	/*
	@RequestMapping(value = "/addRoom", method = RequestMethod.GET)
	public String addRoomView(){
		
		return "room/addRoom";	
	}
	
	@RequestMapping(value = "/addRoom", method = RequestMethod.POST)
	public @ResponseBody String addRoom(@ModelAttribute Room room) throws NullPointerException {

		roomService.add(room);
		return "Room added";
	}
	
	*/
	
	@RequestMapping(value = "/editRoom", method = RequestMethod.POST)
	public @ResponseBody String editRoom(@ModelAttribute Room room) throws NullPointerException {
		//Room room = roomService.getOne(id);
		roomService.update(room);
		System.out.println(room.toString());
		return "Room sucessfully edited";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody String saveRoom(@ModelAttribute("room") Room room){
		//roomService.add(room);
		return "Done";
	}
	
	@RequestMapping(value = "/deleteRoom/{id}", method = RequestMethod.GET)
	public String deleteRoomView(@PathVariable Long id, Model model){
		Room room = roomService.getOne(id);
		model.addAttribute("room", room);
		return "room/deleteRoom";	
	}
	
	
	@RequestMapping(value = "/deleteRoom", method = RequestMethod.POST)
	public @ResponseBody String deleteRoom(@ModelAttribute("room") Room room){
		roomService.delete(room.getId());
		return "Room succesfully deleted";
	}
	
	
	
}
