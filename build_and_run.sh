#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
#pushd libraries/messaging-utilities
#./build.sh
#popd

#pushd RabbitTest
#./build.sh
#popd

pushd DTUPay
./build.sh
popd

# Update the set of services and
# build and execute the system tests
pushd demo_client
# docker-compose down # We dont take down the containers after tests
./deploy.sh 
sleep 5s
./test.sh
popd

# Cleanup the build images
docker image prune -f