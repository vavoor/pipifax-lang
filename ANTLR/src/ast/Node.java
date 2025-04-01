package ast;

import util.AsmWriter;

public abstract class Node {
  public abstract void generateCode(AsmWriter asm);
}
