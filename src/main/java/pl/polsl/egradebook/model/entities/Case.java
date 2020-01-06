package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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

    public int getCaseID() {
        return caseID;
    }

    public void setCaseID(int caseID) {
        this.caseID = caseID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

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
