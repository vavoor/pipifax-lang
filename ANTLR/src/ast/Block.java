package ast;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import util.AsmWriter;

public class Block extends Node {
  private List<Statement> statements = new ArrayList<>();

  public void addStmt(Statement stmt) {
    this.statements.add(stmt);
  }

  @Override
  public int resolveFunctionNames(Map<String, Function> functions) {
    int errors = 0;
    for (Statement s : this.statements) {
      errors += s.resolveFunctionNames(functions);
    }
    return errors;
  }

  @Override
  public int calculateAndCheckTypes() {
     int errors = 0;
    for (Statement s : this.statements) {
      errors += s.calculateAndCheckTypes();
    }
    return errors;
  }

  @Override
  public void generateCode(AsmWriter asm) {
    for (Statement s : this.statements) {
      s.generateCode(asm);
    }
  }
}
