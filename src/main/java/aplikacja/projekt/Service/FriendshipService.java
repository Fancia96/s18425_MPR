package aplikacja.projekt.Service;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.FriendshipRepository;
import aplikacja.projekt.Repository.MessageRepository;
import aplikacja.projekt.Repository.PersonRepository;
import javassist.NotFoundException;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.RelationNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {
    //jak usune osobe to usuam viadomosci z nia zviazene

    private FriendshipRepository friendshipRepository;
    private PersonRepository personRepository;
    private MessageRepository messageRepository;

    public List<Friendship> addFriendshipBetweenIDs(Long personOneID, Long personTwoID){
        //szukam czy istnieją osoby
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()){
            // szukam już czy przypadkiem nie istnieją relacje  pomiędzy tymi ID
            Optional<Friendship> list = friendshipRepository.findFriendshipBetweenPeople(
                    personOne.get(), personTwo.get());

            //blokada. jesli nie istnieje to dodaje
            if(list.isEmpty()){
                //dodaje friendship w dwie strony
                Friendship friendshipAddOneSide = new Friendship(
                        personOne.get(), personTwo.get());
                Friendship friendshipAddSecondSide = new Friendship(
                        personTwo.get(), personOne.get());
                return friendshipRepository.saveAll(List.of(friendshipAddOneSide,friendshipAddSecondSide));
            }
            throw new ObjectNotFoundException(MessageService.class, "Relation exists already");

        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public void deleteFriendshipBetweenIDs(Long personOneID, Long personTwoID){
        //usuwam dwie relacje plus wiadomosci
            //szukam czy istnieją osoby
            Optional<Person> personOne = personRepository.findById(personOneID);
            Optional<Person> personTwo = personRepository.findById(personTwoID);

            if(personOne.isPresent() && personTwo.isPresent()){

                // szukam czy istnieje relacja
                Optional<Friendship> relationOne = friendshipRepository.findFriendshipBetweenPeople(
                        personOne.get(), personTwo.get());
                Optional<Friendship> relationTwo = friendshipRepository.findFriendshipBetweenPeople(
                        personTwo.get(), personOne.get());

                //blokada. jesli nie istnieje to dodaje
                if(relationOne.isPresent() && relationTwo.isPresent()){
                    //usuwam dwa friendshipy szukam

                    //szukam wiadomosci tak wiem te nazwy są piekne
                    Optional<List<Message>> messages = messageRepository.findAllMessagesBetweenPeople(personOne.get(), personTwo.get());
                    if(messages.isPresent()){
                        messageRepository.deleteAll(messages.get());
                        //zakladam ze przejdzie
                    }
                    friendshipRepository.deleteAll(List.of(relationOne.get(), relationTwo.get()));

                    //tutaj mam problem zeby ladnie zatwierdzic wszystkie delete za jednym razem HMMMM
                }
                else {
                    throw new ObjectNotFoundException(MessageService.class, ProjektApplication.relationNotFound);
                }
            }
            else {
                throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
            }
    }

    public List<Friendship> findFriendsForID(Long ID){
        Optional<Person> person = personRepository.findById(ID);
        if(person.isPresent()){
           Optional<List<Friendship>> list = friendshipRepository.findFriendsForID(person.get());
            if(list.isPresent()){
                return list.get();
            }
            throw new ObjectNotFoundException(MessageService.class, ProjektApplication.relationNotFound);
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public Friendship findIfFriendshipExistsForIDs(Long personOneID, Long personTwoID){
        //szukam czy istnieją osoby
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()){
            //szukam tylko jednej friendship bo jedna wystarczy żeby potem wiedzieć
            // czy usunąć je obydwie czy mogę też dodać
            Optional<Friendship> friendship = friendshipRepository.findFriendshipBetweenPeople(
                    personOne.get(), personTwo.get());
            if(friendship.isPresent()){
                return friendship.get();
            }
            return null;
        }
        else {
            throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
        }
    }

    public List<Friendship> getAllFriendships(){
        Optional<List<Friendship>> list = friendshipRepository.findAllFriendships();
        if(list.isPresent()){
            return list.get();
        }
        return new ArrayList<Friendship>();
    }

    public void deleteAll(){
        friendshipRepository.deleteAll();
    }

    public FriendshipService(FriendshipRepository friendshipRepository, PersonRepository personRepository, MessageRepository messageRepository){
        this.friendshipRepository = friendshipRepository;
        this.personRepository = personRepository;
        this.messageRepository = messageRepository;
    }
}