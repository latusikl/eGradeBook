package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.UserRepository;

import javax.validation.Valid;

/**
 Controller for user related operations via browser.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {
	
	private final UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public AdminController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@PostMapping("/user/change/password")
	@PreAuthorize("hasAuthority('/admin/user/change/password')")
	public String changeUserPassword(@RequestParam User user){
		if(userRepository.existsById(user.getUserID())){
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			userRepository.save(user);
		}
		return "user-management";
	}
	
	@PostMapping("/user/add")
	@PreAuthorize("hasAuthority('/user/add')")
	public String addUser(@Valid User user, BindingResult bindingResult, String newPassword, Model model) {
		if (bindingResult.hasErrors()) {
			System.err.println("Binding user error addUser");
			model.addAttribute("users", userRepository.findAll());
			
			return "user-management";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "user-management";
	}
	
	@GetMapping(path = "/user/delete/{userID}")
	@PreAuthorize("hasAuthority('/user/delete/{userID}')")
	public String deleteUser(@PathVariable("userID") int userID, Model model, User user, String newPassword) {
		
		User userToDelete = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + userID));
		
		userRepository.delete(userToDelete);
		
		model.addAttribute("users", userRepository.findAll());
		
		return "user-management";
	}
	
	@GetMapping(path = "/user/show/all")
	@PreAuthorize("hasAuthority('/user/show/all')")
	public String startUserManager(Model model, User user,String newPassword ,Authentication authentication) {
		
		model.addAttribute("users", userRepository.findAll());
		return "user-management";
	}
	
}
