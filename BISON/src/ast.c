#include "ast.h"

#include <assert.h>
#include <stdlib.h>

struct Type int_type = { TY_INT, 4};
struct Type double_type = { TY_DOUBLE, 8 };
struct Type string_type = { TY_STRING, 4 };
struct Type void_type = { TY_VOID, 0};

struct Program* program;
struct Function* function;

static void* alloc(size_t size)
{
  void* p = malloc(size);
  assert(p != NULL);

  return p;
}

struct Program* AstProgram(void)
{
  struct Program* prog = malloc(sizeof(struct Program));
  assert(prog != NULL);

  prog->globals = ListMake();
  prog->functions = ListMake();

  return prog;
}

struct Function* AstFunction(const char* name, List* params, struct Type* type, List* stmts)
{
  struct Function* f = malloc(sizeof(struct Function));
  assert(f != NULL);

  f->name = name;
  f->type = type;
  f->params = params;
  f->locals = ListMake();
  f->stmts = stmts;

  return f;
}

struct Variable* AstGlobalVariable(const char* name, struct Type* type)
{
  struct GlobalVariable* v = malloc(sizeof(struct GlobalVariable));
  assert(v != NULL);

  v->super.clazz = V_GLOBAL;
  v->super.name = name;
  v->super.type = type;

  return (struct Variable*) v;
}

struct Variable* AstLocalVariable(const char* name, struct Type* type)
{
  struct LocalVariable* v = malloc(sizeof(struct LocalVariable));
  assert(v != NULL);

  v->super.clazz = V_LOCAL;
  v->super.name = name;
  v->super.type = type;

  return (struct Variable*) v;
}

struct Variable* AstParameter(const char* name, struct Type* type)
{
  struct Parameter* v = malloc(sizeof(struct Parameter));
  assert(v != NULL);

  v->super.clazz = V_PARAMETER;
  v->super.name = name;
  v->super.type = type;

  return (struct Variable*) v;
}


struct Type* AstArrayType(int dim, struct Type* base)
{
  struct ArrayType* t = malloc(sizeof(struct Type));
  assert(t != NULL);

  t->dim = dim;
  t->base = base;
  t->super.clazz = TY_ARRAY;
  t->super.size = dim * base->size;

  return (struct Type*) t;
}

struct Type* AstRefType(struct Type* base)
{
  struct RefType* ref = malloc(sizeof(struct RefType));
  assert(ref != NULL);

  ref->super.clazz= TY_REF;
  ref->super.size = 4; // TODO
  ref->base = base;

  return (struct Type*) ref;
}

struct Expr* AstBinaryExpr(int clazz, struct Expr* left, struct Expr* right)
{
  struct BinaryExpr* e = malloc(sizeof(struct BinaryExpr));
  assert(e != NULL);

  e->super.clazz = clazz;
  e->l = left;
  e->r = right;

  return (struct Expr*) e;
}

struct Expr* AstUnaryExpr(int clazz, struct Expr* expr)
{
  struct UnaryExpr* e = malloc(sizeof(struct UnaryExpr));
  assert(e != NULL);

  e->super.clazz = clazz;
  e->e = expr;

  return (struct Expr*) e;
}

struct Expr* AstIntLiteralExpr(int value)
{
  struct LiteralExpr* e = malloc(sizeof(struct LiteralExpr));
  assert(e != NULL);

  e->super.clazz = E_INT_LITERAL;
  e->u.i = value;

  return (struct Expr*) e;
}

struct Expr* AstDoubleLiteralExpr(double value)
{
  struct LiteralExpr* e = malloc(sizeof(struct LiteralExpr));
  assert(e != NULL);

  e->super.clazz = E_DOUBLE_LITERAL;
  e->u.d = value;

  return (struct Expr*) e;
}

struct Expr* AstStringLiteralExpr(const char* value)
{
  struct LiteralExpr* e = malloc(sizeof(struct LiteralExpr));
  assert(e != NULL);

  e->super.clazz = E_STRING_LITERAL;
  e->u.s = value;

  return (struct Expr*) e;
}

struct Expr* AstLValueExpr(struct LValue *lval)
{
  struct LValueExpr* e = malloc(sizeof(struct LValueExpr));
  assert(e != NULL);

  e->super.clazz = E_LVALUE;
  e->lval = lval;

  return (struct Expr*) e;
}

struct Expr* AstCallExpr(const char* name, List* args)
{
  struct CallExpr* e = malloc(sizeof(struct CallExpr));
  assert(e != NULL);

  e->super.clazz = E_CALL;
  e->name = name;
  e->args = args;

  return (struct Expr*) e;
}

struct LValue* AstNamedLValue(const char* name)
{
  struct NamedLValue* lval = malloc(sizeof(struct NamedLValue));
  assert(lval != NULL);

  lval->super.clazz = LV_NAMED;
  lval->name = name;

  return (struct LValue*) lval;
}

struct LValue* AstIndexedLValue(struct LValue* base, struct Expr* index)
{
  struct IndexedLValue* lval = malloc(sizeof(struct IndexedLValue));
  assert(lval != NULL);

  lval->super.clazz = LV_INDEXED;
  lval->base = base;
  lval->index = index;

  return (struct LValue*) lval;
}

struct Stmt* AstAssignment(struct LValue* lval, struct Expr* expr)
{
  struct Assignemt* s = malloc(sizeof(struct Assignemt));
  assert(s != NULL);

  s->super.clazz = S_ASSIGN;
  s->lval = lval;
  s->expr = expr;

  return (struct Stmt*) s;
}

struct Stmt* AstIfStmt(struct Expr* cond, List* if_true, List* if_false)
{
  struct IfStmt* s = malloc(sizeof(struct IfStmt));
  assert(s != NULL);

  s->super.clazz = S_IF;
  s->cond = cond;
  s->if_true = if_true;
  s->if_false = if_false;

  return (struct Stmt*) s;
}

struct Stmt* AstWhileStmt(struct Expr* cond, List* stmts)
{
  struct WhileStmt* s = malloc(sizeof(struct WhileStmt));
  assert(s != NULL);

  s->super.clazz = S_WHILE;
  s->cond = cond;
  s->stmts = stmts;

  return (struct Stmt*) s;
}

struct Stmt* AstCallStmt(struct CallExpr* call)
{
  struct CallStmt* s = malloc(sizeof(struct CallStmt));
  assert(s != NULL);

  s->super.clazz = S_CALL;
  s->call = call;

  return (struct Stmt*) s;
}

