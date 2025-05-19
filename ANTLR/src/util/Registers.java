package util;

import java.util.List;
import java.util.ArrayList;

public class Registers {

  private static GPRegister[] gpregisters;
  private static FPRegister[] fpregisters;

  static {
    gpregisters = new GPRegister[] {
      new GPRegister("t0"),
      new GPRegister("t1"),
      new GPRegister("t2"),
      new GPRegister("t3"),
      new GPRegister("t4"),
      new GPRegister("t5"),
      new GPRegister("t6"),
      new GPRegister("s1"),
      new GPRegister("s2"),
      new GPRegister("s3"),
      new GPRegister("s4"),
      new GPRegister("s5"),
      new GPRegister("s6"),
      new GPRegister("s7"),
      new GPRegister("s8"),
      new GPRegister("s9"),
      new GPRegister("s10"),
      new GPRegister("s11"),
      new GPRegister("a4"),
      new GPRegister("a5"),
      new GPRegister("a6"),
      new GPRegister("a7")
    };

    fpregisters = new FPRegister[32];
    for (int i = 0; i < 32; i++) {
      fpregisters[i] = new FPRegister("f" + i);
    }
  }

  public static final GPRegister zero = new GPRegister("zero");
  public static final GPRegister fp = new GPRegister("fp");
  public static final GPRegister ra = new GPRegister("ra");
  public static final GPRegister sp = new GPRegister("sp");
  public static final GPRegister a0 = new GPRegister("a0");
  public static final GPRegister a1 = new GPRegister("a1");
  public static final GPRegister a2 = new GPRegister("a2");
  public static final GPRegister a3 = new GPRegister("a3");
  public static final GPRegister a4 = new GPRegister("a4");
  public static final GPRegister a5 = new GPRegister("a5");
  public static final GPRegister a6 = new GPRegister("a6");
  public static final GPRegister a7 = new GPRegister("a7");

  public abstract static class Register {
    protected String name;
    protected boolean used;

    protected Register(String name) {
      this.name = name;
      this.used = false;
    }

    public String toString() {
      return this.name;
    }

    public void release() {
      this.used = false;
    }

    public abstract int size();
  }
  
  public static class GPRegister extends Register {
    
    private GPRegister(String name) {
      super(name);
    }

    public int size() {
      return 4;
    }
  }

  public static class FPRegister extends Register {
    private FPRegister(String name) {
      super(name);
    }

    public int size() {
      return 8;
    }
  }
  
  public static GPRegister acquireGP() {
    for (int i = 0; i < gpregisters.length; i++) {
      if (!gpregisters[i].used) {
        gpregisters[i].used = true;
        return gpregisters[i];
      }
    }
    System.err.println("Running out of general purpose registers");
    return null;
  }

  public static FPRegister acquireFP() {
    for (int i = 0; i < fpregisters.length; i++) {
      if (!fpregisters[i].used) {
        fpregisters[i].used = true;
        return fpregisters[i];
      }
    }
    System.err.println("Running out of floatingpoint purpose registers");
    return null;
  }

  public static int saveSpace() {
    int size = 0;
    
    for (int i = 0; i < gpregisters.length; i++) {
      Register r = gpregisters[i];
      if (r.used) {
        size += r.size();
      }
    }

    for (int i = 0; i < fpregisters.length; i++) {
      Register r = fpregisters[i];
      if (r.used) {
        size += r.size();
      }
    }

    return size;
  }
  
  public static void save(AsmWriter asm, int offset) {
    int off = offset;
    
    for (int i = 0; i < gpregisters.length; i++) {
      Register r = gpregisters[i];
      if (r.used) {
        asm.sw(r, off, sp);
        off += r.size();
      }
    }

    for (int i = 0; i < fpregisters.length; i++) {
      Register r = fpregisters[i];
      if (r.used) {
        asm.fsw(r, off, sp);
        off += r.size();
      }
    }
  }

  public static void restore(AsmWriter asm, int offset) {
    int off = offset;
    
    for (int i = 0; i < gpregisters.length; i++) {
      Register r = gpregisters[i];
      if (r.used) {
        asm.lw(r, off, sp);
        off += r.size();
      }
    }

    for (int i = 0; i < fpregisters.length; i++) {
      Register r = fpregisters[i];
      if (r.used) {
        asm.flw(r, off, sp);
        off += r.size();
      }
    }
  }
}
