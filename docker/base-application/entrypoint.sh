#!/usr/bin/env bash

sleep 10000000000000
exec java -jar target/kafkaProducer-jar-with-dependencies.jar \
    -XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap ${MEMORY_LIMITS}
