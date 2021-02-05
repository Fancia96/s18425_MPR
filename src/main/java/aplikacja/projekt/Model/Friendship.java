package aplikacja.projekt.Model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;

@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;

    @ManyToOne
    @JsonIgnoreProperties(value = { "listOfFriends", "listOfFriendsTwo","listOfMessagesFrom", "listOfMessagesTo" }, allowSetters=true)
    private Person personOne;

    @ManyToOne
    @JsonIgnoreProperties(value = { "listOfFriends", "listOfFriendsTwo","listOfMessagesFrom", "listOfMessagesTo" }, allowSetters=true)
    private Person personTwo;

    public Friendship() {
    }

    public Friendship(Person personOne, Person personTwo) {
        this.personOne = personOne;
        this.personTwo = personTwo;
    }

    public Person getPersonOne() {
        return personOne;
    }

    public void setPersonOne(Person personOne) {
        this.personOne = personOne;
    }

    public Person getPersonTwo() {
        return personTwo;
    }

    public void setPersonTwo(Person personTwo) {
        this.personTwo = personTwo;
    }
}
/**
 *

 *
 */