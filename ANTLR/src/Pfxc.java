import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import java.io.IOException;
import java.io.FileNotFoundException;

import util.AsmWriter;

import ast.Program;

/**
 * The Pipifax compiler
 */
class Pfxc {

  public static void main(String[] args) {
    Pfxc pfxc = new Pfxc(args[0]);
    pfxc.compile();
  }

  private String inputFileName;

  private Pfxc(String fname) {
    this.inputFileName = fname;
  }

  private void compile() {
    try {
      PfxLexer lexer = new PfxLexer(CharStreams.fromFileName(this.inputFileName));
      PfxParser parser = new PfxParser(new CommonTokenStream(lexer));
      ParserRuleContext parseTree = parser.program();

      if (parser.getNumberOfSyntaxErrors() > 0) {
        throw new CompileError();
      }

      AstGen astgen = new AstGen();
      Program program = (Program) parseTree.accept(astgen);

      if (astgen.errors() > 0) {
        throw new CompileError();
      }

      if (program.resolveFunctionNames() > 0) {
         throw new CompileError();
      }

      if (program.calculateAndCheckTypes() > 0) {
        throw new CompileError();
      }

      program.allocateMemory();

      AsmWriter asm = new AsmWriter(baseName() + ".s");
      program.generateCode(asm);
      
    }
    catch (IOException e) {
      System.err.println("Cannot open file");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String baseName() {
    String baseName;
    int dot = this.inputFileName.lastIndexOf('.');
    if (dot > 0) {
      baseName = this.inputFileName.substring(0, dot);
    }
    else {
      baseName = this.inputFileName;
    }
    return baseName;
  }
}
