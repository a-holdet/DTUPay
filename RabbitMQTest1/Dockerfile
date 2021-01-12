FROM adoptopenjdk:11-jre-hotspot
COPY wait-for-it.sh /usr/src/
COPY target/lib /usr/src/lib
COPY target/service1-1.0.0.jar /usr/src/
WORKDIR /usr/src/
RUN apt-get update
RUN apt-get install -y netcat 
CMD java -Xmx32m -jar service1-1.0.0.jar
