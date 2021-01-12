FROM adoptopenjdk:11-jre-hotspot
COPY wait-for-it.sh /usr/src/
COPY target/lib /usr/src/lib
COPY target/service2-1.0.0-runner.jar /usr/src/
WORKDIR /usr/src/
RUN apt-get update
RUN apt-get install -y netcat 
CMD java -Xmx64m -jar service2-1.0.0-runner.jar
