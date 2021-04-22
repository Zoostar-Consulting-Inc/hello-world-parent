package net.zoostar.hw.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.service.UserService;

@Slf4j
@Setter
public class AuthenticationProviderImpl implements AuthenticationProvider {

	@Autowired
	private UserService userManager;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken authenticated = null;
		String email = authentication.getName().trim();
		var cred = authentication.getCredentials().toString();
		log.info("Authenticating: {}...", email);
		try {
			var user = userManager.retrieveByEmail(email);
			if("Hello!23".equals(cred)) {
				authenticated = new UsernamePasswordAuthenticationToken(user.getEmail(), "Hello!23");
			} else {
				throw new BadCredentialsException("Provided password does not equal to \"Hello!23\"");
			}
		} catch (EntityNotFoundException e) {
			log.warn("No entity found for email: {}!", e.getMessage());
			throw new BadCredentialsException("No entity found for supplied email!");
		}
		return authenticated;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

}
