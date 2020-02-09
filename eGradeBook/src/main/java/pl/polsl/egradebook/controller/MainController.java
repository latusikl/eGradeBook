package pl.polsl.egradebook.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @GetMapping("/")
    public String index() {
        return "login.html";
    }

    /**
     * Simple managing of redirect, depending on user role.
     */
    @GetMapping("/authredir")
    public String redirectLoggedUser(Authentication authentication) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN_ROLE")))
            return "redirect:/admin";
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("TEACHER_ROLE")))
            return "redirect:/teacher";
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("STUDENT_ROLE")))
            return "redirect:/student";
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("PARENT_ROLE")))
            return "redirect:/parent";
        else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("HEADMASTER_ROLE")))
            return "redirect:/headmaster";
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
