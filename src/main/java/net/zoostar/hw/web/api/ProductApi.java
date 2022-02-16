package net.zoostar.hw.web.api;

import net.zoostar.hw.entity.Product;
import net.zoostar.hw.entity.SourceEntityMapper;
import net.zoostar.hw.web.request.ProductRequest;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/product")
public class ProductApi extends AbstractSourceApi<Product, String> {

	@Override
	@GetMapping(path = "/update/{sourceCode}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Product> cudOperation(@PathVariable String sourceCode, @RequestParam String sourceId) {
		return super.cudOperation(sourceCode, sourceId);
	}

	@Override
	protected Class<? extends SourceEntityMapper<Product, String>> getEntityMapperClazz() {
		return ProductRequest.class;
	}

	@Override
	protected String getEndPoint() {
		return "/product/retrieve";
	}
}
