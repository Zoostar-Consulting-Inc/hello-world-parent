package net.zoostar.hw;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class HelloWorldWeb extends SpringBootServletInitializer implements WebMvcConfigurer, InitializingBean {

	public static final String ACTIVE_PROFILES_NAME = "spring.profiles.active";
	public static final String CUSTOM_PROFILE_NAME = "custom.profile";
	public static final String DEFAULT_PROFILE_VALUE = "local";
	public static final String PROPERTY_DELIM_VALUE = ",";

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HelloWorldWeb.class);
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addRedirectViewController("/", "/swagger-ui/index.html");
		WebMvcConfigurer.super.addViewControllers(registry);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		System.setProperty(ACTIVE_PROFILES_NAME, parseSpringProfiles(System.getProperty(CUSTOM_PROFILE_NAME)));
		SpringApplication.run(HelloWorldWeb.class, args);
	}
	
	public static String parseSpringProfiles(String property) {
		StringBuilder lowerCaseProperty = new StringBuilder();
		if(StringUtils.hasText(property)) {
			String[] values = property.split(PROPERTY_DELIM_VALUE);
			for(String value : values) {
				lowerCaseProperty.append(value.trim().toLowerCase()).append(PROPERTY_DELIM_VALUE);
			}
		} else {
			lowerCaseProperty.append(DEFAULT_PROFILE_VALUE).append(PROPERTY_DELIM_VALUE);
		}
		return lowerCaseProperty.substring(0, lowerCaseProperty.length()-1);
	}

}
