package pl.polsl.egradebook;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.UserRepository;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class EGradeBookApplication {

	//Example teacher
	//johsmi12 Password
	//Example student
	//jessTran pass
	//Admin admin nimda
	public static void main(String[] args) {
		SpringApplication.run(EGradeBookApplication.class, args);
	}

}
