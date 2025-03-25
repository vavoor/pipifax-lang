#! /usr/bin/bash

# set -x

ANTLR=antlr-4.13.2-complete.jar

cd $(dirname $0)

# Generate code from the grammar only if the grammar has changed
if [[ ! ( -r gen/PfxLexer.java && -r gen/PfxParser.java && -r gen/PfxVisitor.java && -r gen/PfxBaseVisitor.java && gen/PfxParser.java -nt src/Pfx.g4 ) ]]
then
    echo java -jar lib/$ANTLR -o gen -visitor -no-listener src/Pfx.g4
    java -jar lib/$ANTLR -o gen -visitor -no-listener src/Pfx.g4
fi

# Compile all Java files in src
DIRS="gen src"
if [[ -d src/util ]]; then DIRS="$DIRS src/util"; fi
if [[ -d src/ast ]]; then DIRS="$DIRS src/ast"; fi

SRCS=""
for d in $DIRS; do SRCS="$SRCS $d/*.java"; done

# CLASSPATH="dst:lib/$ANTLR"
CLASSPATH="dst\\;lib/$ANTLR"	# This is a peculiarity of running the script in the git bash
echo javac -cp $CLASSPATH -d dst $SRCS
javac -cp $CLASSPATH -d dst $SRCS
