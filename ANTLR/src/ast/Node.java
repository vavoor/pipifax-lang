package ast;

import java.util.Map;

import util.AsmWriter;

public abstract class Node {
  /**
   * @return number of errors
   */
  public int resolveNames(Map<String, GlobalVariable> globals) {
    // Default implementation
    return 0;
  }
  
  public  void generateCode(AsmWriter asm) {
  }
}
