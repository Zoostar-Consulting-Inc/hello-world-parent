package net.zoostar.hw.web.interceptor;

import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GatewayAuditInterceptor implements HandlerInterceptor {

	public static final String ISO_8601_STRING_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXX";
	protected static final SimpleDateFormat ISO_8601_DATE_FORMAT = new SimpleDateFormat(ISO_8601_STRING_FORMAT);
	
	private static final ThreadLocal<GatewayAudit> AUDIT = new ThreadLocal<>() {

		@Override
		protected GatewayAudit initialValue() {
			long time = System.currentTimeMillis();
			GatewayAudit audit = new GatewayAudit();
			audit.setTime(time);
			return audit;
		}
		
	};
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		GatewayAudit audit = AUDIT.get();
		audit.setEndPoint(request.getRequestURI());
		audit.setRemoteAddress(request.getRemoteAddr());
		log.info("Begin Gateway Audit of: {}", audit);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		GatewayAudit audit = AUDIT.get();
		audit.setDuration(System.currentTimeMillis() - audit.getTime());
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		log.info("Completed servicing request in: {} seconds", (AUDIT.get().getDuration() / 1000));
		AUDIT.remove();
	}

}
