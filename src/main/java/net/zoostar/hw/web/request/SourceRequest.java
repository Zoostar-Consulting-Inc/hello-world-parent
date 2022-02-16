package net.zoostar.hw.web.request;

import net.zoostar.hw.entity.PersistableEntityMapper;
import net.zoostar.hw.entity.Source;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class SourceRequest implements PersistableEntityMapper<Source, String> {

	private String sourceCode;
	
	private String baseUrl;

	public SourceRequest(Source source) {
		this.sourceCode = source.getSourceCode();
		this.baseUrl = source.getBaseUrl();
	}
	
	@Override
	public Source toEntity() {
		var entity = new Source();
		entity.setBaseUrl(baseUrl);
		entity.setSourceCode(sourceCode);
		return entity;
	}

}
