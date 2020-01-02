package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.egradebook.model.entities.Presence;
import pl.polsl.egradebook.model.entities.Grade;

import java.util.List;

public interface PresenceRepository extends CrudRepository<Presence,Integer> {
    List<Presence> findByStudent_studentID(int studentID);
}
