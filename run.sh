#!/bin/bash

MAVEN_OPTS="-Xms256m -Xmx2G" mvn clean install -Plocal alfresco:run -Dmdep.unpack.excludes="WEB-INF/lib/spring-webscripts*.jar"
