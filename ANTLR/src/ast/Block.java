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
  public int resolveNames(Map<String, GlobalVariable> globals, Map<String, Function> functions) {
    int errors = 0;
    for (Statement s : this.statements) {
      errors += s.resolveNames(globals, functions);
    }
    return errors;
  }

  @Override
  public int checkTypes() {
     int errors = 0;
    for (Statement s : this.statements) {
      errors += s.checkTypes();
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
