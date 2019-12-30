package pl.polsl.egradebook.model.entities;

import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

public class Timetable {

    @OneToMany
    @JoinColumn(name="subjectID")
    private Set<Subject> subjectSet = new HashSet<Subject>();


    public Set<Subject> getSubjectSet() {
        return subjectSet;
    }

    public void setSubjectSet(Set<Subject> subjectSet) {
        this.subjectSet = subjectSet;
    }

    @Override
    public String toString() {
        return "Timetable{" +
                ", subjectSet=" + subjectSet +
                '}';
    }
}
