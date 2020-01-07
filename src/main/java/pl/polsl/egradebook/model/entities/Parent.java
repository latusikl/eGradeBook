package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "parents")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int parentID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User parent;

    @Override
    public String toString() {
        return "Parent{" +
                "parentID=" + parentID +
                ", parent=" + parent +
                '}';
    }

    public int getParentID() {
        return parentID;
    }

    public void setParentID(int parentID) {
        this.parentID = parentID;
    }

    public User getParent() {
        return parent;
    }

    public void setParent(User parent) {
        this.parent = parent;
    }
}
