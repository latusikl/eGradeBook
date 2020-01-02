package pl.polsl.egradebook.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "attendance")
public class Presence {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int presenceID;

    @ManyToOne
    @JoinColumn(name = "studentID")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "subjectID")
    private Subject subject;

    @NotNull
    private String date;

    private boolean present;

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public int getPresenceID() {
        return presenceID;
    }

    public void setPresenceID(int presenceID) {
        this.presenceID = presenceID;
    }
}
