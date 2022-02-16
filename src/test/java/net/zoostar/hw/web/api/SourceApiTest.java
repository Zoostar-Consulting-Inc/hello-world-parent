package net.zoostar.hw.web.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.UUID;

import net.zoostar.hw.AbstractHelloWorldTestHarness;
import net.zoostar.hw.entity.Product;
import net.zoostar.hw.entity.Source;
import net.zoostar.hw.service.SourceService;
import net.zoostar.hw.web.request.SourceRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

class SourceApiTest extends AbstractHelloWorldTestHarness<Source, String> {

	@Autowired
	protected SourceService<Product, String> sourceManager;
	
	@Test
	void testCreate() throws Exception, Exception {
		//given
		var request = new SourceRequest(toSource("source"));
		String url = "/api/source/create";

		//mock-when
		var entity = request.toEntity();
		entity.setId(UUID.randomUUID().toString());
		when(sourceRepository.save(request.toEntity())).
				thenReturn(entity);
		
		var result = service.perform(MockMvcRequestBuilders.post(url).
				contentType(MediaType.APPLICATION_JSON).
				content(mapper.writeValueAsString(request)).
				accept(MediaType.APPLICATION_JSON)).
				andReturn();
		
		//then
		assertThat(result).isNotNull();
		var response = result.getResponse();
		assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
		
		var actual = mapper.readValue(response.getContentAsByteArray(), Source.class);
		assertThat(actual).isNotNull();
	}
}