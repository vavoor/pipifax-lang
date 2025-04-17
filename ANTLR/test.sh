#! /usr/bin/bash

ANTLR=antlr-4.13.2-complete.jar

cd $(dirname $0)/tst

PFXC="java -cp ../dst\;../lib/$ANTLR Pfxc"

run() {
    echo "$1 ..."
    $PFXC $1
}

if [[ -n "$1" ]]; then
  run $1
else
  for f in *.pfx; do
    run $f
  done
fi
