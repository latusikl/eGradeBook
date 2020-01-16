package pl.polsl.egradebook.model.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int messageID;

    @ManyToOne
    @JoinColumn(name = "caseid", referencedColumnName = "caseid")
    private Case referencedCase;

    @ManyToOne
    @JoinColumn(name = "senderid", referencedColumnName = "userID")
    private User sender;

    @NotNull
    private String content;

    public int getMessageID() {
        return messageID;
    }

    public void setMessageID(int messageID) {
        this.messageID = messageID;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Case getReferencedCase() {
        return referencedCase;
    }

    public void setReferencedCase(Case referencedCase) {
        this.referencedCase = referencedCase;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
