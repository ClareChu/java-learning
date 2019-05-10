#!/usr/bin/env bash

mvn clean install -U

docker build -t clarechu/eureka-provider .

docker login -u clarechu -p lei13971368720

docker push clarechu/eureka-provider