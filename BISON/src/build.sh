#! /usr/bin/bash

SCRIPT_PATH=$(dirname $(readlink -f $0))
cd $SCRIPT_PATH

echo "bison -dv pfx.y"
bison -dv pfx.y

echo "flex pfx.l"
flex pfx.l

echo "gcc -g -o ../pfx *.c"
gcc -g -o ../pfxc *.c
