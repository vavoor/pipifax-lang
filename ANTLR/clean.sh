#! /usr/bin/bash

cd $(dirname $0)
rm -rf dst/* gen/* tst/*.s

cd src
rm -f *.interp *.tokens PfxBaseVisitor.java PfxBaseListener.java PfxVisitor.java PfxListener.java PfxLexer.java PfxParser.java *.class
