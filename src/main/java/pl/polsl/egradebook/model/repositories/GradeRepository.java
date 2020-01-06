package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Grade;

import java.util.List;

@Repository
public interface GradeRepository extends CrudRepository<Grade,Integer> {
    List<Grade> findByStudent_studentID(int studentID);
    List<Grade> findAllByOrderByStudent_User_SurnameAsc();
}
