#!/usr/bin/env bash

mvn clean install -U

docker build -t clarechu/eureka-server .

docker login -u clarechu -p lei13971368720

docker push clarechu/eureka-server