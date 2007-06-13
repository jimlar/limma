#!/bin/sh

export JAVA_HOME=/opt/jdk1.5.0_05
export MAVEN_HOME=/home/jimmy/bin/maven-2.0.6

export PATH=${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}

mvn -Dmaven.test.skip=true package exec:java
