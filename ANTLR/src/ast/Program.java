package ast;

import java.util.Map;
import java.util.LinkedHashMap;

import util.AsmWriter;

public class Program extends Node {
  
  private Map<String, GlobalVariable> variables = new LinkedHashMap<>();

  public boolean addGlobalVariable(GlobalVariable v) {
    String name = v.name();
    if (variables.put(name, v) != null) {
      System.err.println("Global variable \'" + name + "\' is declared more than once.");
      return false;
    }
    return true;
  }

  public void generateCode(AsmWriter asm) {
    asm.dataSection();
    for (GlobalVariable v : this.variables.values()) {
      v.generateCode(asm);
    }
    asm.textSection();
  }
}
