package org.wrighting.alfresco.web.app.servlet;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.alfresco.repo.SessionUser;
import org.alfresco.repo.security.authentication.external.RemoteUserMapper;
import org.alfresco.repo.webdav.auth.BaseSSOAuthenticationFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wrighting.alfresco.repo.web.filter.beans.IgnoreFlagFilter;

public class AuthenticatedEvaluator extends BaseSSOAuthenticationFilter implements ConditionalFilterEvaluator {

	// Debug logging
	private static Log logger = LogFactory.getLog(AuthenticatedEvaluator.class);

	/** The remote user mapper. */
	protected RemoteUserMapper remoteUserMapper;

	@Override
	public void init(FilterConfig fc) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isEnabled(ServletContext context, ServletRequest request, ServletResponse response) {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		if (request.getAttribute(NO_AUTH_REQUIRED) != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Authentication not required, skipping filter for " + req.getRequestURI());
			}
			return false;
		}

		if (request.getAttribute(IgnoreFlagFilter.IGNORE_ATTRIBUTE) != null) {
			return false;
		}

		Enumeration<String> headers = req.getHeaderNames();
		while (headers.hasMoreElements()) {
			String headerName = headers.nextElement();
			String value = req.getHeader(headerName);
			if (logger.isDebugEnabled()) {
				logger.debug("Request header:" + headerName + "=" + value);
			}
			//e.g. if basic auth is being requested e.g. by mobile apps
			if (headerName.equalsIgnoreCase("authorization")) {
				return false;
			}
		}
		
		// Check if the user is already authenticated
		SessionUser user = getSessionUser(context, req, resp, true);

		if (user != null) {
			logger.debug("Current session user name:" + user.getUserName());
		}
		String currentUserName = remoteUserMapper.getRemoteUser(req);

		logger.debug("Current remote user name:" + currentUserName + " for " + req.getRequestURI());

		logger.debug("Current request user name:" + req.getRemoteUser() + " for " + req.getRequestURI());

		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				logger.debug("Cookie:" + cookies[i].getName() + "=" + cookies[i].getValue());
			}
		}
		Enumeration<String> attrNames = req.getAttributeNames();
		while (attrNames.hasMoreElements()) {
			String aname = attrNames.nextElement();
			logger.debug("Attribute:" + aname + "=" + req.getAttribute(aname));
		}

		HttpSession session = req.getSession();
		if (session != null) {
			attrNames = session.getAttributeNames();
			while (attrNames.hasMoreElements()) {
				String aname = attrNames.nextElement();
				logger.debug("Attribute:" + aname + "=" + req.getAttribute(aname));
			}
		}
		if (currentUserName != null) {
			return false;
		} else {
			return true;
		}
	}

	public void setRemoteUserMapper(RemoteUserMapper remoteUserMapper) {
		this.remoteUserMapper = remoteUserMapper;
	}

	@Override
	public boolean authenticateRequest(ServletContext arg0, HttpServletRequest arg1, HttpServletResponse arg2)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void restartLoginChallenge(ServletContext arg0, HttpServletRequest arg1, HttpServletResponse arg2)
			throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	protected Log getLogger() {
		return logger;
	}

}
