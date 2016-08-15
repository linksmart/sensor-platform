#!/bin/bash

# newest version Java JDK 1.8.0_101 (19.07.2016)
ver="8u101"
version="jdk1.8.0_101"
url="http://download.oracle.com/otn-pub/java/jdk/${ver}-b13/jdk-${ver}-linux-arm32-vfp-hflt.tar.gz"

username=administrator

echo "Installing Oracle Java 8 JDK for ARM"
echo "from: ${url}"

cd /home/${username}/Downloads

echo "Downloading Java"
wget --no-check-certificate --no-cookies --header "Cookie: oraclelicense=accept-securebackup-cookie" ${url}

echo "Extracting Java"
mkdir /lib/jvm
tar -xf "jdk-${ver}-linux-arm32-vfp-hflt.tar.gz" -C /lib/jvm

echo "Set environment variables"
echo "JAVA_HOME=/lib/jvm/${version}" >> /etc/profile
echo "export JAVA_HOME" >> /etc/profile
echo "PATH=$PATH:/lib/jvm/${version}/bin" >> /etc/profile
echo "export PATH" >> /etc/profile

echo "Cleanup"
rm "/home/${username}/Downloads/jdk-${ver}-linux-arm32-vfp-hflt.tar.gz"

echo "Finished"
exit 0