#!/bin/sh

# run locally
#cd Sensorplatform
#java -cp "target/resources/*" de.fhg.fit.sensorplatform.main.Main

# run on remote
cd /home/linaro/Sensorplatform
java -cp "bin/*" de.fhg.fit.sensorplatform.main.Main

#remote debugging params
# -Xdebug -Xrunjdwp:transport=dt_socket,server=y,address=8000,suspend=y