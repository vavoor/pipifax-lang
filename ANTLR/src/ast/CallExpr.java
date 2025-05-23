package ast;

import java.util.Map;
import java.util.List;
import java.util.Iterator;

import util.AsmWriter;
import util.Registers;

public class CallExpr extends Expr {
  private String name;
  private List<Expr> args;
  
  private Function function;

  public CallExpr(String name, List<Expr> args) {
    this.name = name;
    this.args = args;
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = 0;
    for (Expr expr : args) {
      errors += expr.resolveFunctionNames(functions);
    }
    
    this.function = functions.get(this.name);
    if (this.function == null) {
      System.err.println("Function \'" + this.name + "\' is called but not defined.");
      errors++;
    }
    
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
    int errors = 0;
    for (Expr expr : args) {
      errors += expr.calculateAndCheckTypes();
    }

    if (args.size() == this.function.parameters().size() - 1) {
      Iterator<Parameter> params = this.function.parameters().iterator();
      Iterator<Expr> args = this.args.iterator();
      for (int i = 0; i < this.args.size(); i++) {
        Parameter param = params.next();
        Expr arg = args.next();
        if (!param.type().accepts(arg.type())) {
          System.err.println("Type mismatch of parameter " + param.name() + " in call to function " + this.name);
          errors++;
        }
      }
    }
    else {
      System.err.println("Number of arguments does not match in call to function " + this.name);
      errors++;
    }
    this.type = this.function.type();
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    int savedRegisters = Registers.saveSpace();
    
    asm.addi(Registers.sp, Registers.sp, -(this.function.parametersSize() + savedRegisters));

    Registers.save(asm, this.function.parametersSize());
    
    Iterator<Parameter> params = this.function.parameters().iterator();
    for (Expr expr : this.args) {
      Parameter param = params.next();
      expr.generateCode(asm);
      param.type().call(new Type.Operation() {
        public void forInt() {
          asm.sw(expr.result(), param.offset(), Registers.sp);
        }
        public void forDouble() {
          asm.fsw(expr.result(), param.offset(), Registers.sp);
        }
        public void forString() {
          asm.sw(expr.result(), param.offset(), Registers.sp);
        }
        public void forArray() {
          asm.addi(Registers.a0, Registers.sp, param.offset());
          asm.memcpy(Registers.a0, expr.result(), expr.type().size());
        }
        public void forReference() {
          asm.sw(expr.result(), param.offset(), Registers.sp);
        }
      });
      
      expr.result().release();
    }
    Parameter ret = params.next();
    asm.jal(function.mangledName());
    
    this.type.call(new Type.Operation() {
      public void forInt() {
        register = Registers.acquireGP();
        asm.lw(register, ret.offset(), Registers.sp);
      }

      public void forDouble() {
        register = Registers.acquireFP();
        asm.flw(register, ret.offset(), Registers.sp);
      }
      
      public void forString() {
        register = Registers.acquireGP();
        asm.lw(register, ret.offset(), Registers.sp);
      }

      public void forArray() {
        register = Registers.acquireGP();
        asm.addi(register, Registers.sp, ret.offset());
      }

      public void forVoid() {
        register = Registers.acquireGP();
      }
    });

    Registers.restore(asm, this.function.parametersSize());
    asm.addi(Registers.sp, Registers.sp, this.function.parametersSize() + savedRegisters);
  }
}
