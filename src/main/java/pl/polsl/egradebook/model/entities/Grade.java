package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int gradeID;

    @OneToOne
    @JoinColumn(name = "studentID", referencedColumnName = "studentID")
    private Student student;

    @OneToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    @NotNull
    private String date;

    @NotNull
    private String description;


    @Min(value = 1, message = "Mark should not be less than 1")
    @Max(value = 6, message = "Mark should not be greater than 6")
    private int mark;

    public int getGradeID() {
        return gradeID;
    }

    public void setGradeID(int gradeID) {
        this.gradeID = gradeID;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }
}
