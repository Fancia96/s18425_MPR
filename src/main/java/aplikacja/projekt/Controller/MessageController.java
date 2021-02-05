package aplikacja.projekt.Controller;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Service.FriendshipService;
import aplikacja.projekt.Service.MessageService;
import aplikacja.projekt.Service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/addCustomDataMessages")
    public ResponseEntity<String> addCustomDataMessages(){

        messageService.sendMessageBetweenIDs(
                2l,
                1l, new Message(
                        "Hai how are you"));

        messageService.sendMessageBetweenIDs(
                1l,
                2l, new Message(
                        "O hello im good how are yooouuu"));

        messageService.sendMessageBetweenIDs(
                2l,
                1l, new Message(
                        "eating and soon playing leagueoflegends"));

        messageService.sendMessageBetweenIDs(
                1l,
                2l, new Message(
                        "what?"));

        messageService.sendMessageBetweenIDs(
                2l,
                1l, new Message(
                        "lol"));

        messageService.sendMessageBetweenIDs(
                1l,
                2l, new Message(
                        "hahahaha xD"));

        //wiecej innych
        messageService.sendMessageBetweenIDs(
                2l,
                4l, new Message(
                        "Hi wona play with me"));
        messageService.sendMessageBetweenIDs(
                4l,
                2l, new Message(
                        "yes sure brotha"));

        messageService.sendMessageBetweenIDs(
                4l,
                1l, new Message(
                        "Come play"));
        messageService.sendMessageBetweenIDs(
                1l,
                4l, new Message(
                        "no"));

        return ResponseEntity.ok("i added test messages :D");

    }

    @PostMapping("/sendMessageBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<Message> sendMessageBetweenIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID,
            @RequestBody Message message){
        Message sendThisMessage = messageService.sendMessageBetweenIDs(
                personOneID, personTwoID, message);

        return ResponseEntity.ok(sendThisMessage);
    }

    @DeleteMapping("/deleteMessageByID/{ID}")
    public ResponseEntity<Void> deleteMessageByID(@PathVariable Long ID){
        messageService.deleteMessageByID(ID);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/deleteAllMessagesBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<Void> deleteAllMessagesBetweenIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID){
        messageService.deleteAllMessagesBetweenIDs(personOneID, personTwoID);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAllMessagesBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<List<Object>> getAllMessagesBetweenIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID){

        return ResponseEntity.ok( messageService.getAllMessagesBetweenIDs(personOneID,personTwoID));
    }

    @GetMapping("/findMessagesContainATextBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<List<Message>> findMessagesContainATextBetweenIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID,
            @RequestBody Message message){

        return ResponseEntity.ok( messageService.findMessagesContainATextBetweenIDs(personOneID, personTwoID, message));
    }

    @GetMapping("/getMessageByID/{ID}")
    public ResponseEntity<Message> getMessageByID(
            @PathVariable Long ID){

            return ResponseEntity.ok(messageService.getMessageByID(ID));
    }

    @GetMapping("/getAllMessages")
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok( messageService.getAllMessages());
    }

}
