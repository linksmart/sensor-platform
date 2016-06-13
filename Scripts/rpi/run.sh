#!/bin/sh

java -cp "bin/*" de.fhg.fit.biomos.sensorplatform.main.Main

#remote debugging params
# -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y
