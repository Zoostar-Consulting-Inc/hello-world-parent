package net.zoostar.hw.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Optional;
import java.util.UUID;

import net.zoostar.hw.AbstractHelloWorldTestHarness;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.repository.EntityRepository;
import net.zoostar.hw.web.request.ProductRequest;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

class ProductApiTest extends AbstractHelloWorldTestHarness<EntityRepository<Product, String>, Product, String> {

	@Test
	void testCreate() throws Exception {
		//given
		var request = toProductRequest("source", "sourceId");
		String url = "/api/product/update/" + request.getSource() + "?sourceId=" + request.getSourceId();
		
		//mock-when
		var entity = request.toEntity();
		entity.setId(UUID.randomUUID().toString());
		when(repository.save(request.toEntity())).
				thenReturn(entity);
		
		Source source = source(request.getSource());
		assertThat(source.isNew()).isFalse();
		assertThat(source.hashCode()).isNotZero();
		
		when(sourceRepository.findBySourceCode(request.getSource())).
				thenReturn(Optional.of(source));
		when(rest.getForEntity(source.getBaseUrl() + source.getEndPoint() + "?id=" + request.getSourceId(), ProductRequest.class)).
				thenReturn(new ResponseEntity<>(request, HttpStatus.OK));
		
		var result = api.perform(get(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();
		
		//then
		assertThat(result).isNotNull();
		log.debug("Result: {}", result);
		
		var response = result.getResponse();
		assertThat(response).isNotNull();
		log.debug("Response status: {}", response.getStatus());
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
		assertThat(actual.getSourceCode()).isEqualTo(entity.getSourceCode());
		assertThat(actual.getSourceId()).isEqualTo(entity.getSourceId());
		assertThat(actual.isNew()).isFalse();
		assertThat(request.toEntity().isNew()).isTrue();
		
		entity.setSourceId(actual.getSourceId().substring(0, actual.getSourceId().length()-1));
		assertThat(actual).isNotEqualTo(entity);

		entity.setSourceId(actual.getSourceId());
		entity.setSourceCode(actual.getSourceCode().substring(0, actual.getSourceCode().length()-1));
		assertThat(actual).isNotEqualTo(entity);
	}

	@Test
	void testUpdate() throws Exception {
		//given
		var request = toProductRequest("source", "sourceId");
		String url = "/api/product/update/" + request.getSource() + "?sourceId=" + request.getSourceId();
		
		//mock-when
		var entity = request.toEntity();
		entity.setId(UUID.randomUUID().toString());
		
		var persistable = toProductRequest(request.getSource(), request.getSourceId());
		persistable.setDesc(persistable.getDesc() + "_update");
		
		var updatedEntity = persistable.toEntity();
		updatedEntity.setId(entity.getId());

		var source = source(request.getSource());
		assertThat(source.isNew()).isFalse();
		
		when(repository.existsBySourceCodeAndId(request.getSource(), request.getSourceId())).
				thenReturn(true);
		when(sourceRepository.findBySourceCode(request.getSource())).
				thenReturn(Optional.of(source));
		when(rest.getForEntity(source.getBaseUrl() + source.getEndPoint() + "?id=" + request.getSourceId(), ProductRequest.class)).
				thenReturn(new ResponseEntity<>(persistable, HttpStatus.OK));
		when(repository.findBySourceCodeAndId(request.getSource(), request.getSourceId())).
				thenReturn(Optional.of(entity));
		when(repository.save(updatedEntity)).
				thenReturn(updatedEntity);
		
		var result = api.perform(get(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request))).
				andReturn();
		
		//then
		assertThat(result).isNotNull();
		log.debug("Result: {}", result);
		
		var response = result.getResponse();
		assertThat(response).isNotNull();
		log.debug("Response status: {}", response.getStatus());
		assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
		
		var actual = mapper.readValue(response.getContentAsString(), Product.class);
		assertThat(actual).isNotNull();
		log.info("Updated entity: {}.", actual);
		
		var duplicate = actual;
		assertThat(duplicate).isEqualTo(actual);
		assertThat(actual.getClass()).isEqualTo(updatedEntity.getClass());
		assertThat(actual).hasSameHashCodeAs(updatedEntity);
		assertThat(actual.getId()).isEqualTo(updatedEntity.getId());
		assertThat(actual.getName()).isEqualTo(updatedEntity.getName());
		assertThat(actual.getSku()).isEqualTo(updatedEntity.getSku());
		assertThat(actual.getSourceCode()).isEqualTo(updatedEntity.getSourceCode());
		assertThat(actual.getSourceId()).isEqualTo(updatedEntity.getSourceId());
		assertThat(actual.isNew()).isFalse();
		assertThat(request.toEntity().isNew()).isTrue();
	}
	
	protected ProductRequest toProductRequest(String sourceCode, String sourceId) {
		var request = new ProductRequest();
		request.setDesc("This is a product description");
		request.setSku("SKU");
		request.setSource(sourceCode);
		request.setSourceId(sourceId);
		log.info("Created ProductMapper: {}", request.toString());
		return request;
	}

	private Source source(String sourceCode) {
		var source = new Source();
		source.setBaseUrl("/" + sourceCode);
		source.setEndPoint("/product/retrieve");
		source.setId(UUID.randomUUID().toString());
		source.setSourceCode(sourceCode);
		return source;
	}

}
