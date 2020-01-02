package pl.polsl.egradebook.model.entities;

import javax.persistence.*;

@Entity
@Table(name = "parents")
public class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int parentID;

    @OneToOne
    @JoinColumn(name = "userID")
    private User parent;

    @OneToOne
    @JoinColumn(name = "childID",  referencedColumnName="userID")
    private User child;

}
