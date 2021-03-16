package net.zoostar.hw.web.controller.api;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.StringUtils;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.dao.hsql.ProductRepository;
import net.zoostar.hw.model.Product;
import net.zoostar.hw.service.impl.ProductServiceImpl;

@Slf4j
@ActiveProfiles({"test"})
@ExtendWith(MockitoExtension.class)
@ContextConfiguration(locations = {"classpath:META-INF/applicationContext-test.xml"})
class ProductRestControllerTest {
	
	static final int PAGE_LIMIT = 3;
	
	List<Product> totalProducts;
	
	@Mock
	ProductRepository repository;
	
	ProductRestController controller;

	@BeforeEach
	public void beforeEach(TestInfo test) throws JsonParseException, JsonMappingException, IOException {
		System.out.println();
		log.info("Executing test: [{}]...", test.getDisplayName());

		totalProducts = Collections.unmodifiableList(new ObjectMapper().readValue(
				new ClassPathResource("data/products.json").getInputStream(), new TypeReference<List<Product>>() { }));
		log.info("Total number of products loaded: {}", totalProducts.size());

		controller = new ProductRestController(
				new ProductServiceImpl(repository));
	}
	
	@Test
	void testCreate() {
		//GIVEN
		String id = UUID.randomUUID().toString();
		Product product = productKey("assetId1", "sku1", "MDM");
		
		//WHEN
		Mockito.when(controller.getProductManager().getRepository().save(product)).
				thenReturn(savedProduct(id, product));
		
		//THEN
		ResponseEntity<Product> actualResponse = controller.create(product);
		assertNotNull(actualResponse);
		Product entity = actualResponse.getBody();
		assertNotNull(entity);
		log.info("Retrieved product: {}", entity);
		assertEquals(id, entity.getId());
		assertFalse(entity.isNew());
	}

	protected Product savedProduct(String id, Product product) {
		Product entity = productKey(product.getAssetId(), product.getSku(), product.getSource());
		entity.setId(id);
		return entity;
	}

	protected Product productKey(String assetId, String sku, String source) {
		Product product = new Product();
		product.setAssetId(assetId);
		product.setSku(sku);
		product.setSource(source);
		product.setDescription("This is a Product with assetID1, sku1 and source MDM");
		product.setItemCount(1);
		product.setSourceId("mdm-source-1");
		product.setType("KIT");
		return product;
	}

	@Test
	void testRetrieveFailureForInvalidPageNumber() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = -1;
		
		//WHEN
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.retrieveProducts(number, PAGE_LIMIT),
				"Expected failure for Invalid Page Number");
		
		//THEN
		assertEquals("Page number may not be less than 0!", exception.getMessage());
	}

	@Test
	void testRetrieveFailureForInvalidPageLimit() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = 1;
		int limit = 0;
		
		//WHEN
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> controller.retrieveProducts(number, limit),
				"Expected failure for Invalid Page Limit");
		
		//THEN
		assertEquals("Page size limit has to be greater than 0!", exception.getMessage());
	}
	
	@Test
	void testRetrieveFirstPage() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int firstPage = 0;
		PageRequest request = PageRequest.of(firstPage, PAGE_LIMIT);
		Page<Product> expectedPage = page(request, PAGE_LIMIT, totalProducts.size());
		
		//WHEN
		Mockito.when(controller.getProductManager().getRepository().
				findAll(request)).thenReturn(expectedPage);
		
		//THEN
		ResponseEntity<Page<Product>> actualResponse = controller.retrieveProducts(firstPage, PAGE_LIMIT);
		assertNotNull(actualResponse);
		log.info("Response: {}", actualResponse);
		
		Page<Product> page = actualResponse.getBody();
		assertNotNull(page);
		
		log.info("Max total pages: {}", page.getTotalPages());
		log.info("This page number: {}", (page.getNumber() + 1));
		log.info("Total elements from all pages: {}", page.getTotalElements());
		log.info("Number of elements in this page: {}", page.getNumberOfElements());
		assertEquals((firstPage), page.getNumber(), "Actual Page Number: " + page.getNumber());
		assertEquals(PAGE_LIMIT, page.getSize(), "Actual Page Size: " + page.getSize());
		assertTrue(page.isFirst());
		assertFalse(page.isLast());
		
		List<Product> products = page.getContent();
		assertEquals(expectedPage.getNumberOfElements(), page.getNumberOfElements());
		
		int i = expectedPage.getNumber() * PAGE_LIMIT;
		for(Product actualProduct : products) {
			String id = String.valueOf(++i);
			assertNotNull(actualProduct);
			log.info("Retrieved product: {}", actualProduct);
			assertEquals(id, actualProduct.getId());
			assertTrue(StringUtils.isNotBlank(actualProduct.getId()));
			Product expectedProduct = createExistingProduct(String.valueOf(id), "assetId" + id, "sku" + id, "MDM", "sourceId" + id, "KIT", 1, "");
			assertEquals(expectedProduct, actualProduct);
			assertEquals(expectedProduct.hashCode(), actualProduct.hashCode());
		}
	}
	
	@Test
	void testRetrieveLastPage() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN
		int number = 3;
		PageRequest request = PageRequest.of(number, PAGE_LIMIT);
		Page<Product> expectedPage = page(request, PAGE_LIMIT, totalProducts.size());
		
		//WHEN
		Mockito.when(controller.getProductManager().getRepository().
				findAll(PageRequest.of(number, PAGE_LIMIT))).thenReturn(expectedPage);
		
		//THEN
		ResponseEntity<Page<Product>> actualResponse = controller.retrieveProducts(number, PAGE_LIMIT);
		assertNotNull(actualResponse);
		log.info("Response: {}", actualResponse);
		
		Page<Product> page = actualResponse.getBody();
		assertNotNull(page);
		
		log.info("Max total pages: {}", page.getTotalPages());
		log.info("This page number: {}", (page.getNumber() + 1));
		log.info("Total elements from all pages: {}", page.getTotalElements());
		log.info("Number of elements in this page: {}", page.getNumberOfElements());
		assertEquals(number, page.getNumber(), "Actual Page Number: " + page.getNumber());
		assertEquals(PAGE_LIMIT, page.getSize(), "Actual Page Size: " + page.getSize());
		assertFalse(page.isFirst());
		assertTrue(page.isLast());
		
		List<Product> products = page.getContent();
		assertEquals(expectedPage.getNumberOfElements(), page.getNumberOfElements());
		
		int i = expectedPage.getNumber() * PAGE_LIMIT;
		for(Product actualProduct : products) {
			String id = String.valueOf(++i);
			assertNotNull(actualProduct);
			log.info("Retrieved product: {}", actualProduct);
			assertEquals(id, actualProduct.getId());
			assertTrue(StringUtils.isNotBlank(actualProduct.getId()));
			Product expectedProduct = createExistingProduct(String.valueOf(id), "assetId" + id, "sku" + id, "MDM", "sourceId" + id, "KIT", 1, "");
			assertEquals(expectedProduct, actualProduct);
			assertEquals(expectedProduct.hashCode(), actualProduct.hashCode());
		}
	}
	
	@Test
	void testRetrieveProductByKey() throws JsonParseException, JsonMappingException, IOException {
		//GIVEN {key}
		String assetId = "assetId1";
		String sku = "sku1";
		String source = "MDM";
		int firstPage = 0;
		PageRequest request = PageRequest.of(firstPage, 1);
		Optional<Product> expectedProduct = page(request, PAGE_LIMIT, totalProducts.size()).get().findFirst();
		
		//WHEN
		Mockito.when(controller.getProductManager().getRepository().
				findByAssetIdAndSkuAndSource(assetId, sku, source)).thenReturn(expectedProduct.get());
		
		//THEN
		ResponseEntity<Product> actualResponse = controller.retrieveProduct(assetId, sku, source);
		assertNotNull(actualResponse);
		log.info("Response: {}", actualResponse);
		
		Product product = actualResponse.getBody();
		assertNotNull(product);
		log.info("Retrieved product: {}.", product);
		assertEquals(sku, product.getSku());
		assertEquals(source, product.getSource());
	}

	protected Page<Product> page(PageRequest page, int limit, int size) throws JsonParseException, JsonMappingException, IOException {
		int from = page.getPageNumber() * limit;
		int to = from + limit;
		if(to > size) {
			to = from + (size % from);
		}
		log.info("Given page number {} and page size {}...", (page.getPageNumber() + 1), limit);
		log.info("Then return a total of {}/{} products from {} to {}.", (to - from), size, (from + 1), to);
		return new PageImpl<>(totalProducts.subList(from, to), page, totalProducts.size());
	}

	protected Product createExistingProduct(String id, String assetId,
			String sku,	String source, String sourceId, String type,
			int itemCount, String description) {
		Product product = createNewProduct(assetId, sku, source, sourceId, type, itemCount, description);
		product.setId(id);
		return product;
	}
	
	protected Product createNewProduct(String assetId, String sku,
			String source, String sourceId, String type,
			int itemCount, String description) {
		Product product = new Product();
		product.setAssetId(assetId);
		product.setSku(sku);
		product.setSource(source);
		product.setSourceId(sourceId);
		product.setType(type);
		product.setItemCount(itemCount);
		product.setDescription(description);
		return product;
	}

}
