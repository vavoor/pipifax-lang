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

  public void utilitySection() {
    nl();
    this.output.println(this.utilities);
  }

  public void println(String s) {
    this.output.println(s);
  }

  public void nl() {
    this.output.println();
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
  
  public void fsw(Registers.Register r, int offset, Registers.Register ra) {
    instr("fsw", r.toString(), offset(offset,ra));
  }

  public void fsw(Registers.Register r, Registers.Register ra) {
    fsw(r, 0, ra);
  }

  public void flw(Registers.Register r, int offset, Registers.Register ra) {
    instr("flw", r.toString(), offset(offset,ra));
  }

  public void flw(Registers.Register r, Registers.Register ra) {
    flw(r, 0, ra);
  }

  public void slt(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("slt", rd.toString(), rs.toString(), rt.toString());
  }

  public void sltu(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("sltu", rd.toString(), rs.toString(), rt.toString());
  }

  public void slti(Registers.Register rd, Registers.Register rs, int imm) {
    instr("slti", rd.toString(), rs.toString(), Integer.toString(imm));
  }

  public void sltiu(Registers.Register rd, Registers.Register rs, int imm) {
    instr("sltiu", rd.toString(), rs.toString(), Integer.toString(imm));
  }

  public void feq(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("feq.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void flt(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("flt.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void fle(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("fle.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void add(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("add", rd.toString(), rs.toString(), rt.toString());
  }

   public void addi(Registers.Register rd, Registers.Register rs, int val) {
    instr("addi", rd.toString(), rs.toString(), Integer.toString(val));
  }

  public void sub(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("sub", rd.toString(), rs.toString(), rt.toString());
  }

  public void mul(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("mul", rd.toString(), rs.toString(), rt.toString());
  }

  public void div(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("div", rd.toString(), rs.toString(), rt.toString());
  }

  public void jal(String name) {
    instr("jal", name);
  }

  public void j(String name) {
    instr("j", name);
  }

  public void ret() {
    instr("ret");
  }

  public void beq(Registers.Register rs, Registers.Register rt, String label) {
    instr("beq", rs.toString(), rt.toString(), label);
  }

  public void bne(Registers.Register rs, Registers.Register rt, String label) {
    instr("bne", rs.toString(), rt.toString(), label);
  }

  public void blt(Registers.Register rs, Registers.Register rt, String label) {
    instr("blt", rs.toString(), rt.toString(), label);
  }
  
  public void beqz(Registers.Register r, String label) {
    instr("beqz", r.toString(), label);
  }

  public void bnez(Registers.Register r, String label) {
    instr("bnez", r.toString(), label);
  }

  public void fadd(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("fadd.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void fsub(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("fsub.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void fmul(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("fmul.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void fdiv(Registers.Register rd, Registers.Register rs, Registers.Register rt) {
    instr("fdiv.d", rd.toString(), rs.toString(), rt.toString());
  }

  public void not(Registers.Register rd, Registers.Register rs) {
    instr("not", rd.toString(), rs.toString());
  }

  public void neg(Registers.Register rd, Registers.Register rs) {
    instr("neg", rd.toString(), rs.toString());
  }

  public void fneg(Registers.Register rd, Registers.Register rs) {
    instr("fneg.d", rd.toString(), rs.toString());
  }

  public void dtoi(Registers.Register rd, Registers.Register rs) {
    instr("fcvt.w.d", rd.toString(), rs.toString());
  }

  public void itod(Registers.Register rd, Registers.Register rs) {
    instr("fcvt.d.w", rd.toString(), rs.toString());
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

  public void memcpy(Registers.Register rd, Registers.Register rs, int count) {
    if (rd != Registers.a0) {
      mv(Registers.a0, rd);
    }
    if (rs != Registers.a1) {
      mv(Registers.a1, rs);
    }
    li(Registers.a2, count/4);
    jal("__memcpy"); // TODO: provide an implementation of __memcpy
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

  private static final String utilities =
  "__memcpy:\n" +
  "\tbnez a2,__memcpy_1\n" +
  "\tret\n" +
  "__memcpy_1:\n" +
  "\taddi a2,a2,-4\n" +
  "\tlw a3,0(a1)\n" +
  "\taddi a1,a1,4\n" +
  "\tsw a3,0(a0)\n" +
  "\taddi a0,a0,4\n" +
  "\tj __memcpy\n" +
  "\n" +
  "__strcmp:\n" +
  "\tbeq a0,a1,__strcmp_2\n" +
  "__strcmp_1:\n" +
  "\tlb a2,0(a0)\n" +
  "\tlb a3,0(a1)\n" +
  "\taddi a0,a0,1\n" +
  "\taddi a1,a1,1\n" +
  "\tblt a2,a3,__strcmp_3\n" +
  "\tblt a3,a2,__strcmp_4\n" +
  "\tbnez a0,__strcmp_1\n" +
  "__strcmp_2:\n" +
  "\tmv a0,zero\n" +
  "\tret\n" +
  "__strcmp_3:\n" +
  "\tli a0,-1\n" +
  "\tret\n" +
  "__strcmp_4:\n" +
  "\tli a0,1\n" +
  "\tret\n"
  ;
}
