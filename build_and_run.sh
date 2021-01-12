#!/bin/bash
set -e

<<<<<<< HEAD
# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd libraries/messaging-utilities
./build.sh
=======
pushd DTUPay
mvn package
# Create a new docker image if necessary.
# Restarts the container with the new image if necessary
# The server stays running.
# To terminate the server run docker-compose down in the
# code-with-quarkus direcgtory
docker-compose up -d --build
# clean up images
docker image prune -f
>>>>>>> e0ff9421f9507f13e7e81007ae76973b62b24156
popd

pushd RabbitMQTest1
./build.sh
popd 

pushd RabbitMQTest2
./build.sh
popd

pushd DTUPay
./build.sh
popd

# Update the set of services and
# build and execute the system tests
pushd demo_client
./deploy.sh 
sleep 20s
./test.sh
docker-compose down # We dont take down the containers after tests
popd

pushd end_to_end_tests
./deploy.sh 
sleep 20s
./test.sh
docker-compose down # We dont take down the containers after tests
popd

# Cleanup the build images
docker image prune -f