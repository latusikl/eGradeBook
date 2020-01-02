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

    private int size;

    @NotNull
    private String name;

    @Override
    public String toString() {
        return "StudentsClass{" +
                "classID=" + classID +
                ", size=" + size +
                ", name='" + name + '\'' +
                '}';
    }
}
