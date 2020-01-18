package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polsl.egradebook.model.entities.*;
import pl.polsl.egradebook.model.repositories.*;
import pl.polsl.egradebook.model.util.UrlValidator;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/headmaster")
public class HeadMasterController {

    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    private final GradeRepository gradeRepository;

    private final StudentsClassRepository studentsClassRepository;

    private final PresenceRepository presenceRepository;

    private final CaseRepository caseRepository;

    private final ClassesRepository classesRepository;

    private final MessageRepository messageRepository;

    public HeadMasterController(TeacherRepository teacherRepository, StudentRepository studentRepository,
                                LessonRepository lessonRepository, UserRepository userRepository, GradeRepository gradeRepository,
                                StudentsClassRepository studentsClassRepository, PresenceRepository presenceRepository,
                                CaseRepository caseRepository, ClassesRepository classesRepository, MessageRepository messageRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.studentsClassRepository = studentsClassRepository;
        this.presenceRepository = presenceRepository;
        this.caseRepository = caseRepository;
        this.classesRepository = classesRepository;
        this.messageRepository = messageRepository;
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('/headmaster')")
    public String getFirstView(Authentication authentication, Model model) {
        User logged = userRepository.findUserByUserName(authentication.getName());
        model.addAttribute("headmaster", logged);
        return "headmaster-view";
    }


    @GetMapping("/grades")
    @PreAuthorize("hasAuthority('/headmaster/grades')")
    public String getAverageClassGradesRating(Authentication authentication, Model model) {
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-grades-rating-view";
    }

    @PostMapping("/grades")
    @PreAuthorize("hasAuthority('/headmaster/grades')")
    public String AverageClassGradesRatingPost(Authentication authentication, Model model, @RequestParam("selectedClass") int selectedClass) {
        List<Student> students = studentRepository.findAllByStudentsClass_ClassID(selectedClass);
        List<Grade> finalList, tmpList;
        finalList = gradeRepository.findByStudent_studentID(students.get(0).getStudentID());
        double gradesRating = 0;
        if (students.size() == 0) {
            model.addAttribute("ratingComplete", false);
        }
        else if(students.size() == 1) {
            for (Grade g : finalList)
                gradesRating += g.getMark();
            gradesRating = gradesRating / finalList.size();
            model.addAttribute("ratingComplete", true);
            model.addAttribute("rating", gradesRating);
        }
        else if(students.size() > 1){
            for (int i = 1; i < students.size(); i++) {
                tmpList = gradeRepository.findByStudent_studentID(students.get(i).getStudentID());
                finalList.addAll(tmpList);
            }
            for (Grade g : finalList)
                gradesRating += g.getMark();
            gradesRating = gradesRating / finalList.size();
            model.addAttribute("ratingComplete", true);
            model.addAttribute("rating", gradesRating);

        }
        model.addAttribute("chosenClass", classesRepository.findById(selectedClass).get().getName());
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-grades-rating-view";
    }

    @GetMapping("/absence")
    @PreAuthorize("hasAuthority('/headmaster/absence')")
    public String getStudentsClassAbsence(Authentication authentication, Model model) {
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-class-absence-view";
    }

    @PostMapping("/absence")
    @PreAuthorize("hasAuthority('/headmaster/absence')")
    public String StudentsClassAbsencePost(Authentication authentication, Model model, @RequestParam("selectedClass") int selectedClass) {
        List<Student> students = studentRepository.findAllByStudentsClass_ClassID(selectedClass);
        if (students.size() == 0) {
            model.addAttribute("absenceComplete", false);
        }
        else {
            int absenceNumber = 0;
            for (Student s : students) {
                absenceNumber += presenceRepository.findByStudent_studentIDAndPresent(s.getStudentID(), false).size();
            }
            model.addAttribute("absenceComplete", true);
            model.addAttribute("absence", absenceNumber);
        }
        model.addAttribute("chosenClass", classesRepository.findById(selectedClass).get().getName());
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-class-absence-view";
    }

    @GetMapping("/overview")
    @PreAuthorize("hasAuthority('/headmaster/overview')")
    public String getStudentOverview(Authentication authentication, Model model) {
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("overviewComplete", false);
        return "headmaster-student-overview-view";
    }

    @PostMapping("/overview")
    @PreAuthorize("hasAuthority('/headmaster/overview')")
    public String StudentOverview(Model model, @RequestParam("selectedStudent") int selectedStudent) {
        Student student = studentRepository.findById(selectedStudent);

        model.addAttribute("student", student);
        model.addAttribute("grades", gradeRepository.
                findByStudent_studentID(student.getStudentID()));
        model.addAttribute("absences", presenceRepository.
                findByStudent_studentIDAndPresent(student.getStudentID(), false));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("overviewComplete", true);
        return "headmaster-student-overview-view";
    }

    //case management
    @GetMapping("/cases")
    @PreAuthorize("hasAuthority('/headmaster/cases')")
    public String getCaseManagementSite(Authentication authentication, Model model) {
        User headmaster = userRepository.findUserByUserName(authentication.getName());
        model.addAttribute("cases", caseRepository.findByReceiver_UserIDOrSender_UserID(headmaster.getUserID(), headmaster.getUserID()));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newCase", new Case());
        this.addHomeUrl(model);
        return "case-management";
    }

    // add case
    @PostMapping("/cases/add")
    @PreAuthorize("hasAuthority('/headmaster/cases/add')")
    public String addCase(@ModelAttribute("newCase") Case newCase, @RequestParam("content") String msgContent, Model model, Authentication authentication) {
        User headmaster = userRepository.findUserByUserName(authentication.getName());
        newCase.setSender(headmaster);
        caseRepository.save(newCase);
        model.addAttribute("cases", caseRepository.findByReceiver_UserID(headmaster.getUserID()));
        Message newMessage = new Message();
        newMessage.setContent(msgContent);
        newMessage.setReferencedCase(newCase);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);

        this.addHomeUrl(model);
        return "redirect:/headmaster/cases/" + newCase.getCaseID() + "/";
    }

    //cases view for the headmaster
    @GetMapping(path = "/cases/{caseID}")
    @PreAuthorize("hasAuthority('/headmaster/cases/{caseID}')")
    public String selectedCase(@PathVariable("caseID") int caseID, Model model, Authentication authentication) {

        User headmaster = userRepository.findUserByUserName(authentication.getName());
        if (!UrlValidator.canAccessCase(caseID, headmaster.getUserID(), caseRepository))
            return "access-denied";
        Case foundCase = caseRepository.findByCaseID(caseID);
        model.addAttribute("case", foundCase);
        List<Message> messages = messageRepository.findAllByReferencedCase_CaseID(caseID);
        model.addAttribute("messages", messages);
        this.addHomeUrl(model);
        return "case-content-view";
    }

    //reply to case
    @PostMapping(path = "/cases/reply")
    @PreAuthorize("hasAuthority('/headmaster/cases/reply')")
    public String replyToCase(@RequestParam("caseID") int caseID, @RequestParam("content") String content, Authentication authentication) {
        Message newMessage = new Message();
        newMessage.setReferencedCase(caseRepository.findByCaseID(caseID));
        newMessage.setContent(content);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);
        return "redirect:/headmaster/cases/" + caseID + "/";
    }

    @GetMapping("/timetable")
    @PreAuthorize("hasAuthority('/headmaster/timetable')")
    public String getClassTimetable(Authentication authentication, Model model) {
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-class-timetable-view";
    }

    @PostMapping("/timetable")
    @PreAuthorize("hasAuthority('/headmaster/timetable')")
    public String classTimetablePost(Authentication authentication, Model model, @RequestParam("selectedClass") int selectedClass) {

        List<Lesson> lessons = lessonRepository.findAllByStudentsClass_ClassID(selectedClass);
        if (lessons.size() == 0) {
            model.addAttribute("timetableComplete", false);
        }
        else {
            model.addAttribute("timetableComplete", true);
            model.addAttribute("lessons", lessons);
        }
        model.addAttribute("chosenClass", classesRepository.findById(selectedClass).get().getName());
        model.addAttribute("class", classesRepository.findAll());
        return "headmaster-class-timetable-view";
    }

    private void addHomeUrl(Model model) {
        model.addAttribute("homeUrl", "/headmaster/");
    }

}
