package net.zoostar.hw.web.api;

import static org.assertj.core.api.Assertions.assertThat;
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
		assertThat(result).isNotNull();
		log.debug("Result: {}", result);
		
		var response = result.getResponse();
		assertThat(response).isNotNull();;
		log.info("Response status: {}", response.getStatus());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
		var actual = mapper.readValue(response.getContentAsString(), Product.class);
		assertThat(actual).isNotEqualTo(null);
		log.info("Created entity: {}.", actual);
		
		var duplicate = actual;
		assertThat(duplicate).isEqualTo(actual);
		assertThat(actual.getClass()).isEqualTo(entity.getClass());
		assertThat(actual).hasSameHashCodeAs(entity);
		assertThat(actual.getId()).isEqualTo(entity.getId());
		assertThat(actual.getName()).isEqualTo(entity.getName());
		assertThat(actual.getSku()).isEqualTo(entity.getSku());
		assertThat(actual.getSource()).isEqualTo(entity.getSource());
		assertThat(actual.getSourceId()).isEqualTo(entity.getSourceId());
		assertThat(actual.isNew()).isFalse();
		assertThat(request.toEntity().isNew()).isTrue();
		
		entity.setSourceId(actual.getSourceId().substring(0, actual.getSourceId().length()-1));
		assertThat(actual).isNotEqualTo(entity);

		entity.setSourceId(actual.getSourceId());
		entity.setSource(actual.getSource().substring(0, actual.getSource().length()-1));
		assertThat(actual).isNotEqualTo(entity);
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
