package pl.polsl.egradebook.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
	
	@GetMapping("/")
	public String showLoginForm(){
		return "index";
	}
	
	@PostMapping("/")
	public String processLogin(ModelAndView modelAndView, @RequestParam (value = "username") String login, @RequestParam (value="password") String password){
		System.out.println(login + "  "+ password);
		return "index";
	}
	
	
	
}
