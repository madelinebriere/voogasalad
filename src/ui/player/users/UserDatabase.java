package ui.player.users;

import java.util.ArrayList;
import java.util.Collection;

public class UserDatabase {
	
	private Collection<User> database = new ArrayList<User>();
	private Passwords passwords = new Passwords();
	private User activeUser = null;
	
	public User getActiveUser() {
		return activeUser;
	}
	
	public void setActiveUser(User user) {
		activeUser = user;
	}
	
	public Passwords getPasswords(){
		return passwords;
	}
	
	public Collection<User> getDatabase(){
		return database;
	}
	
	public void addUser(User newUser) {
		database.add(newUser);
	}
	
	public void deleteUser(User delete) {
		database.remove(delete);
	}
}