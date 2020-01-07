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
import pl.polsl.egradebook.model.entities.Case;
import pl.polsl.egradebook.model.entities.Presence;
import pl.polsl.egradebook.model.entities.Student;
import pl.polsl.egradebook.model.entities.StudentsClass;
import pl.polsl.egradebook.model.entities.Teacher;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.CaseRepository;
import pl.polsl.egradebook.model.repositories.GradeRepository;
import pl.polsl.egradebook.model.repositories.LessonRepository;
import pl.polsl.egradebook.model.repositories.PresenceRepository;
import pl.polsl.egradebook.model.repositories.StudentRepository;
import pl.polsl.egradebook.model.repositories.StudentsClassRepository;
import pl.polsl.egradebook.model.repositories.TeacherRepository;
import pl.polsl.egradebook.model.repositories.UserRepository;
import pl.polsl.egradebook.model.util.StringValidator;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/teacher")
public class TeacherController {


    private final TeacherRepository teacherRepository;

    private final StudentRepository studentRepository;

    private final LessonRepository lessonRepository;

    private final UserRepository userRepository;

    private final GradeRepository gradeRepository;

    private final StudentsClassRepository studentsClassRepository;

    private final PresenceRepository presenceRepository;

    private final CaseRepository caseRepository;

    private final String homeUrl;
    
    public TeacherController(TeacherRepository teacherRepository, StudentRepository studentRepository, LessonRepository lessonRepository, UserRepository userRepository, GradeRepository gradeRepository, StudentsClassRepository studentsClassRepository, PresenceRepository presenceRepository, CaseRepository caseRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
        this.lessonRepository = lessonRepository;
        this.userRepository = userRepository;
        this.gradeRepository = gradeRepository;
        this.studentsClassRepository = studentsClassRepository;
        this.presenceRepository = presenceRepository;
        this.caseRepository = caseRepository;
        this.homeUrl = "/teacher/";
    }
    
    @GetMapping()
    @PreAuthorize("hasAuthority('/teacher')")
    public String viewGradesAndAttendance(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User loggedTeacher = userRepository.findUserByUserName(userName);
        Iterable<Student> classStudents = studentRepository.findAll();
        model.addAttribute("teacher", loggedTeacher);
        model.addAttribute("grades", gradeRepository.findAllByOrderByStudent_User_SurnameAsc());
        model.addAttribute("attendance", presenceRepository.findAllByLesson_Teacher_User_UserID_OrderByDateDesc(loggedTeacher.getUserID()));
        model.addAttribute("students", studentRepository.findAll());
        model.addAttribute("homeUrl",homeUrl);
        return "teacher-view";
    }

    //cases view for the teacher, coppied from student
    @GetMapping(path = "/cases/{caseID}")
    @PreAuthorize("hasAuthority('/teacher/cases/{caseID}')")
    public String selectedCase(@PathVariable("caseID") int caseID, Model model) {
        Case foundCase = caseRepository.findById(caseID).orElseThrow(() -> new IllegalArgumentException("Invalid id:" + caseID));
        model.addAttribute("case", foundCase);
        model.addAttribute("homeUrl",homeUrl);
        return "case-content-view";
    }

    //submits changed attendances for selected class
    //returns attendance-management.html with model attribute submitSuccessful set to true to show the message on page
    @PostMapping("/attendance/submit")
    @PreAuthorize("hasAuthority('/teacher/attendance/submit')")
    public String submitAttendance(ListWrapper listWrapper, Model model,Authentication authentication)
    {
        List<Presence> studentAttendance=listWrapper.getList();
        System.out.println(studentAttendance);
        for(Presence p: studentAttendance)
        {
            presenceRepository.save(p);
        }
        String userName=authentication.getName();
        Teacher loggedTeacher = teacherRepository.findByUser_UserName(userName);
        int teacherID=loggedTeacher.getTeacherID();
        model.addAttribute("homeUrl",homeUrl);
        model.addAttribute("submitSuccessful",true);
        model.addAttribute("todayDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("lessons",lessonRepository.findAllByTeacher_TeacherID(teacherID));
        return "attendance-management";
    }

    //shows dropdown box with classes and enables class selection for changing attendances
    //sends selected lesson with post to the same page, along with the selected date from date picker
    //returns attendance-management.html with model attribute newInstance set to true to suppress all messages about empty date and list of students
    @GetMapping("/attendance")
    @PreAuthorize("hasAuthority('/teacher/attendance')")
    public String checkAttendance(Authentication authentication, Model model)
    {
        String userName=authentication.getName();
        Teacher loggedTeacher = teacherRepository.findByUser_UserName(userName);
        int teacherID=loggedTeacher.getTeacherID();
        model.addAttribute("homeUrl",homeUrl);
        model.addAttribute("newInstance",true);
        model.addAttribute("todayDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("lessons",lessonRepository.findAllByTeacher_TeacherID(teacherID));
        return "attendance-management";
    }

    //shows all students in class for easy setting of attendance in selected lesson
    //gets selected lesson with post from the dropdown list along with selected date
    //sets model attributes selectedDate and studentsFound to false to warn about incorrect or nonsensical input
    //submits the data if correct using the second form in attendance-management.html to /teacher/attendance/submit
    @PostMapping("/attendance")
    @PreAuthorize("hasAuthority('/teacher/attendance')")
    public String checkAttendancePost(Authentication authentication, Model model, @RequestParam("selectedLesson") int selectedLesson, @RequestParam("selectedDate") String selectedDate)
    {
        String userName=authentication.getName();
        Teacher loggedTeacher = teacherRepository.findByUser_UserName(userName);
        int teacherID=loggedTeacher.getTeacherID();
        model.addAttribute("homeUrl",homeUrl);
        model.addAttribute("todayDate",new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("lessons",lessonRepository.findAllByTeacher_TeacherID(teacherID));


        if(!StringValidator.validateDate(selectedDate))
            model.addAttribute("selectedDate",false);
        else
        {
            model.addAttribute("selectedDate", selectedDate);
            StudentsClass selectedClass = lessonRepository.findByLessonID(selectedLesson).getStudentsClass();
            List<Student> students = studentRepository.findAllByStudentsClass_ClassID(selectedClass.getClassID());
            if(students==null||students.size()==0)
                model.addAttribute("studentsFound", false);
            else {
                List<Presence> newPresences = presenceRepository.findAllByDateAndLesson_LessonID(selectedDate, selectedLesson);
                if (newPresences.size() == 0) {
                    newPresences = new ArrayList<>();
                    for (Student s : students) {
                        Presence p = new Presence();
                        p.setDate(selectedDate);
                        p.setLesson(lessonRepository.findByLessonID(selectedLesson));
                        p.setStudent(s);
                        p.setPresent(false);
                        presenceRepository.save(p);
                        newPresences.add(p);
                    }
                }
                ListWrapper listWrapper = new ListWrapper();
                listWrapper.setList(newPresences);
                model.addAttribute("studentsAttendance", listWrapper);
                System.out.print(newPresences);
            }
        }
        return "attendance-management";
    }
    //case management view copied from student
    @GetMapping("/cases")
    @PreAuthorize("hasAuthority('/teacher/cases')")
    public String getCaseManagementSite(Authentication authentication, Model model) {
        String userName = authentication.getName();
        User loggedTeacher = userRepository.findUserByUserName(userName);
        model.addAttribute("cases", caseRepository.
                findByReceiver_UserID(loggedTeacher.getUserID()));
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("newCase", new Case());
        model.addAttribute("homeUrl",homeUrl);
        return "case-management";
    }

    // add case view copied from student
    @PostMapping("/cases/add")
    @PreAuthorize("hasAuthority('/teacher/cases/add')")
    public String addCase(@ModelAttribute("newCase") @Valid Case newCase, BindingResult bindingResult, Model model, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            System.err.println("Binding user error addCase");
            return "case-management";
        }

        model.addAttribute("homeUrl",homeUrl);
        String userName = authentication.getName();
        Student loggedStudent = studentRepository.findByUser_UserName(userName);
        newCase.setSender(loggedStudent.getUser());
        caseRepository.save(newCase);
        model.addAttribute("cases", caseRepository.findByReceiver_UserID(loggedStudent.getUser().getUserID()));

        return "case-management";
    }

    //list wrapper used to wrap the presence list and send it via post
    class ListWrapper
    {
        private List<Presence> list;

        public ListWrapper()
        {
            this.list=new ArrayList<>();
        }

        public void setList(List<Presence> list) {
            this.list = list;
        }

        public void addPresence(Presence presence)
        {
            this.list.add(presence);
        }
        public List<Presence> getList() {
            return list;
        }
    }

}