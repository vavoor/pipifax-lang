#include "codegen.h"

static FILE* out;
static int label_counter = 0;

static void gen_globals(List* globals)
{
  fprintf(out, "\n\t.data\n");
  ListItor it;
  ListIterator(globals, &it);
  while (ListHasMore(&it)) {
    struct Variable* v = (struct Variable*) ListNext(&it);
    fprintf(out, "G_%s:\t.space %d\n", v->name, v->type->size);
  }
}

static void allocate_function(struct Function* function)
{
  int offset = 0;
  ListItor it;
  ListIterator(function->params, &it);
  while (ListHasMore(&it)) {
    struct Variable* p = ListNext(&it);
    p->offset = offset;
    offset += p->type->size;
  }

  function->ret->offset = offset;
  offset += function->ret->type->size;
  function->args_size = offset;

  offset = 8; /* ra and fp */
  ListIterator(function->locals, &it);
  while (ListHasMore(&it)) {
    struct Variable* l = ListNext(&it);
    offset += l->type->size;
    l->offset = offset;
  }
  function->frame_size = offset;
}

static void gen_expr(struct Expr* expr)
{
}

static void gen_stmts(List* stmts);
static void gen_stmt(struct Stmt* stmt)
{
  switch (stmt->clazz) {
    case S_IF: {
      struct IfStmt* s = (struct IfStmt*) stmt;
      gen_expr(s->cond);
      if (ListSize(s->if_false) > 0) {
        int lbl1 = label_counter++;
        fprintf(out, "\tbneqz %s,_L%d\n", s->cond->reg, lbl1);
        gen_stmts(s->if_false);
        int lbl2 = label_counter++;
        fprintf(out, "\tj _L%d\n", lbl2);
        fprintf(out, "_L%d:\n", lbl1);
        gen_stmts(s->if_true);
        fprintf(out, "_L%d:\n", lbl2);
      }
      else {
        int lbl = label_counter++;
        fprintf(out, "\tbeqz %s,_L%d\n", s->cond->reg, lbl);
        gen_stmts(s->if_true);
        fprintf(out, "_L%d:\n", lbl);
      }
    }
    break;

    case S_WHILE: {
      struct WhileStmt* s = (struct WhileStmt*) stmt;
      int lbl1 = label_counter++;
      int lbl2 = label_counter++;
      fprintf(out, "_L%d:\n", lbl1);
      gen_expr(s->cond);
      fprintf(out, "\tbeqz %s,_L%d\n", s->cond->reg, lbl2);
      gen_stmts(s->stmts);
      fprintf(out, "\tj _L%d\n", lbl1);
      fprintf(out, "_L%d:\n", lbl2);
    }
    break;

    case S_CALL: {
    }
    break;

    case S_ASSIGN: {
    }
    break;
  }
}

static void gen_stmts(List* stmts)
{
  ListItor it;
  ListIterator(stmts, &it);
  while (ListHasMore(&it)) {
    struct Stmt* s = ListNext(&it);
    gen_stmt(s);
  }
}

static void gen_function(struct Function* function)
{
  fprintf(out, "\nF_%s:\n", function->name);
  fprintf(out, "\taddi sp,sp,%d\n", -function->frame_size);
  fprintf(out, "\tsw ra,%d(sp)\n", function->frame_size - 4);
  fprintf(out, "\tsw fp,%d(sp)\n", function->frame_size - 8);
  fprintf(out, "\taddi fp,sp,%d\n", function->frame_size);
  fprintf(out, "\n");

  gen_stmts(function->stmts);

  fprintf(out, "\n");
  fprintf(out, "\tlw fp,%d(sp)\n", function->frame_size - 8);
  fprintf(out, "\tlw ra,%d(sp)\n", function->frame_size - 4);
  fprintf(out, "\taddi sp,sp,%d\n", function->frame_size);
  fprintf(out, "\tret\n");
  fprintf(out, "\n");
}

static void gen_functions(List* functions)
{
  ListItor it;
  ListIterator(functions, &it);
  while (ListHasMore(&it)) {
    struct Function* f = (struct Function*) ListNext(&it);
    allocate_function(f);
  }

  fprintf(out, "\n\t.text");
  ListIterator(functions, &it);
  while (ListHasMore(&it)) {
    struct Function* f = (struct Function*) ListNext(&it);
    gen_function(f);
  }
  fprintf(out, "\n");
}

void gen_code(struct Program* prog, FILE* o)
{
  out = o;

  fprintf(out, "# Pipifax Compiler V1.0\n");

  gen_globals(prog->globals);
  gen_functions(prog->functions);
}
