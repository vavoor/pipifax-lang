package ast;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import util.AsmWriter;

public class Program extends Node {
  
  private Collection<GlobalVariable> variables;
  private Collection<Function> functions;

  public Program(Collection<GlobalVariable> variables, Collection<Function> functions) {
    this.variables = variables;
    this.functions = functions;
  }

  public int resolveFunctionNames() {
    int errors = 0;
    
    HashMap<String, Function> functions = new HashMap<>();
    for (Function f : this.functions) {
      if (functions.put(f.name(), f) != null) {
        System.err.println(" Function \'" + f.name() + "\' is defined more than once.");
        errors++;
      }
    }

    if (functions.get("main") == null) {
      System.err.println("No main function defined.");
      errors++;
    }
    
    for (Function f : this.functions) {
      errors += f.resolveFunctionNames(functions);
    }
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    for (Function f : this.functions) {
      errors += f.calculateAndCheckTypes();
    }
    return errors;
  }

  @Override
  public void allocateMemory() {
    for (Function f : this.functions) {
      f.allocateMemory();
    }
  }
  
  @Override
  public void generateCode(AsmWriter asm) {
    asm.textSection();
    asm.jal("f_main");
    asm.instr("li a7,10");
    asm.instr("ecall");
    
    for (Function f : this.functions) {
      f.generateCode(asm);
    }
    
    asm.dataSection();
    for (GlobalVariable v : this.variables) {
      v.generateCode(asm);
    }
  }
}
