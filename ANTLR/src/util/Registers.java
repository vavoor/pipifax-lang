package util;

public class Registers {

  private static Register[] registers;

  static {
    registers = new Register[]{
      new Register("t0"),
      new Register("t1"),
      new Register("t2"),
      new Register("t3"),
      new Register("t4"),
      new Register("t5"),
      new Register("t6"),
      new Register("s1"),
      new Register("s2"),
      new Register("s3"),
      new Register("s4"),
      new Register("s5"),
      new Register("s6"),
      new Register("s7"),
      new Register("s8"),
      new Register("s9"),
      new Register("s10"),
      new Register("s11")
    };
  }

  public static final Register zero = new Register("zero");
  public static final Register fp = new Register("fp");
  public static final Register ra = new Register("ra");
  public static final Register sp = new Register("sp");
  public static final Register a0 = new Register("a0");
  public static final Register a1 = new Register("a1");
  public static final Register a2 = new Register("a2");
  public static final Register a3 = new Register("a3");
  public static final Register a4 = new Register("a4");
  public static final Register a5 = new Register("a5");
  public static final Register a6 = new Register("a6");
  public static final Register a7 = new Register("a7");
  
  public static class Register {
    private String name;
    private boolean used;
    
    private Register(String name) {
      this.name = name;
      this.used = false;
    }
    
    public String toString() {
      return this.name;
    }

    public void release() {
      this.used = false;
    }
  }
  
  public static Register acquire() {
    for (int i = 0; i < registers.length; i++) {
      if (!registers[i].used) {
        registers[i].used = true;
        return registers[i];
      }
    }
    System.err.println("Running out of registers");
    return null;
  }
}
