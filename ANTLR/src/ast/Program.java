package ast;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import util.AsmWriter;

public class Program extends Node {
  
  private Map<String, GlobalVariable> variables = new LinkedHashMap<>();
  private List<Statement> stmts = new ArrayList<>();

  public boolean addGlobalVariable(GlobalVariable v) {
    String name = v.name();
    if (variables.put(name, v) != null) {
      System.err.println("Global variable \'" + name + "\' is declared more than once.");
      return false;
    }
    return true;
  }

  public void addStatement(Statement stmt) {
    stmts.add(stmt);
  }

  public int resolveNames() {
    int errors = 0;
    for (Statement stmt : stmts) {
      errors += stmt.resolveNames(this.variables);
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.dataSection();
    for (GlobalVariable v : this.variables.values()) {
      v.generateCode(asm);
    }
    
    asm.textSection();
    for (Statement stmt : stmts) {
      stmt.generateCode(asm);
    }
  }
}
