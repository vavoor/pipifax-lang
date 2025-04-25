#include <assert.h>
#include <stdio.h>

#include "resolver.h"
#include "map.h"

static List* scopes;
static Map* functions;

static void enter_scope(void)
{
  ListPush(scopes, MapMake());
}

static void exit_scope(void)
{
  Map* m = ListPop(scopes);
  if (m != NULL) {
    MapDelete(m);
  }
}

static void* lookup(const char* key)
{
  ListItor it;
  ListIterator(scopes, &it);
  while (ListHasMore(&it)) {
    Map* m = ListNext(&it);
    void* value = MapGet(m, key);
    if (value != NULL) {
      return value;
    }
  }
  return NULL;
}

static void* insert(struct Variable* v)
{
  Map* m = ListTop(scopes);
  if (m != NULL) {
    void* previous = MapPut(m, v->name, v, NULL);
    return previous;
  }
  return NULL;
}

static int resolve_expr(struct Expr* expr);
static int resolve_lvalue(struct LValue* lval)
{
  int errors = 0;
  switch (lval->clazz) {
    case LV_NAMED: {
      struct NamedLValue* lv = (struct NamedLValue*) lval;
      lv->variable = lookup(lv->name);
      if (lv->variable == NULL) {
        fprintf(stderr, "Variable %s is not declared\n", lv->name);
        errors++;
      }
    }
    break;

    case LV_INDEXED: {
      struct IndexedLValue* lv = (struct IndexedLValue*) lval;
      errors += resolve_lvalue(lv->base);
      errors += resolve_expr(lv->index);
    }
    break;
  }

  return errors;
}

static int resolve_expr(struct Expr* expr)
{
  int errors = 0;

  switch (expr->clazz) {
    case E_OR:
    case E_AND:
    case E_ADD:
    case E_SUB:
    case E_MUL:
    case E_DIV:
    case E_LE:
    case E_LT:
    case E_GE:
    case E_GT:
    case E_EQ:
    case E_NE:
    case E_CMP: {
      struct BinaryExpr* b = (struct BinaryExpr*) expr;
      errors += resolve_expr(b->l);
      errors += resolve_expr(b->r);
    }
    break;

    case E_NOT:
    case E_NEG:
    case E_INT_CAST:
    case E_DOUBLE_CAST: {
      struct UnaryExpr* u = (struct UnaryExpr*) expr;
      errors += resolve_expr(u->expr);
    }
    break;

    case E_CALL: {
      struct CallExpr* e = (struct CallExpr*) expr;
      e->function = MapGet(functions, e->name);
      if (e->function == NULL) {
        fprintf(stderr, "Function %s is not defined.\n", e->name);
        errors++;
      }
      ListItor it;
      ListIterator(e->args, &it);
      while (ListHasMore(&it)) {
        struct Expr* e2 = (struct Expr*) ListNext(&it);
        errors += resolve_expr(e2);
      }
    }
    break;

    case E_LVALUE: {
      struct LValueExpr* e = (struct LValueExpr*) expr;
      errors += resolve_lvalue(e->lval);
    }
    break;

  }

  return errors;
}

static int resolve_stmts(List* stmts, struct Function* function)
{
  int errors = 0;
  ListItor it;
  ListIterator(stmts, &it);
  while (ListHasMore(&it)) {
    struct Node* n = ListNext(&it);
    switch (n->clazz) {
      case S_IF: {
        struct IfStmt* s = (struct IfStmt*) n;
        errors += resolve_expr(s->cond);
        errors += resolve_stmts(s->if_true, function);
        errors += resolve_stmts(s->if_false, function);
      }
      break;

      case  S_WHILE: {
        struct WhileStmt* s = (struct WhileStmt*) n;
        errors += resolve_expr(s->cond);
        errors += resolve_stmts(s->stmts, function);
      }
      break;

      case S_CALL: {
        struct CallStmt* s = (struct CallStmt*) n;
        errors += resolve_expr((struct Expr*) s->call);
      }
      break;

      case S_ASSIGN: {
        struct Assignemt* s = (struct Assignemt*) n;
        errors += resolve_lvalue((struct LValue*) s->lval);
        errors += resolve_expr(s->expr);
      }
      break;

      case V_LOCAL: {
        struct Variable* v= (struct Variable*) n;
        ListAppend(function->locals, v);
        if (insert(v)) {
          fprintf(stderr, "Local variable %s declared more than once\n", v->name);
          errors++;
        }
      }
      break;
    }
  }

  return errors;
}

static int resolve_function(struct Function* function)
{
  int errors = 0;
  enter_scope();

  insert(function->ret);

  ListItor it;
  ListIterator(function->params, &it);
  while (ListHasMore(&it)) {
    struct Variable* v = ListNext(&it);
    if (insert(v) != NULL) {
      fprintf(stderr, "Parameter %s of function %s declared more than once\n", v->name, function->name);
      errors++;
    }
  }

  enter_scope();
  errors += resolve_stmts(function->stmts, function);
  exit_scope();

  exit_scope();
  return errors;
}

int resolve(struct Program* program)
{
  int errors = 0;
  functions = MapMake();
  scopes = ListMake();
  enter_scope();

  ListItor it;
  ListIterator(program->globals, &it);
  while (ListHasMore(&it)) {
    struct Variable* v = ListNext(&it);
    if (insert(v) != NULL) {
      fprintf(stderr, "Global variable %s declared more than once\n", v->name);
      errors++;
    }
  }

  ListIterator(program->functions, &it);
  while (ListHasMore(&it)) {
    struct Function* f = ListNext(&it);
    if (MapPut(functions, f->name, f, NULL) != NULL) {
      fprintf(stderr, "Function %s is defined more than once\n", f->name);
      errors++;
    }
  }

  ListIterator(program->functions, &it);
  while (ListHasMore(&it)) {
    struct Function* f = ListNext(&it);
    errors += resolve_function(f);
  }

  if (MapGet(functions, "main") == NULL) {
    fprintf(stderr, "Main function is missing\n");
    errors++;
  }

  exit_scope();
  ListDelete(scopes);
  MapDelete(functions);

  return errors;
}
