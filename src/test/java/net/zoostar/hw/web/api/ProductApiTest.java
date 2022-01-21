package net.zoostar.hw.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import net.zoostar.hw.AbstractHelloWorldTestHarness;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.web.request.ProductRequest;

class ProductApiTest extends AbstractHelloWorldTestHarness {

	@Test
	void testCreate() throws Exception {
		//given
		String url = "/api/product/create";
		var request = toProductRequest();
		
		//mock
		var entity = request.toEntity();
		entity.setId(UUID.randomUUID().toString());
		Mockito.when(productRepository.save(request.toEntity())).
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
		assertThat(actual).isNotNull();
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
