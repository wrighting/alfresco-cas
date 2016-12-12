A sample Alfresco SDK application with CAS authentication enabled.

N.B. Note that if Alfresco is configured with external auth then you should be aware that it makes use of HTTP headers and you should take suitable steps to ensure that you are not vunerable to a header injection attack. (see platform README.md)

Use at your own risk

Please see the README.md files in the sub projects for details of how to install each component.

You do not need to install the platform/repo amp unless you want to use the Alfresco Admin Console with CAS authentication

You will need to set the location of the repository in share-config-custom.xml.

There are several ways you can do this:

  * Build with the profile local, this includes share-config-custom.xml from the alfresco-cas-share-amp project with the default settings of localhost:8080
  * As above but setting the properties useraccess.endpoint, feed.endpoint, api.endpoint to point to a different location
  * Use a default build and put the settings in an external file e.g. /var/lib/tomcat7/shared/classes/alfresco/web-extension/share-config-custom.xml

