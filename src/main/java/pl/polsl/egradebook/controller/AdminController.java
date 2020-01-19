package pl.polsl.egradebook.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.polsl.egradebook.model.entities.*;
import pl.polsl.egradebook.model.repositories.*;
import pl.polsl.egradebook.model.security.RoleProperties;
import pl.polsl.egradebook.wrappers.DoubleStringWrapper;
import pl.polsl.egradebook.wrappers.TripleStringWrapper;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Controller for administrator management console.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;
    private final TeacherRepository teacherRepository;
    private final StudentsClassRepository studentsClassRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleProperties roleProperties;

    public AdminController(final UserRepository userRepository, final StudentRepository studentRepository, final ParentRepository parentRepository, final TeacherRepository teacherRepository, final StudentsClassRepository studentsClassRepository, final PasswordEncoder passwordEncoder, final RoleProperties roleProperties) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
        this.teacherRepository = teacherRepository;
        this.studentsClassRepository = studentsClassRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleProperties = roleProperties;
    }

    @GetMapping()
    @PreAuthorize("hasAuthority('/admin')")
    public String getMainView(Model model) {

        addRequiredModelAttributesForAdminRedirect(model);
        return "admin-view";
    }

    @PostMapping("/user/change/password")
    @PreAuthorize("hasAuthority('/admin/user/change/password')")
    public String changeUserPassword(@ModelAttribute @Valid DoubleStringWrapper doubleStringWrapper) {
        User userToChange = userRepository.findUserByUserName(doubleStringWrapper.getParameter1());

        if (userToChange != null) {
            userToChange.setPassword(passwordEncoder.encode(doubleStringWrapper.getParameter2()));
            userRepository.save(userToChange);
        }
        return "redirect:/admin";
    }

    @PostMapping("/user/add")
    @PreAuthorize("hasAuthority('/admin/user/add')")
    public String addUser(@ModelAttribute @Valid User user, Model model) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        model.addAttribute("addedUser", user);

        return addAttributesForRoleRedirect(user.getRoleType(), Integer.toString(user.getUserID()), user.getUserName());
    }


    @GetMapping("user/add/student/{userID}/{userName}")
    @PreAuthorize("hasAuthority('/admin/user/add/*')")
    public String addStudentToUser(@PathVariable("userID") String userID, @PathVariable("userName") String userName, Model model, TripleStringWrapper tripleStringWrapper) {
        model.addAttribute("userID", userID);
        model.addAttribute("userName", userName);
        model.addAttribute("tripleStringWrapper", tripleStringWrapper);
        model.addAttribute("parents", parentRepository.findAll());
        model.addAttribute("classes", studentsClassRepository.findAll());
        return "admin-add-student";
    }

    @PostMapping("user/add/student")
    @PreAuthorize("hasAuthority('/admin/user/add/*')")
    public String addStudentToUser(@ModelAttribute @Valid TripleStringWrapper tripleStringWrapper, Model model) {
        Optional<User> addedUser = userRepository.findById(Integer.parseInt(tripleStringWrapper.getParameter1()));
        Optional<Parent> parentToConnect = parentRepository.findById(Integer.parseInt(tripleStringWrapper.getParameter2()));
        Optional<StudentsClass> studentsClassToConnect = studentsClassRepository.findById(Integer.parseInt(tripleStringWrapper.getParameter3()));

        Student studentToAdd = new Student();
        studentToAdd.setUser(addedUser.get());
        studentToAdd.setParent(parentToConnect.get());
        studentToAdd.setStudentsClass(studentsClassToConnect.get());

        studentRepository.save(studentToAdd);

        addRequiredModelAttributesForAdminRedirect(model);
        return "redirect:/admin";
    }

    @GetMapping("user/add/parent/{userID}/{userName}")
    @PreAuthorize("hasAuthority('/admin/user/add/*')")
    public String addParentToUser(@PathVariable("userID") String userID, @PathVariable("userName") String userName, Model model){
        Optional<User> userToConnect = userRepository.findById(Integer.parseInt(userID));

        if(userToConnect.isPresent()){
            Parent parentToAdd = new Parent();
            parentToAdd.setUser(userToConnect.get());
            parentRepository.save(parentToAdd);
        }

        addRequiredModelAttributesForAdminRedirect(model);
        return "redirect:/admin";
    }

    @GetMapping("user/add/teacher/{userID}/{userName}")
    @PreAuthorize("hasAuthority('/admin/user/add/*')")
    public String addTeacherToUser(@PathVariable("userID") String userID, @PathVariable("userName") String userName, Model model){
        Optional<User> userToConnect = userRepository.findById(Integer.parseInt(userID));

        if(userToConnect.isPresent()){
            Teacher teacherToAdd = new Teacher();
            teacherToAdd.setUser(userToConnect.get());
            teacherRepository.save(teacherToAdd);
        }

        addRequiredModelAttributesForAdminRedirect(model);
        return "redirect:/admin";
    }

    @GetMapping(path = "/user/delete/{userID}")
    @PreAuthorize("hasAuthority('/admin/user/delete/{userID}')")
    public String deleteUser(@PathVariable("userID") int userID, Model model, User user, String newPassword) {

        Optional<User> userToDelete = userRepository.findById(userID);

        if (userToDelete.isPresent()) {
            userRepository.delete(userToDelete.get());
        }

        addRequiredModelAttributesForAdminRedirect(model);

        return "redirect:/admin";
    }

    @GetMapping("/show/students")
    public String showStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());

        return "admin-show-students";
    }

    @GetMapping("/show/parents")
    public String showParents(Model model) {
        model.addAttribute("parents", parentRepository.findAll());

        return "admin-show-parents";
    }

    @GetMapping("/show/teachers")
    public String showTeachers(Model model) {
        model.addAttribute("teachers", teacherRepository.findAll());

        return "admin-show-teachers";
    }


    private void addRequiredModelAttributesForAdminRedirect(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("roles", roleProperties.getRoleNames());
        model.addAttribute("user", new User());
        model.addAttribute("doubleStringWrapper", new DoubleStringWrapper());
    }

    private String addAttributesForRoleRedirect(String roleName, String userID, String userName) {
        String commonRedirectPart = "redirect:/admin/user/add/";
        return commonRedirectPart + roleName + "/" + userID + "/" + userName;
    }
}
