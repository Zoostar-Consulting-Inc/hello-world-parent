package net.zoostar.hw.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.User;
import net.zoostar.hw.service.UserService;

@Slf4j
@Setter
public class AuthenticationProviderImpl implements AuthenticationProvider {

	private static final String PASSWORD = "Hello!23";
	
	@Autowired
	private UserService userManager;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authenticated = null;
		String email = authentication.getName().trim();
		String password = authentication.getCredentials().toString();
		log.info("Authenticating: {}...", email);
		try {
			User user = userManager.retrieveByEmail(email);
			if(PASSWORD.equals(password)) {
				authenticated = new UsernamePasswordAuthenticationToken(user.getEmail(), PASSWORD);
			} else {
				throw new BadCredentialsException("Provided password does not equal to \" " + PASSWORD + "\"");
			}
		} catch (EntityNotFoundException e) {
			log.warn("No entity found for email: {}", e.getMessage());
			throw new UsernameNotFoundException(e.getMessage());
		}
		return authenticated;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
