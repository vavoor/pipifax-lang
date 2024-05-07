#! /usr/bin/bash

# set -x

ANTLR=antlr-4.13.1-complete.jar

cd $(dirname $0)

# Generate code from the grammar only if the grammar has changed
cd src
if [[ ! ( -r PfxLexer.java && -r PfxParser.java && -r PfxVisitor.java && -r PfxBaseVisitor.java && PfxParser.java -nt Pfx.g4 ) ]]
then
    echo java -jar ../lib/$ANTLR -visitor -no-listener Pfx.g4
    java -jar ../lib/$ANTLR -visitor -no-listener Pfx.g4
fi
cd ..

# Compile all Java files in src
DIRS="src"
if [[ -d src/util ]]; then DIRS="$DIRS src/util"; fi
if [[ -d src/ast ]]; then DIRS="$DIRS src/ast"; fi

SRCS=""
for d in $DIRS; do SRCS="$SRCS $d/*.java"; done

# CLASSPATH="dst:lib/$ANTLR"	
CLASSPATH="dst\\;lib/$ANTLR"	# This is a peculiarity of running the script in the git bash
echo javac -cp $CLASSPATH -d dst $SRCS
javac -cp $CLASSPATH -d dst $SRCS
