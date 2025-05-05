class CodeGen extends PfxBaseVisitor<Void> {

  private AsmWriter asm;
  
  public CodeGen(AsmWriter asm) {
    this.asm = asm;
  }

  @Override
  public Void visitProgram(PfxParser.ProgramContext ctx) {
    this.asm.dataSection();
    visitChildren(ctx);
    this.asm.textSection();
    return null;
  }

  @Override
  public Void visitGlobalVariable(PfxParser.GlobalVariableContext ctx) {
    String varName = ctx.Name().getText();
    this.asm.println(varName + ":\t.word 0");
    return null;
  }
}
