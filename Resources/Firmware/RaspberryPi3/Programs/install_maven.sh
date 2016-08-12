#!/bin/bash

# newest version of maven is 3.3.9 (12.08.2016)
version=3.3.9
url=http://mirror.synyx.de/apache/maven/maven-3/${version}/binaries/apache-maven-${version}-bin.tar.gz

echo "Installing Maven"
echo "from: ${url}"

cd /home/${USER}/Downloads

echo "Downloading Maven"
wget url

echo "Extracting Maven"
mkdir /lib/maven
tar -xf "apache-maven-${version}-bin.tar.gz" -C /lib/maven

echo "Set environment variables"
echo "M2_HOME=/lib/maven/apache-maven-${version}" >> /etc/profile
echo "export M2_HOME" >> /etc/profile
echo "M2=/lib/maven/apache-maven-${version}/bin" >> /etc/profile
echo "export M2" >> /etc/profile
echo "PATH=$PATH:$M2" >> /etc/profile
echo "export PATH" >> /etc/profile
echo "M2_HOME is:"
echo $M2_HOME
echo "M2 is:"
echo $M2
echo "PATH is:"
echo $PATH

echo "Cleanup"
rm "/home/${USER}/Downloads/apache-maven-${version}-bin.tar.gz"

echo "Finished"
exit 0