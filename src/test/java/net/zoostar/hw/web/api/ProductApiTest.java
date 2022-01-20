package net.zoostar.hw.web.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.repository.ProductRepository;
import net.zoostar.hw.web.request.ProductRequest;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
class ProductApiTest {

	@Autowired
	protected MockMvc api;
	
	@Autowired
	ObjectMapper mapper;

	@MockBean
	private ProductRepository entityRepository;
	
	@Test
	void testCreate() throws Exception {
		//given
		String url = "/api/product/create";
		var request = toProductRequest();
		
		//mock
		var entity = request.toEntity();
		entity.setId(UUID.randomUUID().toString());
		Mockito.when(entityRepository.save(request.toEntity())).
				thenReturn(entity);
		
		//when
		var result = api.perform(post(url).
				accept(MediaType.APPLICATION_JSON).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();
		
		//then
		assertNotNull(result);
		log.debug("Result: {}", result);
		
		var response = result.getResponse();
		assertNotNull(response);
		log.info("Response status: {}", response.getStatus());
		assertEquals(HttpStatus.CREATED.value(), response.getStatus());
		
		var actual = mapper.readValue(response.getContentAsString(), Product.class);
		assertNotNull(actual);
		log.info("Created entity: {}.", actual);
		var duplicate = actual;
		assertEquals(actual, duplicate);
		assertNotEquals(actual, null);
		assertEquals(entity.getClass(), actual.getClass());
		assertEquals(entity.hashCode(), actual.hashCode());
		assertEquals(entity.getId(), actual.getId());
		assertEquals(entity.getName(), actual.getName());
		assertEquals(entity.getSku(), actual.getSku());
		assertEquals(entity.getSource(), actual.getSource());
		assertEquals(entity.getSourceId(), actual.getSourceId());
		assertFalse(actual.isNew());
		assertTrue(request.toEntity().isNew());
		
		entity.setSourceId(actual.getSourceId().substring(0, actual.getSourceId().length()-1));
		assertNotEquals(entity, actual);
		entity.setSourceId(actual.getSourceId());
		entity.setSource(actual.getSource().substring(0, actual.getSource().length()-1));
		assertNotEquals(entity, actual);
		
		var duplicateProductRequest = new ProductRequest(actual);
		assertEquals(request, duplicateProductRequest);
		assertEquals(request.hashCode(), duplicateProductRequest.hashCode());
	}

	protected ProductRequest toProductRequest() {
		var request = new ProductRequest();
		request.setDesc("This is a product description");
		request.setSku("SKU");
		request.setSource("source");
		request.setSourceId("sourceId");
		return request;
	}

}
