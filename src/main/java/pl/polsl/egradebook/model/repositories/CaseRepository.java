package pl.polsl.egradebook.model.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Case;

import java.util.List;

@Repository
public interface CaseRepository extends CrudRepository<Case, Integer> {
    Case findByCaseID(int caseID);

    List<Case> findByReceiver_UserID(int receiverID);

    List<Case> findByReceiver_UserIDOrSender_UserID(int receiverID, int senderID);

    List<Case> findAllByReceiver_UserIDAndAndCaseID(int receiverID, int caseID);

    List<Case> findAllBySender_UserIDAndAndCaseID(int receiverID, int caseID);

}

