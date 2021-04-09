package net.zoostar.hw.web.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ActiveProfiles({"test"})
//@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml"})
class IndexControllerTest {

	@Autowired
	IndexController controller;
	
	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

//		controller = new IndexController();
//		controller.setEnvValue("dev");
//		controller.setAppName("Hello World");
	}

	@Test
	void testLoadHomePage() throws ServletException {
		//GIVEN
		Map<String, Object> params = new HashMap<String, Object>();
		
		//WHEN
		ModelAndView modelAndView = controller.loadHomePage(params);
		
		//THEN
		assertNotNull(modelAndView);
		assertEquals("index", modelAndView.getViewName());
		assertEquals("test", modelAndView.getModel().get("env"));
		assertEquals("Hello World", modelAndView.getModel().get("message"));
	}

}
