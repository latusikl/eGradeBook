package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.polsl.egradebook.model.entities.*;
import pl.polsl.egradebook.model.repositories.*;

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

    public HeadMasterController(TeacherRepository teacherRepository, StudentRepository studentRepository, LessonRepository lessonRepository, UserRepository userRepository, GradeRepository gradeRepository, StudentsClassRepository studentsClassRepository, PresenceRepository presenceRepository, CaseRepository caseRepository, ClassesRepository classesRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.studentsClassRepository = studentsClassRepository;
        this.presenceRepository = presenceRepository;
        this.caseRepository = caseRepository;
        this.classesRepository = classesRepository;
    }


    @GetMapping()
    @PreAuthorize("hasAuthority('/headmaster')")
    public String getFirstView(Authentication authentication, Model model) {
        User logged = userRepository.findUserByUserName("Hellothere66");
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
        model.addAttribute("choosenClass", classesRepository.findById(selectedClass).get().getName());
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
        model.addAttribute("choosenClass", classesRepository.findById(selectedClass).get().getName());
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
    public String StudentOverview(Authentication authentication, Model model, @RequestParam("selectedStudent") int selectedStudent) {
        /*List<Student> students = studentRepository.findAllByStudentsClass_ClassID(selectedClass);
        if (students.size() == 0) {
            model.addAttribute("overviewComplete", false);
        }
        else {
            int absenceNumber = 0;
            for (Student s : students) {
                absenceNumber += presenceRepository.findByStudent_studentIDAndPresent(s.getStudentID(), false).size();
            }
            model.addAttribute("overviewComplete", true);
            model.addAttribute("absence", absenceNumber);
        }
        model.addAttribute("choosenClass", classesRepository.findById(selectedClass).get().getName());
        model.addAttribute("class", classesRepository.findAll());*/
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
}
