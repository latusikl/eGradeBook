package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;


@Controller
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PresenceRepository presenceRepository;


    @GetMapping("/")
    public String viewGradesAndAttendance(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User loggedTeacher = userRepository.findUserByUserName(userName);
        Iterable<Student> classStudents = studentRepository.findAll();
        model.addAttribute("teacher", loggedTeacher);
        model.addAttribute("grades", gradeRepository.findAllByOrderByStudent_User_SurnameAsc());
        model.addAttribute("attendance", presenceRepository.findAll());
        model.addAttribute("students", studentRepository.findAll());

        return "teacher-view";
    }

}