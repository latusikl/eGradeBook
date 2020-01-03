package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Case;

import java.util.List;

@Repository
public interface CaseRepository extends CrudRepository<Case,Integer> {
    List<Case> findByReceiver_UserID(int receiverID);
}

