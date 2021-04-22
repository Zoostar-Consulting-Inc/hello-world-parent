package net.zoostar.hw.web.filter;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GatewayAudit {

	private long time;
	
	private long duration;
	
	private String endPoint;
	
	private String remoteAddress;

	private String username;
}
