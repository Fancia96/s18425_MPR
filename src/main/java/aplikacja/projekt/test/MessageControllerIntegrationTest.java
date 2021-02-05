package aplikacja.projekt.test;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.FriendshipRepository;
import aplikacja.projekt.Repository.MessageRepository;
import aplikacja.projekt.Repository.PersonRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MessageControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private MessageRepository messageRepository;

    private static final ObjectMapper om = new ObjectMapper();

    private void clean() {
        messageRepository.deleteAll();
        friendshipRepository.deleteAll();
        personRepository.deleteAll();

//        clean() should return the list of people created
//        and use it to build requests
//        like
//        '/get/' + listOfCreatedPeople[0].getID()
//        that way we don't have hardcoded ids
//        to worry about

    }

    private void createTwoPeople(){
        Person person = new Person("Bob");
        personRepository.save(person);
        Person person2 = new Person("Bob2");
        personRepository.save(person2);
    }

    private void createRelation(){
        Friendship friendship = new Friendship(
                personRepository.getOne(1l), personRepository.getOne(2l));
        friendshipRepository.save(friendship);

        Friendship friendship2 = new Friendship(
                personRepository.getOne(2l), personRepository.getOne(1l));
        friendshipRepository.save(friendship2);
    }

    @Test
    public void shouldSendMessage() throws Exception {
       // clean();

        createTwoPeople();

        createRelation();

        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");

        mvc.perform(post("/sendMessageBetweenIDs/1/2")
                .content(om.writeValueAsString(message))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }

    @Test
    public void shouldGetAllMessagesBetween() throws Exception {
        clean();
        createTwoPeople();
        createRelation();
        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");
        messageRepository.save(message);
        Message message2 = new Message(personRepository.findById(2l).get(),personRepository.findById(1l).get(),"tek");
        messageRepository.save(message2);

        mvc.perform(get("/getAllMessagesBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void shouldFindMessageById() throws Exception {
        clean();
        createTwoPeople();
        createRelation();
        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");
        messageRepository.save(message);

        mvc.perform(get("/getMessageByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)));
    }


    @Test
    public void shouldNotFindMessageById() throws Exception {
        clean();

        mvc.perform(get("/getMessageByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.messageNotFound)));
    }


    @Test
    public void shouldFindAllMessages() throws Exception {
        clean();
createTwoPeople();
createRelation();
        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");
        messageRepository.save(message);

        mvc.perform(get("/getAllMessages")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    public void shouldDeleteMessageBetweenIDs() throws Exception {
        clean();
        createTwoPeople();
        createRelation();
        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");
        messageRepository.save(message);
        Message message2 = new Message(personRepository.findById(2l).get(),personRepository.findById(1l).get(),"tek");
        messageRepository.save(message2);

        mvc.perform(MockMvcRequestBuilders.delete("/deleteAllMessagesBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    public void shouldNotDeleteMessageBetweenIDs() throws Exception {
        clean();

        mvc.perform(MockMvcRequestBuilders.delete("/deleteAllMessagesBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.personNotFound)));
    }

    @Test
    public void shouldDeleteMessageByIDs() throws Exception {
        clean();
        createTwoPeople();
        createRelation();
        Message message = new Message(personRepository.findById(1l).get(),personRepository.findById(2l).get(),"tek");
        messageRepository.save(message);

        mvc.perform(MockMvcRequestBuilders.delete("/deleteMessageByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    public void shouldNotDeleteMessageByIDs() throws Exception {
        clean();
        createTwoPeople();
        createRelation();

        mvc.perform(MockMvcRequestBuilders.delete("/deleteMessageByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.messageNotFound)));
    }

}
