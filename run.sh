#!/bin/bash
# Downloads the spring-loaded lib if not existing and runs the full all-in-one
# (Alfresco + Share + Solr) using the runner project
springloadedfile=~/.m2/repository/org/springframework/springloaded/1.2.3.RELEASE/springloaded-1.2.3.RELEASE.jar

if [ ! -f $springloadedfile ]; then
mvn validate -Psetup
fi
#Enable remote debugging
#http://blogs.mulesoft.com/debugging-your-tomcat-webapp-with-eclipse/
export JPDA_OPTS="-agentlib:jdwp=transport=dt_socket, address=8000, server=y, suspend=n"
MAVEN_OPTS="-javaagent:$springloadedfile -noverify -Xms256m -Xmx2G" mvn install -Prun
