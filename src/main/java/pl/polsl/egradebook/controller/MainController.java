package pl.polsl.egradebook.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {
    private final PasswordEncoder passwordEncoder;

    public MainController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String index() {
        return "login.html";
    }

    //redirect here after successful login, url temporary, better name pending
    @GetMapping("/authredir")
    public String authredir(Authentication authentication) {
        //get collection of all authorities, which contains allowed urls
        //admin
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("/user/show/all")))
            return "redirect:/user/show/all";
            //teacher
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("/teacher")))
            return "redirect:/teacher";
            //student
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("/student")))
            return "redirect:/student";
            //else
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("/parent")))
            return "redirect:/parent";
        else
            return "redirect:/";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/error/403")
    public String getAccessDeniedPage() {
        return "access-denied.html";
    }
}
