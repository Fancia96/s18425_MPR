package aplikacja.projekt.Model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    @Column(unique = true)
    private String nickname;

    ///dla wiadomosci
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personFrom")
    private List<Message> listOfMessagesFrom = new ArrayList<Message>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personTo")
    private List<Message> ListOfMessagesTo = new ArrayList<Message>();

    //dla relacji przyjaciele
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personOne")
    private List<Friendship> ListOfFriends = new ArrayList<Friendship>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "personTwo")
    private List<Friendship> ListOfFriendsTwo = new ArrayList<Friendship>();

    public void setListOfFriendsTwo(List<Friendship> listOfFriendsTwo) {
        ListOfFriendsTwo = listOfFriendsTwo;
    }

    public List<Friendship> getListOfFriends() {
        return ListOfFriends;
    }

    public List<Friendship> getListOfFriendsTwo() {
        return ListOfFriendsTwo;
    }

    public void setListOfFriends(List<Friendship> listOfFriends) {
        ListOfFriends = listOfFriends;
    }

    public Person() {
        this.nickname = "nick"+getID();
    }

    public Person(String nickname) {
        this.nickname = nickname;
    }

    public Long getID() {
        return ID;
    }

    //@JsonIgnore
    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        String friends = "";
        for(Friendship person : getListOfFriends()){
            //friends += person.getNickname();
        }

        return "Person{" +
                "ID=" + ID +
                ", nickname='" + nickname +"}";
    }

    public List<Message> getListOfMessagesFrom() {
        return listOfMessagesFrom;
    }

    public void setListOfMessagesFrom(List<Message> listOfMessagesFrom) {
        this.listOfMessagesFrom = listOfMessagesFrom;
    }

    public List<Message> getListOfMessagesTo() {
        return ListOfMessagesTo;
    }

    public void setListOfMessagesTo(List<Message> listOfMessagesTo) {
        ListOfMessagesTo = listOfMessagesTo;
    }
}


/**
 *
 *
 {"nickname": "Baby"}

 {"nickname": "KalaBBB", "id": 4}

 *
 */
