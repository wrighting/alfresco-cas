package org.wrighting.alfresco.repo.web.filter.beans;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class IgnoreFlagFilter implements Filter {

	private static Log logger = LogFactory.getLog(IgnoreFlagFilter.class);
	
	public static final String IGNORE_ATTRIBUTE = "filter.ignore";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setAttribute(IGNORE_ATTRIBUTE, true);
		if ( logger.isDebugEnabled()) {
             logger.debug("Ignore filter set for " + ((HttpServletRequest)request).getRequestURI());
         }
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		
	}

}
