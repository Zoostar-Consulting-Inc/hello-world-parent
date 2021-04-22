package net.zoostar.hw.web.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.junit.function.ThrowingRunnable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.Mockito;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import net.zoostar.hw.web.AbstractMockTestHarness;

class AuthenticationProviderImplTest extends AbstractMockTestHarness {

	UsernamePasswordAuthenticationToken authentication;
	AuthenticationProviderImpl authenticationProvider;
	
	@BeforeEach
	protected void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		super.beforeEach(test);
		authenticationProvider = new AuthenticationProviderImpl();
		authenticationProvider.setUserManager(userManager);
	}
	
	@Test
	void testAuthenticateSuccess() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String email = "user1@email.com";
		String password = "Hello!23";
		authentication = new UsernamePasswordAuthenticationToken(email, password);
		assertTrue(authenticationProvider.supports(authentication.getClass()));
		
		//WHEN
		Mockito.when(userManager.getRepository().
				findByEmail(email)).thenReturn(entity(0, 1));
		Authentication authenticated = authenticationProvider.authenticate(authentication);
		
		//THEN
		assertNotNull(authenticated);
		assertEquals(email, authenticated.getName());
		assertEquals(password, authenticated.getCredentials().toString());
	}
	
	@Test
	void testAuthenticateUserNotFound() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String email = "user1@email.com";
		authentication = new UsernamePasswordAuthenticationToken(email, "Hello!23");
		assertTrue(authenticationProvider.supports(authentication.getClass()));
		
		//WHEN
		Mockito.when(userManager.getRepository().
				findByEmail(email)).thenReturn(null);
		final ThrowingRunnable throwingRunnable = () -> authenticationProvider.authenticate(authentication);
		
		//THEN
		Assert.assertThrows(BadCredentialsException.class, throwingRunnable);
	}
	
	@Test
	void testAuthenticateIncorrectPassword() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		String email = "user1@email.com";
		authentication = new UsernamePasswordAuthenticationToken(email, "Hello123");
		assertTrue(authenticationProvider.supports(authentication.getClass()));
		
		//WHEN
		Mockito.when(userManager.getRepository().
				findByEmail(email)).thenReturn(entity(0, 1));
		final ThrowingRunnable throwingRunnable = () -> authenticationProvider.authenticate(authentication);
		
		//THEN
		Assert.assertThrows(BadCredentialsException.class, throwingRunnable);
	}

}
