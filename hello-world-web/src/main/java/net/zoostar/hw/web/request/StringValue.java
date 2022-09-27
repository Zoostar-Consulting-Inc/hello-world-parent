package net.zoostar.hw.web.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StringValue {

	private String value;

	public StringValue(String value) {
		this.value = value;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
