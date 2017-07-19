package com.editors.viberbot.mvc.controllers.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String showLogin(Model model){
		
		return "login";
	}
	
	@PostMapping("/login")
	public String check(@RequestParam(required = true, value = "email") String email,
						@RequestParam(required = true, value = "password") String password){
		if(email == "admin@mail.com" && password == "password")
			return "index";
		
		//return showLogin();
		//return "room/rooms";
		return "";
	}
}
