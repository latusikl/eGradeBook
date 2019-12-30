package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	@Autowired
	PasswordEncoder passwordEncoder;
	@GetMapping("/")
	public String index(){
		return "login.html";
	}
	
	@GetMapping("/login")
	public String login(){
		return "login.html";
	}
	
	
	
}
