package pl.polsl.egradebook.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "subjects")
public class Subject {

    @Id
    private int subjectID;

    @NotNull
    private int classID;

    @NotNull
    private int teacherID;

    @NotNull
    private String name;

    @NotNull
    private String day;

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(int teacherID) {
        this.teacherID = teacherID;
    }

    public int getSubjectID() {
        return subjectID;
    }

    public void setSubjectID(int subjectID) {
        this.subjectID = subjectID;
    }

    @Override
    public String toString() {
        return "Subject{" +
                "subjectID=" + subjectID +
                ", classID=" + classID +
                ", teacherID=" + teacherID +
                ", name='" + name + '\'' +
                ", day='" + day + '\'' +
                '}';
    }


}
