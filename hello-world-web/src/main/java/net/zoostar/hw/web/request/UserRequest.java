package net.zoostar.hw.web.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserRequest {
	private String email;
	private String firstName;
	private String lastName;
}
