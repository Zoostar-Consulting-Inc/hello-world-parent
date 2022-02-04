package net.zoostar.hw.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class SourceTest {

	@Test
	void test() {
		var expected = new Source();
		expected.setBaseUrl("baseUrl");
		expected.setEndPoint("endPoint");
		expected.setId(UUID.randomUUID().toString());
		expected.setSourceCode("sourceCode");
		
		var duplicate = expected;
		assertThat(duplicate).isEqualTo(expected);
		assertThat(duplicate.hashCode()).isEqualTo(expected.hashCode());
		
		var actual = new Source();
		actual.setBaseUrl("baseUrl2");
		actual.setEndPoint("endPoint2");
		actual.setId(UUID.randomUUID().toString());
		actual.setSourceCode("sourceCode");
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.hashCode()).isEqualTo(expected.hashCode());
		
		actual.setSourceCode("sourceCode2");
		assertThat(actual).isNotEqualTo(expected);
	}

}
