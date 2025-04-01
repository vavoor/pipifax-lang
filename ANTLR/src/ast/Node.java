package ast;

import util.AsmWriter;

public abstract class Node {
  public void generateCode(AsmWriter asm) {}
}
