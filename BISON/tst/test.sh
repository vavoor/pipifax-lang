#! /usr/bin/bash

gcc -g -I ../src -o t_list t_list.c ../src/list.c && ./t_list
gcc -g -I ../src -o t_map t_map.c ../src/map.c ../src/list.c && ./t_map

for f in *.pfx; do
  echo "$f ..."
  ../pfxc $f
  if [[ $? = 0 ]]; then
    echo "Done"
  else
    echo "Error"
  fi
done
