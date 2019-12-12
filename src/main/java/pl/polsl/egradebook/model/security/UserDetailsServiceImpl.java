package pl.polsl.egradebook.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.polsl.egradebook.model.entities.User;
import pl.polsl.egradebook.model.repositories.UserRepository;

/**
 Implementation of required login service interface for authentication
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleProperties roleProperties;
	
	@Override
	public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
		User user = userRepository.findUserByUserName(login);
		
		if(user == null)
			throw new UsernameNotFoundException(login);
		
		return new UserDetailsImpl(user,roleProperties);
	}
}
