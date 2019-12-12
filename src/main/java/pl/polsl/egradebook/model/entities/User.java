package pl.polsl.egradebook.model.entities;





import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 User entity.
 Due to password hashing required is to add users via server and "/user/add" URL.
 */
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userID;
	
	//Basic information about user
	@NotNull
	private String name;
	@NotNull
	private String surname;
	@NotNull
	private String email;
	
	
	@NotBlank(message = "Login is required!")
	private String userName;
	@NotBlank(message = "Password is required!")
	private String password;
	@NotBlank(message = "Role type is required!")
	private String roleType;
	
	@Override
	public String toString() {
		return "User{" + "userID=" + userID + ", name='" + name + '\'' + ", surname='" + surname + '\'' + ", email='" + email + '\'' + ", login='" + userName + '\'' + ", password='" + password + '\'' + ", roleType=" + roleType + '}';
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String login) {
		this.userName = login;
	}
	
	public int getUserID() {
		return userID;
	}
	
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getRoleType() {
		return roleType;
	}
	
	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}
}
