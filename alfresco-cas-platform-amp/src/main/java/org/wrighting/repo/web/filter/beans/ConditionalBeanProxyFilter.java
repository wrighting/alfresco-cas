package org.wrighting.alfresco.repo.web.filter.beans;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.alfresco.repo.web.filter.beans.BeanProxyFilter;
import org.alfresco.repo.web.filter.beans.DependencyInjectedFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wrighting.alfresco.web.app.servlet.ConditionalFilterEvaluator;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class ConditionalBeanProxyFilter extends BeanProxyFilter {

	private static Log logger = LogFactory.getLog(ConditionalBeanProxyFilter.class);
	private static final String INNER_FILTER_CLASS = "filterClass";
	private static final String EVALUATOR_BEAN = "evaluatorBeanName";
   /**
     * Name of the init parameter that carries the proxied bean name 
     */
    private static final String INIT_PARAM_BEAN_NAME = "beanName";

	private Filter innerFilter;

	private DependencyInjectedFilter innerDIFilter;

	private ServletContext context;

	private boolean beanProxyFilter = true;
	private ConditionalFilterEvaluator evaluator;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (evaluator.isEnabled(context, request, response)) {
			if (beanProxyFilter) {
				innerDIFilter.doFilter(this.context, request, response, chain);
			} else {
				innerFilter.doFilter(request, response, chain);
			}

		} else {
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		context = filterConfig.getServletContext();
		WebApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);
		
		String beanName = filterConfig.getInitParameter(INIT_PARAM_BEAN_NAME);
		if (beanName != null) {
			  innerDIFilter = (DependencyInjectedFilter)ctx.getBean(beanName);
		} else {
			beanProxyFilter = false;
		}
		

		try {
			logger.info("Loading inner filter: " + filterConfig.getInitParameter(INNER_FILTER_CLASS));
			if (beanProxyFilter) {
				innerDIFilter = (DependencyInjectedFilter) Class
						.forName(filterConfig.getInitParameter(INNER_FILTER_CLASS)).newInstance();
			} else {
				innerFilter = (Filter) Class.forName(filterConfig.getInitParameter(INNER_FILTER_CLASS)).newInstance();
				innerFilter.init(filterConfig);
			}
		} catch (InstantiationException | RuntimeException | ClassNotFoundException | IllegalAccessException e) {
			logger.error("exception while creating inner filter", e);
			throw new RuntimeException(e);
		}

		innerFilter.init(filterConfig);

		try {
			evaluator = (ConditionalFilterEvaluator) ctx.getBean(filterConfig.getInitParameter(EVALUATOR_BEAN));
		} catch (RuntimeException e) {
			logger.error("exception while creating evaluator", e);
			throw new RuntimeException(e);
		}

	}
}
