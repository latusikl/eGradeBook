package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Teacher;


@Repository
public interface TeacherRepository extends CrudRepository<Teacher, Integer> {
    Teacher findByUser_UserName(String userName);
}
