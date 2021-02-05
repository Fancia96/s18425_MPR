package aplikacja.projekt.Service;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.ProjektApplication;
import aplikacja.projekt.Repository.FriendshipRepository;
import aplikacja.projekt.Repository.MessageRepository;
import aplikacja.projekt.Repository.PersonRepository;
import org.hibernate.ObjectNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private PersonRepository personRepository;
    private FriendshipRepository friendshipRepository;

    public Message sendMessageBetweenIDs (
            Long personOneID, Long personTwoID, Message message) throws ObjectNotFoundException {
        //szukam obiektow osoby po id
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()){
            Optional<Friendship> findFriendshipOne = friendshipRepository.findFriendshipBetweenPeople(
                    personOne.get(), personTwo.get());

            Optional<Friendship> findFriendshipTwo = friendshipRepository.findFriendshipBetweenPeople(
                    personTwo.get(), personOne.get());

            if(findFriendshipOne.isPresent() && findFriendshipTwo.isPresent()){
                String messageEdited = correctMessage(message.getMsg());

                Message messageAdd = new Message(
                        personOne.get(), personTwo.get(), messageEdited);

                return messageRepository.save(messageAdd);
            }
            else{
                throw new ObjectNotFoundException(MessageService.class, ProjektApplication.relationNotFound);
            }
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);

    }

    private String correctMessage(String msg){
        String message = msg;
        for(String bad : ProjektApplication.badWords) {
            if(msg.contains(bad)) {
                String replaceWithThis = "";
                for (int i = 0; i <= bad.length(); i++) {
                    replaceWithThis += "*";
                }
                message = message.replaceAll(bad, replaceWithThis);
            }
        }
        return message;
    }

    public void deleteMessageByID(Long ID){
        Optional<Message> message = messageRepository.findById(ID);
        if(message.isPresent()){
            messageRepository.deleteById(ID);
        }
        else{
            throw new ObjectNotFoundException(MessageService.class, ProjektApplication.messageNotFound);
        }
    }

    public void deleteAllMessagesBetweenIDs(Long personOneID, Long personTwoID){
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()) {
            Optional<List<Message>> list = messageRepository.findAllMessagesBetweenPeople(personOne.get(), personTwo.get());
            if(list.isPresent()){
                messageRepository.deleteAll(list.get());
            }
        }
        else {
            throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
        }

    }

    public List<Object> getAllMessagesBetweenIDs(
            Long personOneID, Long personTwoID){
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()) {
            Optional<List<Object>> list = messageRepository.findAllMessagesBetweenPeopleOrderByTime(personOne.get(), personTwo.get());
            if (list.isPresent()) {
                return list.get();
            }
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public List<Message> findMessagesContainATextBetweenIDs(
            Long personOneID, Long personTwoID, Message message){
        Optional<Person> personOne = personRepository.findById(personOneID);
        Optional<Person> personTwo = personRepository.findById(personTwoID);

        if(personOne.isPresent() && personTwo.isPresent()) {
            Optional<List<Message>> list = messageRepository.findMessagesContainATextBetweenIDs(personOne.get(), personTwo.get(), message.getMsg().toUpperCase());
            if (list.isPresent()) {
                return list.get();
            }
            return null;
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.personNotFound);
    }

    public Message getMessageByID(Long ID){
        Optional<Message> msg = messageRepository.findById(ID);
        if(msg.isPresent()){
            return msg.get();
        }
        throw new ObjectNotFoundException(MessageService.class, ProjektApplication.messageNotFound);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public MessageService(
            FriendshipRepository friendshipRepository, PersonRepository personRepository,
            MessageRepository messageRepository){
        this.friendshipRepository = friendshipRepository;
        this.personRepository = personRepository;
        this.messageRepository = messageRepository;
    }

    public void deleteAll(){
        messageRepository.deleteAll();
    }

    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
}