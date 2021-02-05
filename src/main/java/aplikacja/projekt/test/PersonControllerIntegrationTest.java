package aplikacja.projekt.test;

import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.PersonRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private PersonRepository personRepository;

    private static final ObjectMapper om = new ObjectMapper();

    @AfterEach
    private void clean1() {
        personRepository.deleteAll();
        personRepository.flush();
    }

    private int last_id = 1;

    @Test
    public void shouldCreatePerson() throws Exception {
        clean1();
        Person person = new Person("Bob");

        mvc.perform(post("/createPerson")
                .content(om.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(last_id)))
                .andExpect(jsonPath("$.nickname", is("Bob")));

        last_id ++ ;
    }

    @Test
    public void shouldNotCreatePerson() throws Exception {
        clean1();
        Person person = new Person("milk");

        mvc.perform(post("/createPerson")
                .content(om.writeValueAsString(person))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.BAD_REQUEST", is("Word you put in name is not allowed")));
    }

    @Test
    public void shouldFindPersonById() throws Exception {
        clean1();
        Person person = new Person("Bob");
        personRepository.save(person);

        mvc.perform(get("/findPersonByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(last_id)))
                .andExpect(jsonPath("$.nickname", is("Bob")));

        last_id ++;
    }

    @Test
    public void shouldNotFindPersonById() throws Exception {
        clean1();
        mvc.perform(get("/findPersonByID/100")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.personNotFound)));
    }


    @Test
    public void shouldFindPersonAll() throws Exception {
        clean1();

        Person person = new Person("Bob");
        personRepository.save(person);
        Person person2 = new Person("Bob2");
        personRepository.save(person2);

        mvc.perform(get("/getEverything")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(last_id)))
                .andExpect(jsonPath("$[0].nickname", is("Bob")));

        personRepository.deleteAll();
        last_id++;
        last_id++;
    }


    @Test
    public void shouldEditPerson() throws Exception{
        clean1();
        Person person = new Person("sun");
        personRepository.save(person);

        Person person2 = new Person("moon");
        person2.setID(person.getID());

        mvc.perform(put("/editPerson")
                .content(om.writeValueAsString(person2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname", is("moon")));
    }

    @Test
    public void shouldNotEditPerson() throws Exception {
        clean1();
        Person person = new Person("sun");
        personRepository.save(person);

        Person person2 = new Person("milk");
        person2.setID(person.getID());

        mvc.perform(put("/editPerson")
                .content(om.writeValueAsString(person2))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.BAD_REQUEST", is("Word you put in name is not allowed")));
    }

    @Test
    public void shouldDeletePerson() throws Exception {
        clean1();
        Person person = new Person("sun");
        personRepository.save(person);

        mvc.perform(MockMvcRequestBuilders.delete("/deletePersonByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").doesNotHaveJsonPath());
    }

    @Test
    public void shouldNotDeletePerson() throws Exception {
        clean1();

        mvc.perform(MockMvcRequestBuilders.delete("/deletePersonByID/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.NOT_FOUND", is(ProjektApplication.personNotFound)));
    }

//    private void loadH2FromBackup() {
//        println "Restoring H2 from backup location."
//        Sql sql = connectToSql()
//        sql.execute("DROP ALL OBJECTS")
//        sql.execute("RUNSCRIPT FROM ?", [H2_BACKUP_LOCATION])
//    }
//
//    private Sql connectToSql() {
//        Sql.newInstance('jdbc:h2:file:/tmp/myApplicationDb;AUTO_SERVER=TRUE', 'sa', '', 'org.h2.Driver')
//    }

}
