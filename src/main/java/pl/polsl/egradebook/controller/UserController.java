package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.UserRepository;

import javax.validation.Valid;

@Controller
public class UserController {
	//Allows to use JPA repository for user entity
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/add-user")
	public String showSignUpForm(User user) {
		return "add-user";
	}
	@PostMapping(path = "/add-user")
	public String addUser(@Valid User user, BindingResult bindingResult, Model model){
		if(bindingResult.hasErrors()){
			System.err.println("Binding user error addUser");
			return "add-user";
		}
		userRepository.save(user);
		model.addAttribute("users",userRepository.findAll());
		return "users";
	}
	
	@GetMapping(path="/delete-user/{userID}")
	public String deleteUser(@PathVariable("userID") int userID, Model model){
		User userToDelete = userRepository.findById(userID)
				.orElseThrow(() -> new IllegalArgumentException("Invalid id:" + userID));
		userRepository.delete(userToDelete);
		model.addAttribute("users",userRepository.findAll());
		return "users";
	}
	
	@GetMapping(path="/users")
	public String startUserManager(Model model){
		model.addAttribute("users",userRepository.findAll());
		return "users";
	}
	
	
}
