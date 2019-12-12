package pl.polsl.egradebook.model.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import pl.polsl.egradebook.model.entities.User;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 Implementation of another interface required for authentication.
 */
public class UserDetailsImpl implements UserDetails {
	
	/**
	 Logging user representation.
	 */
	private User user;
	/**
	 All role properties encapsulating object.
	 */
	private RoleProperties roleProperties;
	
	UserDetailsImpl(User user, RoleProperties roleProperties){
		this.user=user;
		this.roleProperties=roleProperties;
	}
	
	/**
	 Function assigns authorities to view URL's based on role name for user.
	 Allowed URL's are retrieved from XML and encapsulated with RoleProperties class.
	 @see RoleProperties
	 */
	private List<GrantedAuthority> getGrantedAuthorityList(String roleType){
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
		
		if(roleProperties.getRoleNames().contains(roleType)){
			List<String> roleAuthority = roleProperties.getPropertiesByRoleName(user.getRoleType());
			
			for(var authority : roleAuthority){
				grantedAuthorityList.add(new SimpleGrantedAuthority(authority));
			}
		}
		
			return grantedAuthorityList;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return  getGrantedAuthorityList(user.getRoleType());
	}
	
	@Override
	public String getPassword() {
		return this.user.getPassword();
	}
	
	@Override
	public String getUsername() {
		return this.user.getUserName();
	}
	
	//Rest not used right now but required
	//Provides more complex authentication
	
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
}
