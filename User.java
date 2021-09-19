
public class User {
	private String username;
	private String firstName;
	private String lastName;
	private int donated;
	private String password; 
	private int avatar;
	
	public User(String username, String firstName,String lastName, String password, int donated, int avatar) {
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.donated = donated;
		this.password = password;
		this.avatar = avatar;
	}
	
	public String getUserName() {
		return username.isEmpty()?"":username;
	}
	
	public String getLastName() {
		return lastName.isEmpty()?"":lastName;
	}
	public String getFirstName() {
		return firstName.isEmpty()?"":firstName;
	}
	public int getDonated() {
		return donated;
	}
	public String getPassword() {
		return password;
	}
	public int getAvatar() {
		return avatar;
	}
}
