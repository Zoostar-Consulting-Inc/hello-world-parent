package net.zoostar.hw.web.interceptor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GatewayAudit {

	private long time;
	
	private double duration;
	
	private String endPoint;
	
	private String remoteAddress;
}
