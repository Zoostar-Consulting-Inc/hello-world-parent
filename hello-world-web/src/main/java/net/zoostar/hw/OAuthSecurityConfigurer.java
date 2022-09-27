package net.zoostar.hw;
 
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
public class OAuthSecurityConfigurer extends WebSecurityConfigurerAdapter implements InitializingBean {

	public static final String PROFILE_SECURED = "secured";
	
	@Autowired
	private Environment environment;
	
	private List<String> activeProfiles;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("{}", "Configuring OAuth Security...");
		if(activeProfiles.contains(PROFILE_SECURED)) {
			http.authorizeRequests().antMatchers("/api/**").hasRole("USER").anyRequest().authenticated().and().csrf()
			.disable().oauth2Login();
		} else {
			http.authorizeRequests().antMatchers("/api/**").permitAll();
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		log.info("{}", "Determining profile to configure security...");
		String[] arr = environment.getActiveProfiles();
		activeProfiles = Arrays.asList(arr);
		log.info("Loading profiles: {}", activeProfiles);
		
	}

}
