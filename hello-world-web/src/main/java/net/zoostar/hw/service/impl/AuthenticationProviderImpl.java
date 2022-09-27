package net.zoostar.hw.service.impl;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthenticationProviderImpl implements AuthenticationProvider {

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		log.info("Authenticating: {}...", authentication.getPrincipal().toString());
		return null;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		log.info("{}", "Verify if OAuth2AuthenticationToken auth is supported.");
		return OAuth2AuthenticationToken.class.isAssignableFrom(authentication.getClass());
	}

}
