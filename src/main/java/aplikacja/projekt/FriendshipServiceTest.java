package aplikacja.projekt;


import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Service.FriendshipService;
import aplikacja.projekt.Service.PersonService;
import org.hibernate.ObjectNotFoundException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class FriendshipServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private FriendshipService friendshipService;

    @Test
    public void shouldCreateFriendship(){
        friendshipService.deleteAll();
        personService.deleteAll();
        Person userOne = new Person("Tom");
        Person userTwo = new Person("TomTwo");

        personService.createPerson(userOne);
        personService.createPerson(userTwo);

        friendshipService.addFriendshipBetweenIDs(userOne.getID(), userTwo.getID());
        Assert.assertNotNull(friendshipService.findIfFriendshipExistsForIDs(userOne.getID(), userTwo.getID()));

    }

    @Test
    public void shouldNotCreateFriendship(){
        friendshipService.deleteAll();
        personService.deleteAll();
        Person userOne = new Person("Tom");

        personService.createPerson(userOne);

        try {
            friendshipService.addFriendshipBetweenIDs(userOne.getID(), 2l);
            Assert.assertTrue(false);
        }
        catch(ObjectNotFoundException obj) {
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.personNotFound);
        }
    }

    @Test
    public void shouldDeleteFriendship(){
        friendshipService.deleteAll();
        personService.deleteAll();
        Person userOne = new Person("Tom");
        Person userTwo = new Person("TomTwo");

        personService.createPerson(userOne);
        personService.createPerson(userTwo);

        friendshipService.addFriendshipBetweenIDs(userOne.getID(), userTwo.getID());

        friendshipService.deleteFriendshipBetweenIDs(
                userOne.getID(), userTwo.getID());

        //wiec znalezienie powinno dac exceptiob


           Assert.assertNull(friendshipService.findIfFriendshipExistsForIDs(
                    userOne.getID(), userTwo.getID()));

    }

    @Test
    public void shouldNotDeleteFriendship(){
        friendshipService.deleteAll();
        personService.deleteAll();
        Person userOne = new Person("Tom");
        Person userTwo = new Person("TomTwo");

        personService.createPerson(userOne);
        personService.createPerson(userTwo);

        friendshipService.addFriendshipBetweenIDs(userOne.getID(), userTwo.getID());

        friendshipService.deleteFriendshipBetweenIDs(
                userOne.getID(), userTwo.getID());

        try{
            friendshipService.deleteFriendshipBetweenIDs(
                userOne.getID(), userTwo.getID());
            Assert.assertTrue(false);

        }
            catch(ObjectNotFoundException obj) {
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.relationNotFound);
        }

    }

}
