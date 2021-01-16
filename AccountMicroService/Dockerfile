FROM adoptopenjdk:11-jre-hotspot
COPY wait-for-it.sh /usr/src/wait-for-it.sh
COPY target/lib /usr/src/lib
COPY target/accountmicroservice-1.0.0.jar /usr/src/
WORKDIR /usr/src/
RUN apt-get update
RUN apt-get install -y netcat 
CMD java -Xmx32m -jar accountmicroservice-1.0.0.jar
