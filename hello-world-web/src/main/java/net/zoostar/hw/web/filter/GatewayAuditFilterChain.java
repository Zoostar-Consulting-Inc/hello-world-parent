package net.zoostar.hw.web.filter;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Component
public class GatewayAuditFilterChain extends AbstractRequestLoggingFilter {

	private final GatewayAudit auditor = new GatewayAudit();
	
	@Override
	protected void beforeRequest(HttpServletRequest request, String message) {
		auditor.setEndPoint(request.getRequestURI());
		auditor.setRemoteAddress(request.getRemoteAddr());
		auditor.setUsername(request.getRemoteUser());
		log.info("Begin Gateway Audit of: {}", auditor);
		auditor.setTime(System.currentTimeMillis());
	}

	@Override
	protected void afterRequest(HttpServletRequest request, String message) {
		auditor.setDuration(System.currentTimeMillis() - auditor.getTime());
		double time = (double)auditor.getDuration() / 1000;
		log.info("Completed servicing request in: {} seconds", time);
	}

}
