#! /usr/bin/bash

gcc -g -I ../src -o t_list t_list.c ../src/list.c && time ./t_list
gcc -g -I ../src -o t_map t_map.c ../src/map.c ../src/list.c && time ./t_map

