#!/bin/bash
set -e

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd libraries/messaging-utilities
mvn clean install
popd

pushd RabbitTest
mvn clean package
docker build -t rabbitmqtest .
popd

pushd DTUPay
mvn clean package
docker build -t dtupay .
popd

docker image prune -f
# docker-compose down
docker-compose up -d rabbitMq
sleep 20s
docker-compose up -d RabbitMQTest dtupay
sleep 5s

# Update the set of services and
# build and execute the system tests
pushd demo_client
mvn clean test
popd

# Cleanup the build images
docker image prune -f
# docker-compose down