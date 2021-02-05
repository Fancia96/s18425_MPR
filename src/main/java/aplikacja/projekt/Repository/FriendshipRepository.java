package aplikacja.projekt.Repository;

import aplikacja.projekt.Model.Friendship;
import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    @Query(" SELECT f.personOne.ID, f.personOne.nickname, f.personTwo.nickname FROM Friendship f ")
    Optional<List<Friendship>> findAllFriendships();

    @Query(" SELECT f.personOne.nickname, f.personTwo.nickname FROM Friendship f " +
            "where f.personOne = :person")
    Optional<List<Friendship>> findFriendsForID(Person person);

    @Query(" SELECT f FROM Friendship f " +
            "where f.personOne = :personOne and f.personTwo = :personTwo")
    Optional<Friendship> findFriendshipBetweenPeople(Person personOne, Person personTwo);

    @Query(" SELECT f FROM Friendship f " +
            "where (f.personTwo = :person) or (f.personTwo = :person)")
    Optional<List<Friendship>> findAllFreindshipsThisPersonHasWithEveryoneAndBack(Person person);

}
