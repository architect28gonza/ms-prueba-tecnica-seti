#!/bin/bash

docker network create ms-network
 
docker run -d \
  --name db_franquicia \
  --network ms-network \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=db_gestion_franquicia \
  -p 5432:5432 \
  postgres:17

docker exec -i db_franquicia psql -U postgres -d db_gestion_franquicia < init.sql

docker build -t ms-franquicia-reactive .
 
docker run -d --name ms_franquicia --network ms-network \
  -e SPRING_HOST=db_franquicia \
  -e SPRING_PUERTO=5432 \
  -e SPRING_DATABASE=db_gestion_franquicia \
  -e SPRING_USERNAME=postgres \
  -e SPRING_PASSWORD=postgres \
  -p 8080:8080 \
  ms-franquicia-reactive


docker ps