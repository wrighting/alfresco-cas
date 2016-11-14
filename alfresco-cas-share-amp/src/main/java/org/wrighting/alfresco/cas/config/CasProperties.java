package org.wrighting.alfresco.cas.config;

import org.springframework.extensions.webscripts.processor.BaseProcessorExtension;

public class CasProperties extends BaseProcessorExtension {
	
	private String casServerLogoutUrl;
	private String casServiceParamName;

	private String logoutDestination;
	private String passwordLink;
	
	public String getLogoutDestination() {
		return logoutDestination;
	}

	public void setLogoutDestination(String logoutDestination) {
		this.logoutDestination = logoutDestination;
	}

	public String getCasServerLogoutUrl() {
		return this.casServerLogoutUrl;
	}

	public void setCasServerLogoutUrl(String casServerLogoutUrl) {
		this.casServerLogoutUrl = casServerLogoutUrl;
	}

	public String getCasServiceParamName() {
		return casServiceParamName;
	}

	public void setCasServiceParamName(String casServiceParamName) {
		this.casServiceParamName = casServiceParamName;
	}

	public String getPasswordLink() {
		return passwordLink;
	}

	public void setPasswordLink(String passwordLink) {
		this.passwordLink = passwordLink;
	}
}
