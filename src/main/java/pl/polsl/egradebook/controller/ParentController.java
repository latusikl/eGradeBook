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
import org.springframework.web.bind.annotation.ResponseBody;
import pl.polsl.egradebook.model.entities.Case;
import pl.polsl.egradebook.model.entities.Grade;
import pl.polsl.egradebook.model.entities.Lesson;
import pl.polsl.egradebook.model.entities.Message;
import pl.polsl.egradebook.model.entities.Parent;
import pl.polsl.egradebook.model.entities.Presence;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.exceptions.InputException;
import pl.polsl.egradebook.model.repositories.CaseRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.LessonRepository;
import pl.polsl.egradebook.model.repositories.MessageRepository;
import pl.polsl.egradebook.model.repositories.ParentRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;
import pl.polsl.egradebook.model.util.UrlValidator;

import java.text.DecimalFormat;
import java.util.List;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final StudentRepository studentRepository;

    private final ParentRepository parentRepository;

    private final LessonRepository lessonRepository;

    private final GradeRepository gradeRepository;

    private final PresenceRepository presenceRepository;

    private final CaseRepository caseRepository;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private Student child;

    public ParentController(StudentRepository studentRepository, ParentRepository parentRepository,
                            LessonRepository lessonRepository, GradeRepository gradeRepository,
                            PresenceRepository presenceRepository, CaseRepository caseRepository, MessageRepository messageRepository, UserRepository userRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.lessonRepository = lessonRepository;
        this.gradeRepository = gradeRepository;
        this.presenceRepository = presenceRepository;
        this.caseRepository = caseRepository;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('/parent')")
    public String selectChild(Authentication authentication, Model model) {

        Parent loggedParent = this.getParentByUserName(authentication.getName());
        List<Student> children = studentRepository.findAllByParent_ParentID(loggedParent.getParentID());
        model.addAttribute("parent", loggedParent);
        if (children != null && children.size() > 1) {
            model.addAttribute("children", children);
        } else {
            Student child = children.get(0);
            model.addAttribute("child", children.get(0));
            getChildInformations(model, loggedParent, child);
        }
        return "parent-view";
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('/parent')")
    public String getParentView(@RequestParam int childID, Authentication authentication, Model model) {
        Parent loggedParent = getParentByUserName(authentication.getName());
        Student child = studentRepository.findById(childID);
        getChildInformations(model, loggedParent, child);
        return "parent-view";
    }

    private void getChildInformations(Model model, Parent loggedParent, Student child) {
        int loggedStudentClassID = child.getStudentsClass().getClassID();
        List<Lesson> lessons = lessonRepository.findAllByStudentsClass_ClassID(loggedStudentClassID);
        model.addAttribute("child", child);
        model.addAttribute("parent", loggedParent);
        model.addAttribute("grades", gradeRepository.
                findByStudent_studentID(child.getStudentID()));
        model.addAttribute("absences", presenceRepository.
                findByStudent_studentIDAndPresent(child.getStudentID(), false));
        model.addAttribute("lessons", lessons);
        this.child = child;
    }

    @GetMapping(path = "/attendance/{presenceID}")
    @PreAuthorize("hasAuthority('/parent/attendance/{presenceID}')")
    public String excuseAbsence(@PathVariable("presenceID") int presenceID) {
        try {
            Presence foundPresence = presenceRepository.findById(presenceID).orElseThrow(() ->
                    new InputException("Invalid id:" + presenceID));
            foundPresence.setPresent(true);
            presenceRepository.save(foundPresence);
        } catch (InputException e) {
            System.err.println(e);
        }
        return "redirect:/parent/attendance";
    }

    @GetMapping(path = "/attendance")
    @PreAuthorize("hasAuthority('/parent/attendance')")
    public String getAttendanceView(Authentication authentication, Model model) {
        Parent loggedParent = getParentByUserName(authentication.getName());
        if (this.child == null) {
            return "redirect:/parent";
        }
        getChildInformations(model, loggedParent, this.child);
        return "parent-attendance-view";
    }

    @GetMapping(path = "/grades")
    @PreAuthorize("hasAuthority('/parent/grades')")
    public String getGradesView(Authentication authentication, Model model) {
        Parent loggedParent = getParentByUserName(authentication.getName());
        if (this.child == null) {
            return "redirect:/parent";
        }
        getChildInformations(model, loggedParent, this.child);
        return "parent-grades-view";
    }

    //case details view
    @GetMapping(path = "/cases/{caseID}")
    @PreAuthorize("hasAuthority('/parent/cases/{caseID}')")
    public String selectedCase(@PathVariable("caseID") int caseID, Model model, Authentication authentication) {

        Parent loggedParent = this.getParentByUserName(authentication.getName());
        if (!UrlValidator.canAccessCase(caseID, loggedParent.getUser().getUserID(), caseRepository))
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
    @PreAuthorize("hasAuthority('/parent/cases')")
    public String getCaseManagementSite(Authentication authentication, Model model) {
        User loggedParent = this.getParentByUserName(authentication.getName()).getUser();
        model.addAttribute("cases", caseRepository.findByReceiver_UserIDOrSender_UserID(loggedParent.getUserID(), loggedParent.getUserID()));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newCase", new Case());
        this.addHomeUrl(model);
        return "case-management";
    }

    // add case view
    @PostMapping("/cases/add")
    @PreAuthorize("hasAuthority('/parent/cases/add')")
    public String addCase(@ModelAttribute("newCase") Case newCase, @RequestParam("content") String msgContent, Model model, Authentication authentication) {

        User loggedParent = this.getParentByUserName(authentication.getName()).getUser();
        newCase.setSender(loggedParent);

        caseRepository.save(newCase);
        model.addAttribute("cases", caseRepository.findByReceiver_UserID(loggedParent.getUserID()));
        Message newMessage = new Message();
        newMessage.setContent(msgContent);
        newMessage.setReferencedCase(newCase);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);

        this.addHomeUrl(model);
        return "redirect:/parent/cases/" + newCase.getCaseID() + "/";
    }

    //reply to case
    @PostMapping(path = "/cases/reply")
    @PreAuthorize("hasAuthority('/parent/cases/reply')")
    public String replyToCase(@RequestParam("caseID") int caseID, @RequestParam("content") String content, Authentication authentication) {
        Message newMessage = new Message();
        newMessage.setReferencedCase(caseRepository.findByCaseID(caseID));
        newMessage.setContent(content);
        newMessage.setSender(userRepository.findUserByUserName(authentication.getName()));
        messageRepository.save(newMessage);
        return "redirect:/parent/cases/" + caseID + "/";
    }

    @GetMapping(path = "/statistics")
    @PreAuthorize("hasAuthority('/parent/statistics')")
    @ResponseBody
    public String getStatistics(Authentication authentication) {
        if (this.child == null) {
            return "Child not selected";
        }
        String returnString = "Grades average: ";
        double average = 0;
        List<Grade> gradeList = gradeRepository.findByStudent_studentID(this.child.getStudentID());
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
        int absenceNumber = presenceRepository.findByStudent_studentIDAndPresent(this.child.getStudentID(), false).size();
        returnString += Integer.toString(absenceNumber);
        return returnString;
    }

    private void addHomeUrl(Model model) {
        model.addAttribute("homeUrl", "/parent/");
    }

    private Parent getParentByUserName(String userName) {
        return parentRepository.findByUser_UserName(userName);
    }
}
