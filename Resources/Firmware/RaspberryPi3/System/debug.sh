#!/bin/sh

echo "Stop any running sensorplatform application"
/etc/init.d/sensorplatform stop > /dev/null 2>&1

echo "Start java application in debug mode, waiting for remote connection on port 8000"
sudo $JAVA_HOME/bin/java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y -cp "bin/*" de.fhg.fit.biomos.sensorplatform.main.Main
