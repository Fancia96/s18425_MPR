package aplikacja.projekt.Controller;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import aplikacja.projekt.Service.FriendshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FriendshipController {

    @Autowired
    private FriendshipService friendshipService;

    @PostMapping("/addCustomDataFriendship")
    public ResponseEntity<String> addCustomDataFriendship(){

        //add Fancia Friend Fashoo
        List<Friendship> addFriends1 = friendshipService.addFriendshipBetweenIDs(
                1l,
                2l);

        //da info ze nie moze bo uzytkownik zabanowany lol
        List<Friendship>  addFriends2 = friendshipService.addFriendshipBetweenIDs(
                1l,
                3l);

        List<Friendship> addFriends3 = friendshipService.addFriendshipBetweenIDs(
                4l,
                2l);

        List<Friendship> addFriends4 = friendshipService.addFriendshipBetweenIDs(
                4l,
                1l);

        return ResponseEntity.ok("i added test friendshibs. PS BAN SHOBI :D");

    }

    //czy istnieje , szukam po nicku
    @PostMapping("/addFriendshipBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<List<Friendship>> addFriendshipBetweenIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID){
        List<Friendship>  createThisFriendship = friendshipService.addFriendshipBetweenIDs(
                personOneID, personTwoID);

        return ResponseEntity.ok(createThisFriendship);
    }

    @DeleteMapping("/deleteFriendshipBetweenIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<Void> deleteFriendshipBetweenIDs(
            @PathVariable Long personOneID,@PathVariable Long personTwoID){
        friendshipService.deleteFriendshipBetweenIDs(personOneID, personTwoID);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/findIfFriendshipExistsForIDs/{personOneID}/{personTwoID}")
    public ResponseEntity<Friendship> findIfFriendshipExistsForIDs(
            @PathVariable Long personOneID, @PathVariable Long personTwoID){
        return  ResponseEntity.ok(friendshipService.findIfFriendshipExistsForIDs(personOneID, personTwoID));
    }

    @GetMapping("/findFriendsForID/{personID}")
    public ResponseEntity<List<Friendship>> findFriendsForID(@PathVariable Long personID){
        return  ResponseEntity.ok(friendshipService.findFriendsForID(personID));
    }

    @GetMapping("/getAllFriendships")
    public ResponseEntity<List<Friendship>> getAllFriendships() {
        return ResponseEntity.ok(friendshipService.getAllFriendships());
    }

}
