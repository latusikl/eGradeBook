package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.polsl.egradebook.model.entities.Case;
import pl.polsl.egradebook.model.entities.Lesson;
import pl.polsl.egradebook.model.entities.Parent;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.repositories.ParentRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    public ParentController(StudentRepository studentRepository, ParentRepository parentRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('/parent')")
    public String getStudentView(Authentication authentication, Model model) {
        String loggedParentName = authentication.getName();
        Parent loggedParent = parentRepository.findByUserName(loggedParentName);
        List<Student> children = studentRepository.findAllByParentParentID(loggedParent.getParentID());

        model.addAttribute("children", children);

        return "parent-view";
    }
}
