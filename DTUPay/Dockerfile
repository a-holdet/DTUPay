FROM adoptopenjdk:11-jre-hotspot
COPY target/lib /usr/src/lib
COPY target/dtupay-1.0.0-SNAPSHOT-runner.jar /usr/src
WORKDIR /usr/src
<<<<<<< HEAD
=======
COPY target/dtupay-1.0.0-SNAPSHOT-runner.jar /usr/src
COPY target/lib /usr/src/lib
>>>>>>> e0ff9421f9507f13e7e81007ae76973b62b24156
CMD java -Xmx64m \
-jar dtupay-1.0.0-SNAPSHOT-runner.jar