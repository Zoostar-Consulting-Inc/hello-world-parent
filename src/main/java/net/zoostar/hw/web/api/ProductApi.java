package net.zoostar.hw.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.service.ProductService;
import net.zoostar.hw.web.request.ProductRequest;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductApi {

	@Autowired
	protected ProductService entityManager;

	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> create(@RequestBody ProductRequest request) {
		log.info("Creating entity from request: {}", request);
		return new ResponseEntity<>(entityManager.create(request.toEntity()), HttpStatus.CREATED);
	}
}
