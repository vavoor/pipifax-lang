# Simple compile script in Python

import os

antlr = "antlr-4.13.2-complete.jar"
classpath = f"dst;lib/{antlr}"

def cmd(c):
  print(c)
  os.system(c)

cmd(f"java -jar lib/{antlr} -o gen -visitor -no-listener src/Pfx.g4")

sources = "gen/*.java src/util/*.java src/ast/*.java src/*.java"
cmd(f"javac -cp {classpath} -d dst {sources}")
