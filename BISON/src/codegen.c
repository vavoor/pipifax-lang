#include "codegen.h"

static FILE* out;

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
  ListIterator(function->locals, &it);
}

static void gen_function(struct Function* function)
{
  fprintf(out,"\nF_%s:\n", function->name);
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
