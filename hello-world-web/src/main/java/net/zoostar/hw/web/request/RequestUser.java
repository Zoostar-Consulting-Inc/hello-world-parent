package net.zoostar.hw.web.request;

import lombok.ToString;
import net.zoostar.hw.model.User;

@ToString
public class RequestUser implements RequestEntity<User> {
	
	private User user;
	
	public RequestUser(String email) {
		user = new User(email);
	}
	
	public String getEmail() {
		return user.getEmail();
	}
	
	public void setName(String name) {
		user.setName(name);
	}
	
	public String getName() {
		return user.getName();
	}
	
	@Override
	public User toEntity() {
		return user;
	}
}
