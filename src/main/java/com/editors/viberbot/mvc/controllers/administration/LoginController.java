package com.editors.viberbot.mvc.controllers.administration;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
	
	
	@GetMapping("/login")
	public String showLogin(Model model){
		
		return "login";
	}
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(){
		return "redirect:/rooms";
	}
}
