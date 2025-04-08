package ast;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import util.AsmWriter;

public class Program extends Node {
  
  private Map<String, GlobalVariable> variables = new LinkedHashMap<>();
  private Map<String, Function> functions = new LinkedHashMap<>();

  public boolean addGlobalVariable(GlobalVariable v) {
    String name = v.name();
    if (variables.put(name, v) != null) {
      System.err.println("Global variable \'" + name + "\' is declared more than once.");
      return false;
    }
    return true;
  }

  public boolean addFunction(Function f) {
    String name = f.name();
    if (functions.put(name, f) != null) {
      System.err.println("Funciton \'" + name + "\' is defined more than once.");
      return false;
    }
    return true;
  }

  public Function function(String name) {
    return this.functions.get(name);
  }

  public int resolveNames() {
    int errors = 0;
    for (Function f : this.functions.values()) {
      errors += f.resolveNames(this.variables, this.functions);
    }
    return errors;
  }

  @Override
  public int checkTypes() {
    int errors = 0;
    for (Function f : this.functions.values()) {
      errors += f.checkTypes();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    asm.textSection();
    asm.println("\tjal main");
    asm.println("\tli a7,10");
    asm.println("\tecall");
    asm.println("");
    
    for (Function f : this.functions.values()) {
      f.generateCode(asm);
    }
    
    asm.dataSection();
    for (GlobalVariable v : this.variables.values()) {
      v.generateCode(asm);
    }
  }
}
