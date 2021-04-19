package net.zoostar.hw.web.controller.api.secured;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@Controller
@NoArgsConstructor
@RequestMapping(value="/secured/api/product")
public class ProductRestController {
	
	@GetMapping(value="/", produces=MediaType.TEXT_HTML_VALUE)
	public String loadSecuredPage(Map<String, Object> model) {
		log.info("{}", "Loading secured page...");
		return "product";
	}
}
