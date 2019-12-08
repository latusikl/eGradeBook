package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.egradebook.model.entities.User;

public interface UserRepository extends CrudRepository<User,Integer> {}
