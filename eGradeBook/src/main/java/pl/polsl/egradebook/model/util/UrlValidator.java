package pl.polsl.egradebook.model.util;

import pl.polsl.egradebook.model.entities.Case;
import pl.polsl.egradebook.model.repositories.CaseRepository;

import java.util.List;

public class UrlValidator {
    public static boolean canAccessCase(int caseID, int receiverID, CaseRepository caseRepository) {
        List<Case> casesReceived = caseRepository.findAllByReceiver_UserIDAndAndCaseID(receiverID, caseID);
        List<Case> casesSent = caseRepository.findAllBySender_UserIDAndAndCaseID(receiverID, caseID);
        return !(casesReceived.size() == 0 && casesSent.size() == 0);
    }
}
