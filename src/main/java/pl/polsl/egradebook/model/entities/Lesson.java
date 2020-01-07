package pl.polsl.egradebook.model.entities;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "lessons")
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int lessonID;

    @OneToOne
    @JoinColumn(name = "classID")
    private StudentsClass studentsClass;

    @OneToOne
    @JoinColumn(name = "subjectID")
    private Subject Subject;

    @ManyToOne
    @JoinColumn(name = "teacherID", referencedColumnName = "userID")
    private Teacher teacher;

    @OneToOne
    @JoinColumn(name = "dayID")
    private Day day;

    private int number;

    public int getLessonID() {
        return lessonID;
    }

    public void setLessonID(int lessonID) {
        this.lessonID = lessonID;
    }

    public pl.polsl.egradebook.model.entities.Subject getSubject() {
        return Subject;
    }

    public void setSubject(pl.polsl.egradebook.model.entities.Subject subject) {
        Subject = subject;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public StudentsClass getStudentsClass() {
        return studentsClass;
    }

    public void setStudentsClass(StudentsClass studentsClass) {
        this.studentsClass = studentsClass;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "lessonID=" + lessonID +
                ", studentsClass=" + studentsClass +
                ", Subject=" + Subject +
                ", teacher=" + teacher +
                ", day=" + day +
                ", number=" + number +
                '}';
    }
}
