package pl.polsl.egradebook.model.repositories;

import org.springframework.data.repository.CrudRepository;
import pl.polsl.egradebook.model.entities.StudentsClass;

import java.util.List;

public interface StudentsClassRepository extends CrudRepository<StudentsClass, Integer> {
   // List<StudentsClass> findDistinctByClassID(int classID);
}
