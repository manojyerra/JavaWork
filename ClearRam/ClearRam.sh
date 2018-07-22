#!/bin/bash

echo "Before clearing ram";
echo
echo
free -h
echo
echo
sync; echo 1 | sudo tee /proc/sys/vm/drop_caches
echo
echo
echo "After clearing ram";
echo
echo
free -h
