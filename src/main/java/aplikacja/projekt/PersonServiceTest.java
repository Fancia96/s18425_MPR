package aplikacja.projekt;

import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Repository.FriendshipRepository;
import aplikacja.projekt.Repository.MessageRepository;
import aplikacja.projekt.Repository.PersonRepository;
import aplikacja.projekt.Service.PersonService;
import org.hibernate.ObjectNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
@RunWith(SpringRunner.class)
//@DataJpaTest
public class PersonServiceTest {
//    @MockBean
//    PersonRepository repo;
//    @MockBean
//    FriendshipRepository repo2;
//    @MockBean
//    MessageRepository repo3;

    //@Autowired
   // private UserRepository userRepository;

    @Autowired
    //@InjectMocks
    private PersonService personService;

    void cleanUp(){
      ///  repo = Mockito.mock(PersonRepository.class);
       // repo2 = Mockito.mock(FriendshipRepository.class);
        //repo3 = Mockito.mock(MessageRepository.class);

       // personService = new PersonService(repo, repo2, repo3);
    }

    @BeforeEach
    public void setup(){
        personService.deleteAll();
        //MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldCreatePerson(){
        personService.deleteAll();
        Person user = new Person("Tom");
        personService.createPerson(user);
        Assert.assertNotNull(user.getID());
    }

    @Test
    public void shouldNotCreatePersonBadWords(){
        personService.deleteAll();
        Person user = new Person("milk");
        try {
            personService.createPerson(user);
        }
        catch(Exception EXC){}

        Assert.assertNull(user.getID() );
    }

    @Test
    public void shouldEditPerson(){
        personService.deleteAll();
        Person user = new Person("Tom");
        personService.createPerson(user);
        user.setNickname("newNicnkanme");
        try{
            personService.editPersonByID(user);
        }

        catch(Exception EXC){}


        Assert.assertEquals(user.getNickname(),
                personService.findPersonByID(user.getID()).getNickname());
    }

    @Test
    public void shouldNotEditPerson(){
        personService.deleteAll();
        Person user = new Person("Tom");
        personService.createPerson(user);
        user.setNickname("milk");
        try{
            personService.editPersonByID(user);
        }

        catch(Exception EXC){}


        Assert.assertNotEquals(user.getNickname(),
                personService.findPersonByID(user.getID()).getNickname());
    }

    @Test
    public void shouldNotEditPersonBadWords(){
        personService.deleteAll();
        Person user = new Person("Tom");
        personService.createPerson(user);

        Long id = user.getID();

        Person userEditLikeThis = new Person("Tommilk");
        userEditLikeThis.setID(id);
        try{
            personService.editPersonByID(user);
        }
        catch(Exception EXC){}


        Assert.assertNotEquals(user.getNickname(), "Tommilk");
    }

    @Test
    public void shouldNotDeletePerson(){
        personService.deleteAll();

        Long id = 1l;
        try{
            personService.deletePersonByID(id);
            Assert.assertTrue(false);
        }
        catch(ObjectNotFoundException obj){
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.personNotFound);
       }

    }

    @Test
    public void shouldDeletePerson(){
        personService.deleteAll();
        Person user = new Person("Tom");
        personService.createPerson(user);

        Long id = user.getID();
        personService.deletePersonByID(user.getID());
        try{
            personService.findPersonByID(id);

            Assert.assertTrue(false);
        }
        catch(ObjectNotFoundException obj){
            Assert.assertEquals(obj.getEntityName(),
                    ProjektApplication.personNotFound);}
    }


}
