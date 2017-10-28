#!/bin/bash

make
for((i=0;i<20;i++)) do
    echo term ${i} has been start!
    java udp/StableUDP -classpath ./ | grep ERROR | tee -a shit.txt
done
make clean
