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
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.repositories.CaseRepository;
import pl.polsl.egradebook.model.repositories.LessonRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {
	
	private final StudentRepository studentRepository;
	
	private final GradeRepository gradeRepository;
	
	private final PresenceRepository presenceRepository;
	
	private final CaseRepository caseRepository;
	
	private final UserRepository userRepository;
	
	private final LessonRepository lessonRepository;
	
	public StudentController(StudentRepository studentRepository, GradeRepository gradeRepository, PresenceRepository presenceRepository, CaseRepository caseRepository, UserRepository userRepository, LessonRepository lessonRepository) {
		this.studentRepository = studentRepository;
		this.gradeRepository = gradeRepository;
		this.presenceRepository = presenceRepository;
		this.caseRepository = caseRepository;
		this.userRepository = userRepository;
		this.lessonRepository = lessonRepository;
	}
	
	@GetMapping()
	@PreAuthorize("hasAuthority('/student')")
	public String getStudentView(Authentication authentication, Model model) {
		String userName = authentication.getName();
		Student loggedStudent = studentRepository.findByUser_UserName(userName);
		int loggedStudentClassID = loggedStudent.getStudentsClass().getClassID();
		List<Lesson> lessons = lessonRepository.findAllByStudentsClass_ClassID(loggedStudentClassID);
		model.addAttribute("student", loggedStudent);
		model.addAttribute("grades", gradeRepository.
				findByStudent_studentID(loggedStudent.getStudentID()));
		model.addAttribute("attendance", presenceRepository.
				findByStudent_studentID(loggedStudent.getStudentID()));
		model.addAttribute("lessons", lessons);
		return "student-view";
	}
	
	@GetMapping(path = "/cases/{caseID}")
	@PreAuthorize("hasAuthority('/student/cases/{caseID}')")
	public String deleteUser(@PathVariable("caseID") int caseID, Model model) {
		Case foundCase = caseRepository.findById(caseID).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + caseID));
		model.addAttribute("case", foundCase);
		return "case-content-view";
	}
	
	@GetMapping("/cases")
	@PreAuthorize("hasAuthority('/student/cases')")
	public String getCaseManagementSite(Authentication authentication, Model model) {
		String userName = authentication.getName();
		Student loggedStudent = studentRepository.findByUser_UserName(userName);
		model.addAttribute("cases", caseRepository.
				findByReceiver_UserID(loggedStudent.getUser().getUserID()));
		model.addAttribute("users", userRepository.findAll());
		model.addAttribute("newCase", new Case());
		return "case-management";
	}

	@PostMapping("/cases/add")
	@PreAuthorize("hasAuthority('/student/cases/add')")
	public String addCase(@ModelAttribute("newCase") @Valid Case newCase, BindingResult bindingResult, Model model, Authentication authentication) {
		if (bindingResult.hasErrors()) {
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
