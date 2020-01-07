package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Teacher;
import pl.polsl.egradebook.model.entities.User;

@Repository
public interface TeacherRepository extends CrudRepository<Teacher,Integer> {
    Teacher findByUser_UserName(String userName);
}
