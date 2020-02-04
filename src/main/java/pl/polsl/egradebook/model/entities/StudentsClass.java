package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "classes")
public class StudentsClass {
    @Id
    private int classID;

    @NotNull
    private String name;

    @Override
    public String toString() {
        return "StudentsClass{" +
                "classID=" + classID +
                ", name='" + name + '\'' +
                '}';
    }

    public String getBasicClassInfo(){
        return name + " | " + classID;
    }

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
}
