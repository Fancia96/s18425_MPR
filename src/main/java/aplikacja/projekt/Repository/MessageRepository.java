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
public interface MessageRepository extends JpaRepository<Message, Long> {

    //zalicz wszystkie wiadomosci pomiedzy osobami

    //find in conversation word like bblkabla

    @Query(" SELECT msg FROM Message msg " +
            "where (msg.personFrom = :personFrom and msg.personTo = :personTo) or " +
            "(msg.personTo = :personFrom and msg.personFrom = :personTo)")
    Optional<List<Message>> findAllMessagesBetweenPeople(Person personFrom, Person personTo);

    @Query(" SELECT msg.id FROM Message msg " +
            "where (msg.personFrom = :personFrom and msg.personTo = :personTo) or " +
            "(msg.personTo = :personFrom and msg.personFrom = :personTo)")
    Optional<List<Long>> findAllMessagesIDsBetweenPeople(Person personFrom, Person personTo);

    //tylko do wyswietlenia ladnie dla
    @Query(" SELECT msg.personFrom.nickname, msg.msg, msg.date FROM Message msg " +
            "where (msg.personFrom = :personFrom and msg.personTo = :personTo) or " +
            "(msg.personTo = :personFrom and msg.personFrom = :personTo) order by msg.date ")
    Optional<List<Object>> findAllMessagesBetweenPeopleOrderByTime(Person personFrom, Person personTo);

    @Query(" SELECT msg FROM Message msg " +
            "where (msg.personFrom = :person) or (msg.personTo = :person)")
    Optional<List<Message>> findAllMessagesThisPersonSentAndReceived(Person person);

    @Query(" SELECT msg FROM Message msg " +
            "where ((msg.personFrom = :personFrom and msg.personTo = :personTo) or " +
            "(msg.personTo = :personFrom and msg.personFrom = :personTo)) and UPPER(msg.msg) LIKE %:msg% ")
    Optional<List<Message>> findMessagesContainATextBetweenIDs(Person personFrom, Person personTo, String msg);

}
