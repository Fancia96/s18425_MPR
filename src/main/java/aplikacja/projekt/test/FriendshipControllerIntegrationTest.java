package aplikacja.projekt.test;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.FriendshipRepository;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class FriendshipControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private PersonRepository personRepository;

    private static final ObjectMapper om = new ObjectMapper();

    private void clean() {
        friendshipRepository.deleteAll();
        personRepository.deleteAll();

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
    public void shouldCreateFriendship() throws Exception {
        clean();

        createTwoPeople();

        mvc.perform(post("/addFriendshipBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].personTwo.id", is(2)))
                .andExpect(jsonPath("$[0].personOne.id", is(1)));
    }

   // .andExpect(jsonPath("$.password").doesNotExist())

    @Test
    public void shouldNotCreateFriendship() throws Exception{
        clean();
        mvc.perform(post("/addFriendshipBetweenIDs/1/11")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.personNotFound)));
    }

    @Test
    public void shouldFindAllFriendships() throws Exception {
        clean();

        createTwoPeople();

        createRelation();

        mvc.perform(get("/getAllFriendships")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
        public void shouldFindFriendshipBetweenIDs() throws Exception {
        clean();

        createTwoPeople();

        Friendship friendship = new Friendship(
                personRepository.getOne(1l), personRepository.getOne(2l));
        friendshipRepository.save(friendship);

        mvc.perform(get("/findIfFriendshipExistsForIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.personOne.id", is(1)))
                .andExpect(jsonPath("$.personTwo.id", is(2)));
     }

    @Test
    public void shouldNotFindFrienshipBetweenIdS() throws Exception {
        clean();
        mvc.perform(get("/findIfFriendshipExistsForIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.personNotFound)));

    }


    @Test
    public void shouldDeleteFriendship() throws Exception {
        clean();

        createTwoPeople();

        createRelation();

        mvc.perform(MockMvcRequestBuilders.delete("/deleteFriendshipBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    public void shouldNotDeleteFriendship() throws Exception {
        clean();

        createTwoPeople();

        mvc.perform(MockMvcRequestBuilders.delete("/deleteFriendshipBetweenIDs/1/2")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.relationNotFound)));

    }

}
