# CAS integration module

**N.B.** Note that if Alfresco is configured with external auth then you should be aware that it makes use of HTTP headers and you should take suitable steps to ensure that you are not vulnerable to a header injection attack.

## CAS client configuration

The AMP includes the cas client jar for ease of installation.

If you want to deploy the enclosed jars, instead of the amp, that should work unless you have built the version using web.xml

### default

To use the default configuration you need to configure /etc/java-cas-client.properties - See [CAS Client Github](https://github.com/apereo/java-cas-client)

	casServerLoginUrl=https://tech.wrighting.org/sso/login
	serverName=http://localhost:8080
	ticketValidatorClass=org.jasig.cas.client.validation.Cas30ServiceTicketValidator
	casServerUrlPrefix=https://tech.wrighting.org/sso

### alternate configuration strategy (requires custom build)

The configuration strategy/location can also be changed via properties in the pom.xml.

If you are using the WEB_XML strategy then you should also use the webxml maven profile.


## Logout configuration

The following properties should be set in share-global.properties

    casServerLogoutUrl=https://tech.wrighting.org/sso/logout
    logoutDestination=http://tech.wrighting.org
    
Optionally you can also set a Change Password Link

    passwordLink=
    
## Other configuration (requires custom build)

If your platform/repo is not running on localhost:8080 you will need to change the endpoints in share-config-custom.xml. This can be done via properties in the pom.xml if you want to use the packaged file.
See documentation or the example included here for the settings to use.

For versions 5.0.d to 5.1.f you need to replace the SlingshotAlfrescoConnector.

This can be done by setting the connector.package property in the pom.xml to *wrighting.alfresco*

## Alternative implementation

The default build uses a web-fragment.xml to enable the CAS filters, because these will be applied after the Alfresco filters there is an additional filter (DeepLinkingFilter) to make sure that deep linking works (deep linking is going directly to a page other than the user homepage on logging in).

## Upgrade notes

For new versions you need to check that there are no additional end points to configure in web-fragment.xml
   
  
 
