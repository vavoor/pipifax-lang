package util;

import java.io.PrintStream;
import java.io.FileNotFoundException;

public class AsmWriter {
  private PrintStream output;

  public AsmWriter(String fname) throws FileNotFoundException {
    this.output = new PrintStream(fname);
    this.output.println("# Pipifax compiler V1.0");
  }

  public void dataSection() {
    this.output.println("\n.data");
  }

  public void textSection() {
    this.output.println("\n.text");
  }

  public void println(String s) {
    this.output.println(s);
  }

  public void mv(Registers.Register rd, Registers.Register rs) {
    instr("mv " + rd + "," + rs);
  }
  
  public void la(Registers.Register r, String symbol) {
    instr("la " + r + "," + symbol);
  }

  public void li(Registers.Register r, int value) {
    instr("li " + r + "," + value);
  }

  public void lw(Registers.Register r, int offset, Registers.Register ra) {
    instr("lw " + r + "," + offset + "(" + ra + ")");
  }

  public void lw(Registers.Register r, Registers.Register ra) {
    lw(r, 0, ra);
  }
  
  public void sw(Registers.Register r, int offset, Registers.Register ra) {
    instr("sw " + r + "," + offset + "(" + ra + ")");
  }

  public void sw(Registers.Register r, Registers.Register ra) {
    sw(r, 0, ra);
  }

  public void add(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    op3r("add", rd, rs, rt);
  }

   public void addi(Registers.Register rd, Registers.Register rs, int val) {
    instr("addi " + rd + "," + rs + "," + Integer.toString(val));
  }

  public void mul(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    op3r("mul", rd, rs, rt);
  }

  public void jal(String name) {
    instr("jal " + name);
  }

  public void ret() {
    instr("ret");
  }

  public void op3r(String op, Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr(op + " " + rd + "," + rs + "," + rt);
  }

  public void instr(String instr) {
    this.output.println("\t" + instr);
  }

  public void label(String label) {
    this.output.println(label + ":");
  }
  
  public void comment(String cmt) {
    this.output.println("# " + cmt);
  }
}
