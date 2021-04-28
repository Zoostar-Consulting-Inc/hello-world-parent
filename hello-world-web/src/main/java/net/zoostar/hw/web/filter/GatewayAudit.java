package net.zoostar.hw.web.filter;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class GatewayAudit {

	private String id;
	
	private long time;
	
	private long duration;
	
	private String endPoint;
	
	private String remoteAddress;

	private String username;
	
	public GatewayAudit(String id) {
		this.id = id;
	}
}
