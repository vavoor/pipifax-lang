#include <stdio.h>

#include "types.h"

static int is_int(struct Type* type)
{
  return type->clazz == TY_INT;
}

static int is_double(struct Type* type)
{
  return type->clazz == TY_DOUBLE;
}

static int is_num(struct Type* type)
{
  return is_int(type) || is_double(type);
}

static int is_array(struct Type* type)
{
  struct Type* t = type;
  if (type->clazz == TY_REF) {
    struct RefType* r = (struct RefType*) type;
    t = r->base;
  }
  return t->clazz == TY_ARRAY;
}

static struct Type* effective_type(struct Type* type)
{
  if (type->clazz == TY_REF) {
    struct RefType* rt = (struct RefType*) type;
    return rt->base;
  }
  else {
    return type;
  }
}

static int is_string(struct Type* type)
{
  return type->clazz == TY_STRING;
}

static int assignable(struct Type* l, struct Type* r)
{
  if (is_array(l)) {
    struct ArrayType* atl = (struct ArrayType*) effective_type(l);
    if (is_array(r)) {
      struct ArrayType* atr = (struct ArrayType*) effective_type(r);
      return assignable(atl->base, atr->base) && (atl->dim == -1 || atl->dim == atr->dim);
    }
  }
  else {
    switch (l->clazz) {
      case TY_INT:
        return is_int(r);

      case TY_DOUBLE:
        return is_double(r);

      case TY_STRING:
        return is_string(r);
    }
  }
  return 0;
}

static int check_expr(struct Expr* expr);
static int check_lvalue(struct LValue* lval)
{
  int errors = 0;

  switch (lval->clazz) {
    case LV_NAMED: {
      struct NamedLValue* lv = (struct NamedLValue*) lval;
      lval->type = lv->variable->type;
    }
    break;

    case LV_INDEXED: {
      struct IndexedLValue* lv = (struct IndexedLValue*) lval;
      errors += check_lvalue(lv->base);
      errors += check_expr(lv->index);
      if (is_array(lv->base->type)) {
        struct ArrayType* at = (struct ArrayType*) effective_type(lv->base->type);
        lval->type = at->base;
      }
      else {
        fprintf(stderr, "Array type required\n");
        errors++;
        lval->type = &error_type;
      }
    }
    break;
  }

  return errors;
}

static int check_expr(struct Expr* expr)
{
  int errors = 0;
  switch (expr->clazz) {
    case E_OR:
    case E_AND:{
      struct BinaryExpr* e = (struct BinaryExpr*) expr;
      errors += check_expr(e->l);
      errors += check_expr(e->r);
      if (!is_int(e->l->type) || !is_int(e->r->type)) {
        fprintf(stderr, "Logical operation requires int types\n");
        errors++;
      }
      expr->type = &int_type;
    }
    break;

    case E_ADD:
    case E_SUB:
    case E_MUL:
    case E_DIV: {
      struct BinaryExpr* e = (struct BinaryExpr*) expr;
      errors += check_expr(e->l);
      errors += check_expr(e->r);
      if (!is_num(e->l->type) || !is_num(e->r->type)) {
        fprintf(stderr, "Mathematical operation requires numeric types\n");
        errors++;
      }
      expr->type = e->l->type;
    }
    break;

    case E_LE:
    case E_LT:
    case E_GE:
    case E_GT:
    case E_EQ:
    case E_NE: {
      struct BinaryExpr* e = (struct BinaryExpr*) expr;
      errors += check_expr(e->l);
      errors += check_expr(e->r);
      if (!is_num(e->l->type) || !is_num(e->r->type)) {
        fprintf(stderr, "Comparison operation requires numeric types\n");
        errors++;
      }
      expr->type = &int_type;
    }
    break;

    case E_CMP: {
      struct BinaryExpr* e = (struct BinaryExpr*) expr;
      errors += check_expr(e->l);
      errors += check_expr(e->r);
      if (!is_string(e->l->type) || !is_string(e->r->type)) {
        fprintf(stderr, "<=> operator requires string types\n");
        errors++;
      }
      expr->type = &int_type;
    }
    break;

    case E_NOT: {
      struct UnaryExpr* e = (struct UnaryExpr*) expr;
      errors += check_expr(e->expr);
      if (!is_int(e->expr->type)) {
        fprintf(stderr, "! operator requires integer type\n");
        errors++;
      }
      expr->type = &int_type;
    }
    break;

    case E_NEG: {
      struct UnaryExpr* e = (struct UnaryExpr*) expr;
      errors += check_expr(e->expr);
      if (!is_num(e->expr->type)) {
        fprintf(stderr, "! operator requires numeric type\n");
        errors++;
      }
      expr->type = e->expr->type;
    }
    break;

    case E_INT_CAST: {
      struct UnaryExpr* e = (struct UnaryExpr*) expr;
      errors += check_expr(e->expr);
      if (!is_double(e->expr->type)) {
        fprintf(stderr, "Cast-to-int operator requires double type\n");
        errors++;
      }
      expr->type = &int_type;
    }
    break;

    case E_DOUBLE_CAST: {
      struct UnaryExpr* e = (struct UnaryExpr*) expr;
      errors += check_expr(e->expr);
      if (!is_int(e->expr->type)) {
        fprintf(stderr, "Cast-to-double operator requires integer type\n");
        errors++;
      }
      expr->type = &double_type;
    }
    break;

    case E_CALL: {
      struct CallExpr* call = (struct CallExpr*) expr;
      ListItor args;
      ListIterator(call->args, &args);
      ListItor params;
      ListIterator(call->function->params, &params);
      while (ListHasMore(&args) && ListHasMore(&params)) {
        struct Expr* arg = (struct Expr*) ListNext(&args);
        struct Variable* param = (struct Variable*) ListNext(&params);
        errors += check_expr(arg);
        if (!assignable(param->type, arg->type)) {
          fprintf(stderr, "Type mismatch for parameter %s in call to function %s\n", param->name, call->name);
          errors++;
        }
      }

      if (ListHasMore(&args) || ListHasMore(&params)) {
        fprintf(stderr, "Number of arguments in call to function %s does not match\n", call->name);
        errors++;
      }

      expr->type = call->function->ret->type;
    }
    break;

    case E_LVALUE: {
      struct LValueExpr* e = (struct LValueExpr*) expr;
      errors += check_lvalue(e->lval);
      expr->type = e->lval->type;
    }
    break;

    case E_INT_LITERAL: {
      expr->type = &int_type;
    }
    break;

    case E_DOUBLE_LITERAL: {
      expr->type = &double_type;
    }
    break;

    case E_STRING_LITERAL: {
      expr->type = &string_type;
    }
    break;
  }

  return errors;
}

static int check_stmts(List* stmts)
{
  int errors = 0;
  ListItor it;
  ListIterator(stmts, &it);
  while (ListHasMore(&it)) {
    struct Stmt* n = (struct Stmt*) ListNext(&it);
    switch (n->clazz) {
      case S_IF: {
        struct IfStmt* s = (struct IfStmt*) n;
        errors += check_expr(s->cond);
        errors += check_stmts(s->if_true);
        errors += check_stmts(s->if_false);
      }
      break;

      case  S_WHILE: {
        struct WhileStmt* s = (struct WhileStmt*) n;
        errors += check_expr(s->cond);
        errors += check_stmts(s->stmts);
      }
      break;

      case S_CALL: {
        struct CallStmt* s = (struct CallStmt*) n;
        errors += check_expr((struct Expr*) s->call);
      }
      break;

      case S_ASSIGN: {
        struct Assignemt* s = (struct Assignemt*) n;
        errors += check_lvalue((struct LValue*) s->lval);
        errors += check_expr(s->expr);
        if (!assignable(s->lval->type, s->expr->type)) {
          fprintf(stderr, "Incompatible types in assignment\n");
          errors++;
        }
      }
      break;
    }
  }

  return errors;
}

int check_types(struct Program* program)
{
  int errors = 0;
  ListItor it;
  ListIterator(program->functions, &it);
  while (ListHasMore(&it)) {
    struct Function* f = (struct Function*) ListNext(&it);
    errors += check_stmts(f->stmts);
  }
  return errors;
}
