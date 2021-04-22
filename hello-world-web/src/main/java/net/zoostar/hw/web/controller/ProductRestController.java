package net.zoostar.hw.web.controller;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping(value="/secured/api/product")
public class ProductRestController {
	
	@GetMapping(value="/page.json", produces=MediaType.TEXT_HTML_VALUE)
	public ModelAndView loadSecuredPage(Map<String, Object> model) {
		log.info("{}", "Loading secured page...");
		return new ModelAndView("product", model);
	}
}
