package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class MainController {
    @Autowired
    PasswordEncoder passwordEncoder;

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
