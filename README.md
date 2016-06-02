A sample Alfresco SDK application with CAS authentication enabled.

Please note that it is not sufficient to install the generated amps as they stand, not least because changes are required to web.xml - see the wiki for a more detailed explanation.

You need to rebuild after setting the following maven properties - alfresco.server.name, share.server.name, cas.server.prefix and cas.logout.dest.url - some defaults may be meaningful for you but check anyway.

N.B. Note that if Alfresco is configured with external auth then you should be aware that it makes use of HTTP headers and you should take suitable steps to ensure that you are not vunerable to a header injection attack.
