package net.zoostar.hw.web.controller.api;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.impl.ProductServiceImpl;
import net.zoostar.hw.web.AbstractMockTestHarness;
import net.zoostar.hw.web.request.RequestProduct;

@Slf4j
class ProductRestControllerTest extends AbstractMockTestHarness {
	
	@Mock
	protected ProductRepository repository;

	@InjectMocks
	protected ProductServiceImpl productManager;
	
	protected ProductRestController controller;

	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		super.beforeEach(test);
		
		controller = new ProductRestController();
		controller.setProductManager(productManager);
	}

	@Test
	void testCreate() {
		Product product = product(false, "sku", "name", "desc");
		assertTrue(product.isNew());
		Product entity = entity(UUID.randomUUID().toString(), product);
		assertFalse(entity.isNew());
		when(controller.getProductManager().getRepository().
				save(product)).thenReturn(entity);
		
		//GIVEN
		var request = new RequestProduct(product.getSku());
		request.setDesc(product.getDesc());
		request.setName(product.getName());

		//WHEN
		ResponseEntity<Product> response = controller.create(request);
		
		//THEN
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		product = response.getBody();
		assertFalse(StringUtils.isBlank(product.getId()));
		assertEquals(entity, product);
		assertNotEquals(product, null);
		assertEquals(entity.hashCode(), product.hashCode());
		assertEquals(entity.getId(), product.getId());
		assertEquals(entity.getDesc(), product.getDesc());
		assertEquals(entity.getName(), product.getName());
		assertEquals(entity.getSku(), product.getSku());
		log.info("Created new Product successfully: {}", product);

		assertEquals(request.getDesc(), product.getDesc());
		assertEquals(request.getName(), product.getName());
		assertEquals(request.getSku(), product.getSku());
	}

	protected Product entity(String id, Product product) {
		var entity = product(true, product.getSku(), 
				product.getName(), product.getDesc());
		entity.setId(id);
		return entity;
	}

	protected Product product(boolean useNoArgCtor, 
			String sku, String name, String desc) {
		Product product = null;
		if(useNoArgCtor) {
			product = new Product();
			product.setSku(sku);
		} else {
			product = new Product(name);
		}
		product.setName(name);
		product.setDesc(desc);
		return product;
	}

}
