package net.zoostar.hw.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;

class SourceTest {

	@Test
	void test() {
		var expected = new Source();
		expected.setBaseUrl("baseUrl");
		expected.setId(UUID.randomUUID().toString());
		expected.setSourceCode("sourceCode");
		assertThat(expected.isNew()).isFalse();
		
		var duplicate = expected;
		assertThat(duplicate).isEqualTo(expected);
		assertThat(duplicate.hashCode()).hasSameHashCodeAs(expected.hashCode());
		
		var actual = new Source();
		actual.setBaseUrl("baseUrl2");
		actual.setSourceCode("sourceCode");
		assertThat(actual).isEqualTo(expected);
		assertThat(actual.hashCode()).hasSameHashCodeAs(expected.hashCode());
		assertThat(actual.isNew()).isTrue();
		
		actual.setSourceCode("sourceCode2");
		assertThat(actual).isNotEqualTo(expected);
	}

}
