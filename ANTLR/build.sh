#! /usr/bin/bash

ANTLR=antlr-4.13.1-complete.jar
SRCS='src/*.java'

cd $(dirname $0)
cd src

if [[ ! ( -r PfxLexer.java && -r PfxParser.java && -r PfxVisitor.java && -r PfxBaseVisitor.java && PfxParser.java -nt Pfx.g4 ) ]]; then
    echo java -jar ../lib/$ANTLR -visitor -no-listener Pfx.g4
    java -jar ../lib/$ANTLR -visitor -no-listener Pfx.g4
fi

cd ..
echo javac -cp dst\;lib/$ANTLR -d dst $SRCS
javac -cp dst\;lib/$ANTLR -d dst $SRCS
