package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.egradebook.model.entities.Presence;
import java.util.List;

public interface PresenceRepository extends CrudRepository<Presence,Integer> {
    List<Presence> findByStudent_studentID(int studentID);
    List<Presence> findAllByDateAndLesson_LessonID(String date, int lessonID);
    List<Presence> findAllByLesson_Teacher_User_UserID_OrderByDateDesc(int teacherID);
    Presence findByPresenceID(int presenceID);
}
