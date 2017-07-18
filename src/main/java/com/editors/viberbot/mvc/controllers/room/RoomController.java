package com.editors.viberbot.mvc.controllers.room;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.editors.viberbot.database.entity.Room;
import com.editors.viberbot.database.repository.RoomRepository;

@RestController
public class RoomController {

	@Autowired
	RoomRepository roomRepository;
	
	@RequestMapping("/save")
	public String process(){
		roomRepository.save(new Room(1 , "Conference room"));
		roomRepository.save(new Room(2, "Meeting room"));

		return "Done";
	}
	
	
	@RequestMapping("/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Room cust : roomRepository.findAll()){
			result += "<div>" + cust.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id){
		String result = "";
		result = roomRepository.findOne(id).toString();
		return result;
	}
	
	/*
	@RequestMapping("/findbyname")
	public String fetchDataByLastName(@RequestParam("name") String name){
		String result = "<html>";
		
		for(Room cust: roomRepository.findByName(name)){
			result += "<div>" + cust.toString() + "</div>"; 
		}
		
		return result + "</html>";
	}
	*/
	
}
