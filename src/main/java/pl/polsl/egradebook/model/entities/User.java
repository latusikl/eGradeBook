package pl.polsl.egradebook.model.entities;



import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int userID;
	
	private String name;
	private String surname;
	
	@NotBlank(message = "Login is required!")
	private String login;
	@NotBlank(message = "Password is required!")
	private String password;
	
	@Override
	public String toString() {
		return "User{" + "userID=" + userID + ", name='" + name + '\'' + ", surname='" + surname + '\'' + ", login='" + login + '\'' + ", password='" + password + '\'' + '}';
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(String login) {
		this.login = login;
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
}
