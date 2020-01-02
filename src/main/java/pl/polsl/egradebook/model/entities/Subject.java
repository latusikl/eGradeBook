package pl.polsl.egradebook.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int subjectID;

    @OneToOne
    @JoinColumn(name = "classID")
    private StudentsClass studentsClass;

    @OneToOne
    @JoinColumn(name = "teacherID")
    private User teacher;

    @NotNull
    private String name;

    @NotNull
    @Size(min = 3, max = 3)
    private String day;

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    public StudentsClass getStudentsClass() {
        return studentsClass;
    }

    public void setStudentsClass(StudentsClass studentsClass) {
        this.studentsClass = studentsClass;
    }

    public User getTeacher() {
        return teacher;
    }

    public void setTeacher(User teacher) {
        this.teacher = teacher;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
