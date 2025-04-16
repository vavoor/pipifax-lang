package ast;

import java.util.Map;

import util.AsmWriter;

public abstract class Node {
  /**
   * @return number of errors
   */
  public int resolveFunctionNames(Map<String, Function> functions) {
    // Default implementation
    return 0;
  }

  public int calculateAndCheckTypes() {
    return 0;
  }

  public void allocateMemory() {
  }
  
  public void generateCode(AsmWriter asm) {
  }
}
