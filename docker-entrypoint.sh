#!/bin/sh
set -e

if [ -z "$SERVICE" ]; then
  SERVICE="microservice-banking"
fi

echo "Starting with SERVICE=$SERVICE"

if [ "$SERVICE" = "microservice-banking" ]; then
  exec java -jar /app/banking.jar
elif [ "$SERVICE" = "microservice-customer" ]; then
  exec java -jar /app/customer.jar
elif [ "$SERVICE" = "both" ]; then
  # start banking in background and then customer in foreground
  java -jar /app/banking.jar &
  exec java -jar /app/customer.jar
else
  echo "Unknown SERVICE: $SERVICE" >&2
  exit 1
fi
