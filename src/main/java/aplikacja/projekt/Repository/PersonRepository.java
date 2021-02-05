package aplikacja.projekt.Repository;

import aplikacja.projekt.Model.Message;
import aplikacja.projekt.Model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(" SELECT p FROM Person p " +
            "where p.nickname = :name ")
    Optional<Person> findPersonByName(String name);

}
