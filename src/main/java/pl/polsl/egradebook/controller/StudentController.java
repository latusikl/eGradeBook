package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;



@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @GetMapping("/")
    public String viewGradesAndAttendance(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Student loggedStudent = studentRepository.findByUser_UserName(userName);
        model.addAttribute("student", loggedStudent);
        model.addAttribute("grades", gradeRepository.
                findByStudent_studentID(loggedStudent.getStudentID()));
        model.addAttribute("attendance", presenceRepository.
                findByStudent_studentID(loggedStudent.getStudentID()));
        return "student-view";
    }

}
