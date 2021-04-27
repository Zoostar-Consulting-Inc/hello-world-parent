package net.zoostar.hw.web.filter;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class GatewayAuditFilterChain extends AbstractRequestLoggingFilter {

	private static final List<String> EXCLUDED_ENDPOINTS = Arrays.asList(
			"/hw/login"
	);
	
	private boolean exclude;
	private final GatewayAudit auditor = new GatewayAudit();
	
	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		String endpoint = request.getRequestURI();
		exclude = EXCLUDED_ENDPOINTS.contains(endpoint);
		if(!exclude) {
			auditor.setEndPoint(endpoint);
			auditor.setRemoteAddress(request.getRemoteAddr());
			auditor.setUsername(request.getRemoteUser());
			log.info("Begin Gateway Audit of: {}", auditor);
			auditor.setTime(System.currentTimeMillis());
		}
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		if(!exclude) {
			auditor.setDuration(System.currentTimeMillis() - auditor.getTime());
			double time = (double)auditor.getDuration() / 1000;
			log.info("Completed servicing request in: {} seconds", time);
		}
	}

}
