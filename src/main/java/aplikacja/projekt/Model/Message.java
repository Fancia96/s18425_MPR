package aplikacja.projekt.Model;

import aplikacja.projekt.ProjektApplication;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String msg;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "listOfMessagesFrom", "listOfMessagesTo", "listOfFriends", "listOfFriendsTwo"  }, allowSetters=true)
    private Person personFrom;

    @ManyToOne
    @JsonIgnoreProperties(value = { "listOfMessagesFrom", "listOfMessagesTo", "listOfFriends", "listOfFriendsTwo"  }, allowSetters=true)
    private Person personTo;

    public Message() {}

    public Message(String msg){
        this.msg = msg;
        this.date = new Date();
    }

    public Message(Person personFrom, Person personTo, String msg){
        this.personFrom = personFrom;
        this.personTo = personTo;
        this.msg = msg;
        this.date = new Date() ;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Person getPersonFrom() {
        return personFrom;
    }

    public void setPersonFrom(Person personFrom) {
        this.personFrom = personFrom;
    }

    public Person getPersonTo() {
        return personTo;
    }

    public void setPersonTo(Person personTo) {
        this.personTo = personTo;
    }

}
/**
 *
 *
 {"msg": " XDXDXDXDDXDDXXX"}
 *
 */
