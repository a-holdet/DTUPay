#!/bin/sh
# wait-for-RabbitMq.sh

set -e
  
host="$1"
shift
cmd="$@"
  
until nc -z rabbitmq 5672; do
  >&2 echo "RabbitMq is unavailable - sleeping"
  sleep 1
done
  
>&2 echo "RabbitMq is up - executing command"
exec $cmd