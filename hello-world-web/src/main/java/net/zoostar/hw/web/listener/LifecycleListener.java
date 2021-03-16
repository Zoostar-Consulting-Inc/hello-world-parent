package net.zoostar.hw.web.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class LifecycleListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext context = sce.getServletContext();
		sce.getServletContext().log("Starting Web App Hello World Web at context path: " + context.getContextPath());
		sce.getServletContext().log("Started Web App Hello World Web.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		sce.getServletContext().log("Shutting down Web App Hello World Web...");
		sce.getServletContext().log("Web App Hello World Web Shutdown.");
	}

}
