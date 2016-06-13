#!/bin/sh

java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y -cp "bin/*" de.fhg.fit.biomos.sensorplatform.main.Main
