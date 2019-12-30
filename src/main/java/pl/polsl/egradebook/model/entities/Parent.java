package pl.polsl.egradebook.model.entities;

public class Parent extends User {
    private int childID;

    public int getChildID() {
        return childID;
    }

    public void setChildID(int childID) {
        this.childID = childID;
    }
}
