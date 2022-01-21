package net.zoostar.hw.web.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.service.ProductService;
import net.zoostar.hw.web.request.ProductRequest;

@Getter
@RestController
@RequestMapping("/api/product")
public class ProductApi extends AbstractApi<Product, String> {

	@Autowired
	protected ProductService entityManager;

	@PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> create(@RequestBody ProductRequest request) {
		return super.create(request);
	}
}
