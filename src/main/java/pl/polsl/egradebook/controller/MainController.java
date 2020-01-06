package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import java.util.Collection;


@Controller
public class MainController {
	@Autowired
	PasswordEncoder passwordEncoder;
	@GetMapping("/")
	public String index(){
		return "login.html";
	}
	//redirect here after successful login, url temporary, better name pending
	@GetMapping("/authredir")
	public ModelAndView authredir()
	{
		//get collection of all authorities, which contains allowed urls
		Collection<? extends GrantedAuthority> authorities=SecurityContextHolder.getContext().getAuthentication().getAuthorities();
		//admin
		if(authorities.contains(new SimpleGrantedAuthority("/user/show/all")))
			return new ModelAndView("redirect:/user/show/all");
		//teacher
		else if(authorities.contains(new SimpleGrantedAuthority("/teacher")))
			return new ModelAndView("redirect:/teacher");
		//student
		else if(authorities.contains(new SimpleGrantedAuthority("/student")))
			return new ModelAndView("redirect:/student");
		//else
		else
			return new ModelAndView("redirect:/");
	}
	@GetMapping("/login")
	public String login(){
		return "login.html";
	}
	
	@GetMapping("/error/403")
	public String getAccessDeniedPage(){
		return "access-denied.html";
	}
}
