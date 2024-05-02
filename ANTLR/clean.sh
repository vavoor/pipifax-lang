#! /usr/bin/bash

cd $(dirname $0)
rm -rf dst/*

cd src
rm -f Pfx.interp Pfx.tokens PfxLexer.interp PfxLexer.tokens PfxBaseVisitor.java PfxBaseListener.java PfxVisitor.java PfxListener.java PfxLexer.java PfxParser.java
