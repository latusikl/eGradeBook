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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.polsl.egradebook.model.entities.Case;
import pl.polsl.egradebook.model.entities.Grade;
import pl.polsl.egradebook.model.entities.Lesson;
import pl.polsl.egradebook.model.entities.Message;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.CaseRepository;
import pl.polsl.egradebook.model.repositories.LessonRepository;
import pl.polsl.egradebook.model.repositories.MessageRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;
import pl.polsl.egradebook.model.util.UrlValidator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.Valid;
import java.text.DecimalFormat;
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

    private final MessageRepository messageRepository;

    public StudentController(StudentRepository studentRepository, GradeRepository gradeRepository, PresenceRepository presenceRepository, CaseRepository caseRepository, UserRepository userRepository, LessonRepository lessonRepository, MessageRepository messageRepository) {
        this.studentRepository = studentRepository;
        this.gradeRepository = gradeRepository;
        this.presenceRepository = presenceRepository;
        this.caseRepository = caseRepository;
        this.userRepository = userRepository;
        this.lessonRepository = lessonRepository;
        this.messageRepository = messageRepository;
    }

    @GetMapping()
	@PreAuthorize("hasAuthority('/student')")
	public String getStudentView(Authentication authentication, Model model) {
		Student loggedStudent = this.getStudentByUserName(authentication.getName());
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

	@GetMapping(path = "/grades")
	@PreAuthorize("hasAuthority('/student/grades')")
	public String getStudentGradesView(Authentication authentication, Model model) {
		String userName = authentication.getName();
		Student loggedStudent = studentRepository.findByUser_UserName(userName);
		model.addAttribute("student", loggedStudent);
		model.addAttribute("grades", gradeRepository.
				findByStudent_studentID(loggedStudent.getStudentID()));
		return "student-grades-view";
	}

	@GetMapping(path = "/attendance")
	@PreAuthorize("hasAuthority('/student/attendance')")
    public String getStudentAttendanceView(Authentication authentication, Model model) {
        String userName = authentication.getName();
        Student loggedStudent = studentRepository.findByUser_UserName(userName);
        int loggedStudentClassID = loggedStudent.getStudentsClass().getClassID();
        model.addAttribute("student", loggedStudent);
        model.addAttribute("attendance", presenceRepository.
                findByStudent_studentID(loggedStudent.getStudentID()));
        return "student-attendance-view";
    }

    //case details view
    @GetMapping(path = "/cases/{caseID}")
    @PreAuthorize("hasAuthority('/student/cases/{caseID}')")
    public String selectedCase(@PathVariable("caseID") int caseID, Model model, Authentication authentication) {

        Student loggedStudent = this.getStudentByUserName(authentication.getName());
        if (!UrlValidator.canAccessCase(caseID, loggedStudent.getUser().getUserID(), caseRepository))
            return "access-denied";
        Case foundCase = caseRepository.findByCaseID(caseID);
        model.addAttribute("case", foundCase);
        List<Message> messages = messageRepository.findAllByReferencedCase_CaseID(caseID);
        model.addAttribute("messages", messages);
        this.addHomeUrl(model);
        return "case-content-view";
    }

    //case management
    @GetMapping("/cases")
    @PreAuthorize("hasAuthority('/student/cases')")
    public String getCaseManagementSite(Authentication authentication, Model model) {
        User loggedStudent = this.getStudentByUserName(authentication.getName()).getUser();
        model.addAttribute("cases", caseRepository.findByReceiver_UserIDOrSender_UserID(loggedStudent.getUserID(), loggedStudent.getUserID()));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newCase", new Case());
        this.addHomeUrl(model);
        return "case-management";
    }

    // add case view
    @PostMapping("/cases/add")
    @PreAuthorize("hasAuthority('/student/cases/add')")
    public String addCase(@ModelAttribute("newCase") Case newCase, @RequestParam("content") String msgContent, Model model, Authentication authentication) {

        User loggedStudent = this.getStudentByUserName(authentication.getName()).getUser();

        newCase.setSender(loggedStudent);
        caseRepository.save(newCase);
        model.addAttribute("cases", caseRepository.findByReceiver_UserID(loggedStudent.getUserID()));
        Message newMessage = new Message();
        newMessage.setContent(msgContent);
        newMessage.setReferencedCase(newCase);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);

        this.addHomeUrl(model);
        return "redirect:/student/cases/" + newCase.getCaseID() + "/";
    }

    //reply to case
    @PostMapping(path = "/cases/reply")
    @PreAuthorize("hasAuthority('/student/cases/reply')")
    public String replyToCase(@RequestParam("caseID") int caseID, @RequestParam("content") String content, Authentication authentication) {
        Message newMessage = new Message();
        newMessage.setReferencedCase(caseRepository.findByCaseID(caseID));
        newMessage.setContent(content);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);
        return "redirect:/student/cases/" + caseID + "/";
    }

    @GetMapping(path = "/statistics")
    @PreAuthorize("hasAuthority('/student/statistics')")
    @ResponseBody
    public String getStatistics(Authentication authentication) {
        Student loggedStudent = this.getStudentByUserName(authentication.getName());
        String returnString = "Grades average: ";
        double average = 0;
        List<Grade> gradeList = gradeRepository.findByStudent_studentID(loggedStudent.getStudentID());
        for (var grade : gradeList) {
            average += grade.getMark();
        }
        if (average > 0) {
            average = average / gradeList.size();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        returnString += df.format(average);
        returnString += "\n";
        returnString += "Number of absences: ";
        int absenceNumber = presenceRepository.findByStudent_studentIDAndPresent(loggedStudent.getStudentID(), false).size();
        returnString += Integer.toString(absenceNumber);
        return returnString;
    }

    private Student getStudentByUserName(String userName) {
        return studentRepository.findByUser_UserName(userName);
    }

    private void addHomeUrl(Model model) {
        model.addAttribute("homeUrl", "/student/");
    }
}
