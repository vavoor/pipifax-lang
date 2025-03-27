#! /usr/bin/bash

INPUT=$PWD/$1

ANTLR=antlr-4.13.2-complete.jar

cd $(dirname $0)
java -cp dst\;lib/$ANTLR org.antlr.v4.gui.TestRig Pfx program -gui $INPUT
