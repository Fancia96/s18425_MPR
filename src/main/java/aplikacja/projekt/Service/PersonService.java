package aplikacja.projekt.Service;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.FriendshipRepository;
import aplikacja.projekt.Repository.MessageRepository;
import aplikacja.projekt.Repository.PersonRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonService {
    //jak usune osobe to usuam viadomosci z nia zviazene

    private PersonRepository personRepository;
    private FriendshipRepository friendshipRepository;
    private MessageRepository messageRepository;

    //nie powinno pozwolic wrzucic z tym samym nickiem
    // nie pozwol wrzucic z brzydkimi słowami
    public Person createPerson(Person person) {
        if(checkIfYouCanCreatePerson(person.getNickname())){
            Person newPerson = person;
            return personRepository.save(newPerson);
        }
        throw new DuplicateKeyException("Word you put in name is not allowed");
    }

    private boolean checkIfYouCanCreatePerson(String name){
        for(String bad : ProjektApplication.badWords){
            if(name.toLowerCase().contains(bad.toLowerCase())){
                return false;
            }
        }
        return true;
    }

    // it will mieni nazwe  osoby i beede mogla zrobic test jednostkowy dla tego i upewnic sie ze zmienilo
    //  robie klase z metodami i z adnotacja test i leci to od gory do doołu i cyk
    public Person editPersonByID(Person person){
        Optional<Person> personFind = personRepository.findById(person.getID());
        if(personFind.isPresent()){
            if(checkIfYouCanCreatePerson(person.getNickname())){
                return personRepository.save(person);
            }
            throw new DuplicateKeyException("Word you put in name is not allowed");
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public void deletePersonByID(Long ID){
        Optional<Person> person = personRepository.findById(ID);
        if(person.isPresent()){

            //zapytanie zeby znalezc relacje w dwie strony gdzie jest person
            Optional<List<Friendship>> relationOne = friendshipRepository.findAllFreindshipsThisPersonHasWithEveryoneAndBack(
                    person.get());

            List<Friendship> listAllFreindshipsToDelete = new ArrayList<Friendship>();
            if(relationOne.isPresent()){
                listAllFreindshipsToDelete = relationOne.get();
            }

            Optional<List<Message>> messages = messageRepository
                    .findAllMessagesThisPersonSentAndReceived (person.get());

            List<Message> listAllMessagesToDelete = new ArrayList<Message>();
            if(messages.isPresent()){
                listAllMessagesToDelete = messages.get();
            }
            if(listAllMessagesToDelete.size() > 0) {
                messageRepository.deleteAll(listAllMessagesToDelete);
            }
            if(listAllFreindshipsToDelete.size() > 0) {
                friendshipRepository.deleteAll(listAllFreindshipsToDelete);
            }
            personRepository.deleteById(ID);
        }
        else {
            throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
        }

    }

    public Person findPersonByID(Long ID){
        Optional<Person> person = personRepository.findById(ID);
        if(person.isPresent()){
            return person.get();
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public Person findPersonByName(Person personFind){
        Optional<Person> person = personRepository.findPersonByName(personFind.getNickname());
        if(person.isPresent()){
            return person.get();
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public List<Person> getEverything(){
        return personRepository.findAll();
    }

    public void deleteAll(){
        personRepository.deleteAll();
    }

    public PersonService(PersonRepository personRepository, FriendshipRepository friendshipRepository, MessageRepository messageRepository){
        this.personRepository = personRepository;
        this.messageRepository = messageRepository;
        this.friendshipRepository = friendshipRepository;

    }

    public Person findOne(Person person) {
        Optional<Person> per = personRepository.findById(person.getID());
        if(per.isPresent()){

            return per.get();
        }
        return null;
    }

}
