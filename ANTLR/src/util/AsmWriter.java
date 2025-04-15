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
    instr("mv", rd.toString(), rs.toString());
  }
  
  public void la(Registers.Register r, String symbol) {
    instr("la", r.toString(), symbol);
  }

  public void li(Registers.Register r, int value) {
    instr("li", r.toString(), Integer.toString(value));
  }

  public void lw(Registers.Register r, int offset, Registers.Register ra) {
    instr("lw", r.toString(), offset(offset,ra));
  }

  public void lw(Registers.Register r, Registers.Register ra) {
    lw(r, 0, ra);
  }
  
  public void sw(Registers.Register r, int offset, Registers.Register ra) {
    instr("sw", r.toString(), offset(offset,ra));
  }

  public void sw(Registers.Register r, Registers.Register ra) {
    sw(r, 0, ra);
  }

  public void add(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("add", rd.toString(), rs.toString(), rt.toString());
  }

   public void addi(Registers.Register rd, Registers.Register rs, int val) {
    instr("addi", rd.toString(), rs.toString(), Integer.toString(val));
  }

  public void mul(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("mul", rd.toString(), rs.toString(), rt.toString());
  }

  public void jal(String name) {
    instr("jal", name);
  }

  public void ret() {
    instr("ret");
  }

  public void instr(String op, String... args) {
    StringBuffer instr = new StringBuffer("\t").append(op);
    String sep = " ";
    for (String s : args) {
      instr.append(sep);
      instr.append(s);
      sep = ",";
    }
    
    this.output.println(instr);
  }

  public void label(String label) {
    this.output.println(label + ":");
  }
  
  public void comment(String cmt) {
    this.output.println("# " + cmt);
  }
  
  private String offset(int offset, Registers.Register ra) {
    return Integer.toString(offset) + "(" + ra.toString() + ")";
  }
}
