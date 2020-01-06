package pl.polsl.egradebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.CaseRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;

import javax.validation.Valid;


@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private PresenceRepository presenceRepository;

    @Autowired
    private CaseRepository caseRepository;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping(path = "/cases/{caseID}")
    public String deleteUser(@PathVariable("caseID") int caseID, Model model) {
        Case foundCase = caseRepository.findById(caseID).orElseThrow(() ->
                new IllegalArgumentException("Invalid id:" + caseID));
        model.addAttribute("case", foundCase);
        return "case-content-view";
    }

    @GetMapping("/cases")
    public String getCaseManagementSite(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Student loggedStudent = studentRepository.findByUser_UserName(userName);
        model.addAttribute("cases", caseRepository.
                findByReceiver_UserID(loggedStudent.getUser().getUserID()));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newCase", new Case());
        return "case-management";
    }

    // not tested yet
    @PostMapping("/cases/add")
    public String addCase(@ModelAttribute("newCase") @Valid Case newCase, BindingResult bindingResult, Model model, Authentication authentication) {
        if(bindingResult.hasErrors()){
            System.err.println("Binding user error addCase");
            return "case-management";
        }
        String userName = authentication.getName();
        Student loggedStudent = studentRepository.findByUser_UserName(userName);
        newCase.setSender(loggedStudent.getUser());
        caseRepository.save(newCase);
        model.addAttribute("cases", caseRepository.
                findByReceiver_UserID(loggedStudent.getUser().getUserID()));
        return "case-management";
    }

}
