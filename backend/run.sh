#!/bin/sh

port=${1:-"8080"}

JVM_OPTS="-Djava.net.preferIPv4Stack=true -XX:+UseG1GC -Xloggc:logs/gc_%t.log -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=1G -XX:+PrintGCDateStamps -XX:+PrintGCDetails"

mvn spring-boot:run -Dspring-boot.run.jvmArguments="${JVM_OPTS} -Dserver.port=${port}"
