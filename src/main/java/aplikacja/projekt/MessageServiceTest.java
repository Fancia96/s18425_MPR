package aplikacja.projekt;

import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Service.FriendshipService;
import aplikacja.projekt.Service.MessageService;
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
public class MessageServiceTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private MessageService messageService;

    @Test
    public void shouldSendMessage(){
        messageService.deleteAll();
        personService.deleteAll();
        friendshipService.deleteAll();
        createMessage();

        messageService.sendMessageBetweenIDs(
                1l, 2l, new Message("abc"));

        Assert.assertNotNull(messageService.getMessageByID(1l));


    }
    @Test
    public void shouldNotSendMessage(){
        messageService.deleteAll();
        personService.deleteAll();
        friendshipService.deleteAll();
        createMessage();

        try {
        messageService.sendMessageBetweenIDs(
                1l, 2l, new Message("abc"));
            Assert.assertTrue(false);
        }
        catch(
                ObjectNotFoundException obj) {
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.personNotFound);
        }

    }

    @Test
    public void shouldDeleteMessage(){
        messageService.deleteAll();
        personService.deleteAll();
        friendshipService.deleteAll();
        createMessage();

        messageService.sendMessageBetweenIDs(
                1l, 2l, new Message("abc"));

        try {
            messageService.deleteMessageByID(1L);
            messageService.getMessageByID(1l);
            Assert.assertTrue(false);
        }
        catch(
                ObjectNotFoundException obj) {
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.messageNotFound);
        }

    }

    @Test
    public void shouldNotDeleteMessage(){
        messageService.deleteAll();
        personService.deleteAll();
        friendshipService.deleteAll();
        createMessage();

        messageService.sendMessageBetweenIDs(
                1l, 2l, new Message("abc"));

        personService.deletePersonByID(1l);

        try {
            messageService.deleteAllMessagesBetweenIDs(1l, 2l);
            Assert.assertTrue(false);
        }
        catch(
    ObjectNotFoundException obj) {
        Assert.assertEquals(obj.getEntityName(),
                ProjektApplication.personNotFound);
    }

    }

    private void createMessage(){

        Person userOne = new Person("Tom");
        Person userTwo = new Person("TomTwo");

        personService.createPerson(userOne);
        personService.createPerson(userTwo);

        friendshipService.addFriendshipBetweenIDs(userOne.getID(), userTwo.getID());
    }

}
