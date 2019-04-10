package org.wrighting.alfresco.cas.webscript;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;

import org.alfresco.service.cmr.security.AuthenticationService;

public class GetTicket extends DeclarativeWebScript {

    private static Log logger = LogFactory.getLog(GetTicket.class);

    private static final String MODEL_ALF_TOKEN_KEY = "alf_token";

    private AuthenticationService authenticationService;

    @Override
    protected Map<String,Object> executeImpl(WebScriptRequest req, Status status, Cache cache) {
        String userName = authenticationService.getCurrentUserName();

        Map<String,Object> model = new HashMap<String,Object>();
        String alfToken = authenticationService.getCurrentTicket();

        model.put(MODEL_ALF_TOKEN_KEY, alfToken);

        logger.debug("Token retrieved for :" + userName + " " + alfToken);

        return model;
    }

    /**
     * <p>Sets the authentication service</p>
     * @param authenticationService the {@code AuthenticationService} to be used
     */
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}
