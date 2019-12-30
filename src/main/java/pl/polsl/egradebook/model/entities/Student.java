package pl.polsl.egradebook.model.entities;

import java.util.HashSet;
import java.util.Set;

public class Student extends User {
    private int classID;

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }
}
