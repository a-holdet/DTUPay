#/***
#* @Author Simon Borup Lindegren, s185370
#*/

FROM adoptopenjdk:11-jre-hotspot
# COPY wait-for-it.sh /usr/src/wait-for-it.sh
COPY target/lib /usr/src/lib
COPY target/reportmicroservice-1.0.0.jar /usr/src/
WORKDIR /usr/src/
# RUN apt-get update
# RUN apt-get install -y netcat 
CMD java -Xmx32m -jar reportmicroservice-1.0.0.jar

