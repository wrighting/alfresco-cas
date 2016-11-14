package org.wrighting.alfresco.web.site.servlet;

import static org.alfresco.web.site.SlingshotPageView.REDIRECT_QUERY;
import static org.alfresco.web.site.SlingshotPageView.REDIRECT_URI;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeepLinkingFilter implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	private static Log logger = LogFactory.getLog(DeepLinkingFilter.class);

	public static final String CAS_REDIRECT_URI = "_cas_redirectURI";
	public static final String CAS_REDIRECT_QUERY = "_cas_redirectQueryString";

	// From SSOAuthenticationFilter
	private static final String PAGE_SERVLET_PATH = "/page";
	private static final String LOGIN_PATH_INFORMATION = "/dologin";
	private static final String LOGIN_PARAMETER = "login";
	private static final String LOGIN_PARAMETER_KEY = "pt";
	
	private boolean isLoginPage(final HttpServletRequest req) {
		String pathInfo = req.getPathInfo();
		String parameter = req.getParameter(LOGIN_PARAMETER_KEY);
		if (PAGE_SERVLET_PATH.equals(req.getServletPath())
				&& (LOGIN_PATH_INFORMATION.equals(pathInfo) || pathInfo == null && LOGIN_PARAMETER.equals(parameter))) {
			return true;
		} else {
			return false;
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		if (logger.isDebugEnabled()) {
			logger.debug("Filtering request uri from:" + request.getRequestURI());
		}
		// The Alfresco SSOAuthenticationFilter will redirect to the login page,
		// putting the
		// actual destination into the session
		// Here we re-instate it
		final HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request) {

			@Override
			public String getRequestURI() {
				String ret = super.getRequestURI();
				HttpSession session = getSession(false);
				if (session != null && isLoginPage(this) && session.getAttribute(REDIRECT_URI) != null) {
					String rdu = (String) session.getAttribute(REDIRECT_URI);
					if (logger.isDebugEnabled()) {
						logger.debug("Storing request uri from:" + ret + ":" + rdu);
					}
					session.setAttribute(CAS_REDIRECT_URI, rdu);
				}

				if (logger.isDebugEnabled()) {
					logger.debug("Request uri is:" + ret);
				}
				return ret;
			}

			@Override
			public String getQueryString() {
				HttpSession session = getSession(false);
				String ret = super.getQueryString();
				if (logger.isDebugEnabled()) {
					logger.debug("Query string is:" + ret);
				}
				if (session != null && session.getAttribute(REDIRECT_QUERY) != null) {
					String rdq = (String) session.getAttribute(REDIRECT_QUERY);
					session.setAttribute(CAS_REDIRECT_QUERY, rdq);
					if (logger.isDebugEnabled()) {
						logger.debug("Saving query string from:" + rdq);
					}
				}
				return ret;
			}
		};

		HttpSession session = request.getSession(false);
		if (session != null && logger.isDebugEnabled()) {
			logger.debug("Session REDIRECT_QUERY:" + session.getAttribute(REDIRECT_QUERY));
			logger.debug("Session CAS_REDIRECT_URI:" + session.getAttribute(CAS_REDIRECT_URI));
		}
		if (isLoginPage(request)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Handling login request");
			}
			chain.doFilter(wrapped, response);
		} else if (session != null && session.getAttribute(CAS_REDIRECT_URI) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Handling post login request");
			}

			String redirectUrl = session.getAttribute(CAS_REDIRECT_URI)
					+ (session.getAttribute(CAS_REDIRECT_QUERY) != null
							? ("?" + session.getAttribute(CAS_REDIRECT_QUERY)) : "");
			if (logger.isDebugEnabled()) {
				logger.debug("Redirecting to:" + redirectUrl);
			}
			session.removeAttribute(CAS_REDIRECT_URI);
			session.removeAttribute(CAS_REDIRECT_QUERY);
			((HttpServletResponse) response).sendRedirect(redirectUrl);
		} else {
			chain.doFilter(req, response);
		}

	}

}
