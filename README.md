A sample Alfresco SDK application with CAS authentication enabled.

Please note that it is not sufficient to install the generated amps as they stand, not least because changes are required to web.xml - see the wiki for a more detailed explanation.

You need to rebuild after setting the following maven properties - alfresco.server.name, share.server.name, cas.server.prefix and cas.logout.dest.url - some defaults may be meaningful for you but check anyway.


