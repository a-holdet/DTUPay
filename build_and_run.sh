#!/bin/bash
set -e

End_to_end_test () {
    docker-compose up -d
    sleep 5s

    # Update the set of services and
    # build and execute the system tests
    pushd demo_client
    mvn clean test
    popd

    # Cleanup the build images
    docker image prune -f
    # docker-compose down
}

INPUT=$1

if [ ! -z "$INPUT" ] && [ $INPUT != "demo_client" ]
then
    pushd $INPUT
        mvn clean package
        docker build -t $(echo $INPUT | tr '[:upper:]' '[:lower:]') .
    popd

    End_to_end_test

    exit 0
fi

# Build and install the libraries
# abstracting away from using the
# RabbitMq message queue
pushd libraries/messaging-utilities
mvn clean install
popd

pushd TokenMicroService
mvn clean package
docker build -t tokenmicroservice .
popd

pushd AccountMicroService
mvn clean package
docker build -t accountmicroservice .
popd

pushd PaymentMicroService
mvn clean package
docker build -t paymentmicroservice .
popd

pushd ReportMicroService
mvn clean package
docker build -t reportmicroservice .
popd

pushd DTUPay
mvn clean package
docker build -t dtupay .
popd

docker image prune -f
# docker-compose down
docker-compose up -d rabbitMq
sleep 20s

End_to_end_test