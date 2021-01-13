#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd libraries/messaging-utilities
./build.sh
popd

pushd RabbitMQTest1
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

# pushd end_to_end_tests
# ./deploy.sh 
# sleep 20s
# ./test.sh
# docker-compose down # We dont take down the containers after tests
# popd

# Cleanup the build images
docker image prune -f