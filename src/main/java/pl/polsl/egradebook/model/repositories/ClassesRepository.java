package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.polsl.egradebook.model.entities.StudentsClass;

import java.util.List;

@Repository
public interface ClassesRepository extends CrudRepository<StudentsClass,Integer> {
    List<StudentsClass> findAll();
}
