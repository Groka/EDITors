package com.editors.viberbot.database;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebController {

	RoomRepository repositoryy;
	
	@RequestMapping("/save")
	public String process(){
		repositoryy.save(new Room(1 , "Conference room"));
		repositoryy.save(new Room(2, "Meeting room"));

		return "Done";
	}
	
	
	@RequestMapping("/findall")
	public String findAll(){
		String result = "<html>";
		
		for(Room cust : repositoryy.findAll()){
			result += "<div>" + cust.toString() + "</div>";
		}
		
		return result + "</html>";
	}
	
	@RequestMapping("/findbyid")
	public String findById(@RequestParam("id") long id){
		String result = "";
		result = repositoryy.findOne(id).toString();
		return result;
	}
	
	@RequestMapping("/findbyname")
	public String fetchDataByLastName(@RequestParam("name") String name){
		String result = "<html>";
		
		for(Room cust: repositoryy.findByName(name)){
			result += "<div>" + cust.toString() + "</div>"; 
		}
		
		return result + "</html>";
	}
	
}
