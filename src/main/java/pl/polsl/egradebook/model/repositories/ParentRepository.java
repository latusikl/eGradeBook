package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.Parent;
import pl.polsl.egradebook.model.entities.Student;

@Repository
public interface ParentRepository extends CrudRepository<Parent,Integer> {
    Parent findByUser_UserName(String userName);
}
