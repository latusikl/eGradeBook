package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.egradebook.model.entities.Lesson;

import java.util.List;

public interface LessonRepository extends CrudRepository<Lesson,Integer> {
    Lesson findByLessonID(int lessonID);

    List<Lesson> findAllByStudentsClass_ClassID(int classID);

    List<Lesson> findAllByTeacher_TeacherID(int teacherID);
}
