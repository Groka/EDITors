package com.editors.viberbot.mvc.controllers.user;

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
import com.editors.viberbot.database.entity.User;
import com.editors.viberbot.database.repository.RoomRepository;
import com.editors.viberbot.service.RoomService;
import com.editors.viberbot.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	@RequestMapping("/users")
	public String listUsers(Model model){
		model.addAttribute("users", userService.findAll());
		return "user/listUsers";
	}
	
	@RequestMapping(value = "/edituser/{id}", method = RequestMethod.GET)
	public String editUserView(@PathVariable Long id, Model model){
		User user = userService.getOne(id);
		model.addAttribute("user", user);
		return "user/editUser";	
	}
	
	@RequestMapping(value = "/edituser", method = RequestMethod.POST)
	public String editUser(@ModelAttribute User user) throws NullPointerException, NotFoundException {
		userService.update(user);
		return "redirect:/users";
	}
	
	@RequestMapping(value = "/adduser", method = RequestMethod.GET)
	public String adduserView(Model model){
		model.addAttribute("user", new User());
		return "user/addUser";	
	}
	
	@RequestMapping(value = "/adduser", method = RequestMethod.POST)
	public String addRoom (@RequestParam String viberId,
			@RequestParam String name,
			@RequestParam boolean subscribe) throws NullPointerException {
		
		User user = new User(viberId, name, subscribe);
		userService.addUser(user);
		return "redirect:/users";
	}
	
}
