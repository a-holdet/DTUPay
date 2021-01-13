FROM adoptopenjdk:11-jre-hotspot
WORKDIR /usr/src
COPY target/dtupay-1.0.0-SNAPSHOT-runner.jar /usr/src
COPY target/lib /usr/src/lib
CMD java -Xmx64m \
-jar dtupay-1.0.0-SNAPSHOT-runner.jar