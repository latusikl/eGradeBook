package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/user")
public class UserController {
	
	private final UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAuthority('/user/add')")
	public String showSignUpForm(User user) {
		return "user-add";
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAuthority('/user/add')")
	public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
		if (bindingResult.hasErrors()) {
			System.err.println("Binding user error addUser");
			return "user-add";
		}
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "user-show-all";
	}
	
	@GetMapping(path = "/delete/{userID}")
	@PreAuthorize("hasAuthority('/user/delete/{userID}')")
	public String deleteUser(@PathVariable("userID") int userID, Model model) {
		User userToDelete = userRepository.findById(userID).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + userID));
		userRepository.delete(userToDelete);
		model.addAttribute("users", userRepository.findAll());
		return "user-show-all";
	}
	
	@GetMapping(path = "/show/all")
	@PreAuthorize("hasAuthority('/user/show/all')")
	public String startUserManager(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "user-show-all";
	}
	
}
