package net.zoostar.hw.api.transformer.impl;

import lombok.AllArgsConstructor;
import lombok.ToString;
import net.zoostar.common.Transformer;

@ToString
@AllArgsConstructor
public class GreetingResponseTransformer implements Transformer<GreetingResponse> {

	private final String message;
	
	@Override
	public GreetingResponse transform() {
		return new GreetingResponse(message);
	}

}
