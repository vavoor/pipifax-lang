package ast;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;

import util.AsmWriter;

public class Program extends Node {
  
  private Collection<GlobalVariable> variables;
  private Collection<Function> functions;
  private Collection<DoubleLiteralExpr> doubles;
  private Collection<StringLiteralExpr> strings;

  public Program(Collection<GlobalVariable> variables, Collection<Function> functions,
      Collection<DoubleLiteralExpr> doubles, Collection<StringLiteralExpr> strings) {
    this.variables = variables;
    this.functions = functions;
    this.doubles = doubles;
    this.strings = strings;
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
  public void generateCode(AsmWriter asm) {

    for (Function f : this.functions) {
      f.allocateMemory();
    }
    
    asm.textSection();
    asm.jal("f_main");
    asm.instr("li a7,10");
    asm.instr("ecall");
    
    asm.utilitySection();
    
    for (Function f : this.functions) {
      f.generateCode(asm);
    }
    
    asm.dataSection();
    for (DoubleLiteralExpr d : this.doubles) {
      asm.println(d.id() + ":\t.double " + d.value());
    }
    for (StringLiteralExpr s : this.strings) {
      asm.println(s.id() + ":\t.asciz \"" + s.value() + "\"");
    }
    for (GlobalVariable v : this.variables) {
      v.generateCode(asm);
    }
  }
}
