package pl.polsl.egradebook.model.entities;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "cases")
public class Case {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int caseID;

    @ManyToOne
    @JoinColumn(name = "senderID", referencedColumnName="userID")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiverID", referencedColumnName="userID")
    private User receiver;

    @NotNull
    private String topic;

    @NotNull
    private String content;

    @Override
    public String toString() {
        return "Case{" +
                "caseID=" + caseID +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
