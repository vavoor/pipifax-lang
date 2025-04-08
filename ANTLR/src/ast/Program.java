package ast;

import java.util.Map;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ArrayList;

import util.AsmWriter;

public class Program extends Node {
  
  private Map<String, GlobalVariable> variables;
  private Map<String, Function> functions;

  public Program(Map<String, GlobalVariable> variables, Map<String, Function> functions) {
    this.variables = variables;
    this.functions = functions;
  }

  public Function function(String name) {
    return this.functions.get(name);
  }

  public int resolveFunctionNames() {
    int errors = 0;
    for (Function f : this.functions.values()) {
      errors += f.resolveFunctionNames(this.functions);
    }
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    for (Function f : this.functions.values()) {
      errors += f.calculateAndCheckTypes();
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
