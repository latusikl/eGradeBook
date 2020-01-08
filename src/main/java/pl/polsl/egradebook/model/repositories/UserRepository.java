package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.entities.Student;

import java.util.List;

/**
 Repository for executing SQL queries connected with User Entity.
 */
@Repository
public interface UserRepository extends CrudRepository<User,Integer> {
	User findUserByUserName(String userName);

}
