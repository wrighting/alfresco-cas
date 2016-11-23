package org.wrighting.alfresco.web.app.servlet;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public interface ConditionalFilterEvaluator {

	public void init(FilterConfig fc);
	
	public boolean isEnabled(ServletContext context, ServletRequest req, ServletResponse resp);
	
}
