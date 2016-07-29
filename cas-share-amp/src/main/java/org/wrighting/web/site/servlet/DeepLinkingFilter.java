package org.wrighting.web.site.servlet;

import static org.alfresco.web.site.SlingshotPageView.REDIRECT_QUERY;
import static org.alfresco.web.site.SlingshotPageView.REDIRECT_URI;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DeepLinkingFilter implements Filter {

	private static Log logger = LogFactory.getLog(DeepLinkingFilter.class);

	public static final String CAS_REDIRECT_URI = "_cas_redirectURI";
	public static final String CAS_REDIRECT_QUERY = "_cas_redirectQueryString";

	// From SSOAuthenticationFilter
	private static final String PAGE_SERVLET_PATH = "/page";
	private static final String LOGIN_PATH_INFORMATION = "/dologin";
	private static final String LOGIN_PARAMETER = "login";

	private static final String DEEP_LINK = "_deeplink";

	private boolean isLoginPage(final HttpServletRequest req) {
		String pathInfo;
		if (PAGE_SERVLET_PATH.equals(req.getServletPath())
				&& (LOGIN_PATH_INFORMATION.equals(pathInfo = req.getPathInfo())
						|| pathInfo == null && LOGIN_PARAMETER.equals(req.getParameter("pt")))) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;

		// The Alfresco SSOAuthenticationFilter will redirect to the login page,
		// putting the
		// actual destination into the session
		// Here we re-instate it
		final HttpServletRequestWrapper wrapped = new HttpServletRequestWrapper(request) {

			private Map<String, String[]> modifiableParameters = null;
			private Map<String, String[]> allParameters = null;

			@Override
			public String getRequestURI() {
				String ret = super.getRequestURI();
				HttpSession session = getSession(false);
				if (session != null && !isLoginPage(this) && session.getAttribute(REDIRECT_URI) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Changing request uri from:" + ret);
					}
					String rdu = (String) session.getAttribute(REDIRECT_URI);
					// ret = rdu;
					// session.removeAttribute(REDIRECT_URI);
					session.setAttribute(CAS_REDIRECT_URI, rdu);
				}

				if (session != null && !isLoginPage(this) && session.getAttribute(CAS_REDIRECT_URI) != null) {
					String rdu = (String) session.getAttribute(CAS_REDIRECT_URI);
					// session.setAttribute(REDIRECT_URI, rdu);
					session.removeAttribute(CAS_REDIRECT_URI);
					ret = rdu;
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
				if (session != null && session.getAttribute(REDIRECT_QUERY) != null) {
					if (logger.isDebugEnabled()) {
						logger.debug("Changing query string from:" + ret);
					}
					String rdq = (String) session.getAttribute(REDIRECT_QUERY);
					// ret = rdq;
					// session.removeAttribute(REDIRECT_QUERY);
					session.setAttribute(CAS_REDIRECT_QUERY, rdq);
				}
				if (session != null && !isLoginPage(this) && session.getAttribute(CAS_REDIRECT_QUERY) != null) {
					String rdq = (String) session.getAttribute(CAS_REDIRECT_QUERY);
					// session.setAttribute(REDIRECT_QUERY, rdq);
					session.removeAttribute(CAS_REDIRECT_QUERY);
					ret = rdq;
					modifiableParameters = new TreeMap<String, String[]>();
					try {
						final Map<String, List<String>> query_pairs = new LinkedHashMap<String, List<String>>();
						final String[] pairs = rdq.split("&");
						for (String pair : pairs) {
							final int idx = pair.indexOf("=");
							final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8") : pair;
							if (!query_pairs.containsKey(key)) {
								query_pairs.put(key, new LinkedList<String>());
							}
							final String value = idx > 0 && pair.length() > idx + 1
									? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
							query_pairs.get(key).add(value);
						}
						for (Entry<String, List<String>> entry : query_pairs.entrySet()) {
							String[] values = entry.getValue().toArray(new String[entry.getValue().size()]);
							modifiableParameters.put(entry.getKey(), values);
						}
					} catch (java.io.UnsupportedEncodingException uee) {

					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug("Query string is:" + ret);
				}
				return ret;
			}

			@Override
			public String getParameter(final String name) {
				String[] strings = getParameterMap().get(name);
				if (strings != null) {
					return strings[0];
				}
				return super.getParameter(name);
			}

			@SuppressWarnings("unchecked")
			@Override
			public Map<String, String[]> getParameterMap() {
				if (allParameters == null) {
					allParameters = new TreeMap<String, String[]>();
					if (modifiableParameters != null) {
						allParameters.putAll(modifiableParameters);
					} else {
						allParameters.putAll(super.getParameterMap());
					}
				}
				// Return an unmodifiable collection because we need to uphold
				// the interface contract.
				return Collections.unmodifiableMap(allParameters);
			}

			@Override
			public Enumeration<String> getParameterNames() {
				return Collections.enumeration(getParameterMap().keySet());
			}

			@Override
			public String[] getParameterValues(final String name) {
				return getParameterMap().get(name);
			}
		};

		HttpSession session = request.getSession(false);
		if (isLoginPage(request) || 
				(session != null && session.getAttribute(REDIRECT_QUERY) != null && session.getAttribute(DEEP_LINK) == null)) {
			if (!isLoginPage(request)) {
				session.setAttribute(DEEP_LINK, "linked");
			}
			chain.doFilter(wrapped, response);
		} else {
			chain.doFilter(req, response);
		}

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
