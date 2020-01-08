package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polsl.egradebook.model.entities.Lesson;
import pl.polsl.egradebook.model.entities.Parent;
import pl.polsl.egradebook.model.entities.Presence;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.LessonRepository;
import pl.polsl.egradebook.model.repositories.ParentRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;

import java.util.List;

@Controller
@RequestMapping("/parent")
public class ParentController {

    private final StudentRepository studentRepository;

    private final ParentRepository parentRepository;

    private final LessonRepository lessonRepository;

    private final GradeRepository gradeRepository;

    private final PresenceRepository presenceRepository;

    public ParentController(StudentRepository studentRepository, ParentRepository parentRepository,
                            LessonRepository lessonRepository, GradeRepository gradeRepository,
                            PresenceRepository presenceRepository) {
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.lessonRepository = lessonRepository;
        this.gradeRepository = gradeRepository;
        this.presenceRepository = presenceRepository;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('/parent')")
    public String selectChild(Authentication authentication, Model model) {
        Parent loggedParent = getLoggedParent(authentication.getName());
        List<Student> children = studentRepository.findAllByParent_ParentID(loggedParent.getParentID());
        model.addAttribute("parent", loggedParent);
        if (children != null && children.size() > 1) {
            model.addAttribute("children", children);
        } else {
            Student child = children.get(0);
            model.addAttribute("child", children.get(0));
            parentViewAttributesWhenOnlyOneChild(model, loggedParent, child);
        }
        return "parent-view";
    }

    @PostMapping()
    @PreAuthorize("hasAuthority('/parent')")
    public String getParentView(@RequestParam int childID, Authentication authentication, Model model) {
        Parent loggedParent = getLoggedParent(authentication.getName());
        Student child = studentRepository.findById(childID);
        parentViewAttributesWhenOnlyOneChild(model, loggedParent, child);
        return "parent-view";
    }

    private void parentViewAttributesWhenOnlyOneChild(Model model, Parent loggedParent, Student child) {
        int loggedStudentClassID = child.getStudentsClass().getClassID();
        List<Lesson> lessons = lessonRepository.findAllByStudentsClass_ClassID(loggedStudentClassID);
        model.addAttribute("child", child);
        model.addAttribute("parent", loggedParent);
        model.addAttribute("grades", gradeRepository.
                findByStudent_studentID(child.getStudentID()));
        model.addAttribute("absences", presenceRepository.
                findByStudent_studentIDAndPresent(child.getStudentID(), false));
        model.addAttribute("lessons", lessons);
    }

    @GetMapping(path = "/attendance/{presenceID}")
    @PreAuthorize("hasAuthority('/parent/attendance/{presenceID}')")
    public String excuseAbsence(@PathVariable("presenceID") int presenceID) {
        Presence foundPresence = presenceRepository.findById(presenceID).orElseThrow(() ->
                new IllegalArgumentException("Invalid id:" + presenceID));
        foundPresence.setPresent(true);
        presenceRepository.save(foundPresence);
        return "redirect:/parent";
    }

    private Parent getLoggedParent(String userName) {
        return parentRepository.findByUser_UserName(userName);
    }
}
