package aplikacja.projekt.Controller;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Service.FriendshipService;
import aplikacja.projekt.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping("/itsok")
    public ResponseEntity<String> check(){
        return ResponseEntity.ok("itsok :D");

    }

    @PostMapping("/addCustomDataPeople")
    public ResponseEntity<String> addCustomDataPeople() throws Exception{
        Person  createThisPerson1 = personService.createPerson(
                new Person("Fancia"));
        Person  createThisPerson2 = personService.createPerson(
                new Person("Fashoo"));
        Person  createThisPerson3 = personService.createPerson(
                new Person("Shobs"));
        Person  createThisPerson4 = personService.createPerson(
                new Person("Kala"));

        return ResponseEntity.ok("i added test people :D");

    }

    //nickname not longer than 15
    //niedozzwolone znaki specjalne
    //tworze ciało i wkleję sobie tutaj ładnie
    @PostMapping("/createPerson")
    public ResponseEntity<Person> createPerson(@RequestBody Person person){
            Person createThisPerson = personService.createPerson(person);
            return ResponseEntity.ok(createThisPerson);
    }

    @PutMapping("/editPerson")
    public ResponseEntity<Person> editPersonByID(@RequestBody Person person){
        Person  editThisPerson = personService.editPersonByID(person);

        return ResponseEntity.ok(editThisPerson);
    }

    //usuwam osobe ale tez musze zawrzec wiadomosci i przyjaciol ze strony tej osoby
    @DeleteMapping("/deletePersonByID/{ID}")
    public ResponseEntity<Void> deletePersonByID(@PathVariable Long ID){
        personService.deletePersonByID(ID);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/findPersonByName")
    public ResponseEntity<Person> findPersonByName(@RequestBody Person person){
        return ResponseEntity.ok(personService.findPersonByName(person));
    }

    @GetMapping("/findPersonByID/{ID}")
    public ResponseEntity<Person> findPersonByID(@PathVariable Long ID){
        return ResponseEntity.ok(personService.findPersonByID(ID));
    }

    @GetMapping("/getEverything")
    public ResponseEntity<List<Person>> getEverything() {
        return ResponseEntity.ok(personService.getEverything());
    }


}