#!/bin/bash

# newest version Java JDK 1.8.0_101 (19.07.2016)
java_ver="8u101"
java_version="jdk1.8.0_101"
java_url="http://download.oracle.com/otn-pub/java/jdk/${java_ver}-b13/jdk-${java_ver}-linux-arm32-vfp-hflt.tar.gz"
# newest version of maven is 3.3.9 (12.08.2016)
maven_version=3.3.9
maven_url=http://mirror.synyx.de/apache/maven/maven-3/${maven_version}/binaries/apache-maven-${maven_version}-bin.tar.gz

username=administrator

echo "Installing Oracle Java 8 JDK for ARM"
echo "from: ${java_url}"

cd /home/${username}/Downloads

echo "Downloading Java"
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" ${java_url}

echo "Extracting Java"
mkdir /lib/jvm
tar -xf "jdk-${java_ver}-linux-arm32-vfp-hflt.tar.gz" -C /lib/jvm

rm "/home/${username}/Downloads/jdk-${java_ver}-linux-arm32-vfp-hflt.tar.gz"

echo "Installing Maven"
echo "from: ${maven_url}"

cd /home/${username}/Downloads

echo "Downloading Maven"
wget ${maven_url}

echo "Extracting Maven"
mkdir /lib/maven
tar -xf "apache-maven-${maven_version}-bin.tar.gz" -C /lib/maven

rm "/home/${username}/Downloads/apache-maven-${maven_version}-bin.tar.gz"

echo "Set environment variables"
echo "JAVA_HOME=/lib/jvm/${java_version}" >> /etc/profile
echo "export JAVA_HOME" >> /etc/profile
echo "M2_HOME=/lib/maven/apache-maven-${maven_version}" >> /etc/profile
echo "export M2_HOME" >> /etc/profile
echo "M2=/lib/maven/apache-maven-${maven_version}/bin" >> /etc/profile
echo "export M2" >> /etc/profile
echo "PATH=$PATH:/lib/jvm/${java_version}/bin:/lib/maven/apache-maven-${maven_version}/bin" >> /etc/profile
echo "export PATH" >> /etc/profile

echo "Changes to environment variables take effect after logout and login again"

echo "Finished"
exit 0