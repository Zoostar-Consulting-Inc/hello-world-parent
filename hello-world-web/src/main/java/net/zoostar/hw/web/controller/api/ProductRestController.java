package net.zoostar.hw.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.ProductService;
import net.zoostar.hw.web.request.RequestProduct;

@Slf4j
@Setter
@Getter
@RestController
@RequestMapping(value="/secured/api/customer")
public class ProductRestController {
	
	@Autowired
	protected ProductService productManager;
	
	@PostMapping(consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> create(RequestProduct request) {
		log.info("Creating new record: {}...", request);
		return new ResponseEntity<>(productManager.create(request.toEntity()), HttpStatus.CREATED);
	}
}
