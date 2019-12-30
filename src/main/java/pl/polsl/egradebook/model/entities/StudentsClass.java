package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "classes")
public class StudentsClass {
    @Id
    private int classID;

    @NotNull
    private int size;

    @NotNull
    private String name;
}
