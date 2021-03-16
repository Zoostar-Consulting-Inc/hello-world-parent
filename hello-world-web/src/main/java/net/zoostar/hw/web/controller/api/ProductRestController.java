package net.zoostar.hw.web.controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.exception.EntityAlreadyExistsException;
import net.zoostar.hw.exception.EntityNotFoundException;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.ProductService;

@Slf4j
@Getter
@ToString
@RestController
@NoArgsConstructor
@RequestMapping(value="/api/product")
public class ProductRestController {

	@Autowired
	protected ProductService productManager;
	
	public ProductRestController(ProductService productManager) {
		this.productManager = productManager;
	}

	/**
	 * 
	 * @param number Page number with a 0 based index
	 * @param limit Records to fetch per page
	 * @return
	 */
	@GetMapping(value="/retrieve/products.json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Page<Product>> retrieveProducts(@RequestParam Integer number, @RequestParam Integer limit) {
		log.debug("retrieveProducts(number:{}, limit:{})", number, limit);
		return new ResponseEntity<>(getProductManager().retrieve(number, limit), HttpStatus.OK);
	}

	@GetMapping(value="/product.json", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> retrieveProduct(@RequestParam String assetId, @RequestParam String sku, @RequestParam String source) {
		log.debug("retrieveProduct(assetId:{}, sku:{}, source:{})", assetId, sku, source);
		Product product = null;
		HttpStatus status = HttpStatus.OK;
		
		try {
			product = getProductManager().retrieve(assetId, sku, source);
		} catch (EntityNotFoundException e) {
			log.warn(e.getMessage());
			status = HttpStatus.EXPECTATION_FAILED;
		}
		
		return new ResponseEntity<>(product, status);
	}

	@PostMapping(value="/create.json", consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> create(@RequestBody Product product) {
		ResponseEntity<Product> response = null;
		try {
			response = new ResponseEntity<>(getProductManager().create(product), HttpStatus.OK);
		} catch (EntityAlreadyExistsException e) {
			log.warn(e.getMessage());
			response = new ResponseEntity<>(product, HttpStatus.EXPECTATION_FAILED);
		}
		return response;
	}
}
