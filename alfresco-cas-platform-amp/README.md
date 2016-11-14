Note that Share will work with CAS without this AMP.

The AMP provides CAS authentication against the repository where appropriate.

Examples include
  * [The workflow console](http://localhost:8080/alfresco/s/admin/admin-workflowconsole)
  
## CAS configuration

See the README in cas-share-amp

## Alfresco configuration

See [Configuring external authentication](http://docs.alfresco.com/5.1/concepts/auth-external-intro.html)

## Proxy configuration

The documentation states that as SSO uses headers as external authentication tokens it is important to ensure that there is no untrusted direct access to the Alfresco ports, however, there is a chance that this is not practical e.g. if use Office Services *http://servername:portnumber/alfresco/aos* 

In this case you can run behind a proxy and use that to remove the authentication headers.

### Apache

Enable the headers module (Unix)

    a2enmod headers
    
Then in your Apache site configuration

    Header unset X-Alfresco-Remote-User
    
