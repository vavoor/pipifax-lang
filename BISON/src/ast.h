#ifndef HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3
#define HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3

#include "list.h"
#include "map.h"

struct Node {
  int clazz;
};

/* ******************* Types ************************** */
enum { TY_ERROR = 100, TY_VOID, TY_INT, TY_DOUBLE, TY_STRING, TY_ARRAY, TY_REF };

struct Type {
  int clazz;
  int size;
};

struct ArrayType {
  struct Type super;
  int dim;
  struct Type* base;
};

struct RefType {
  struct Type super;
  struct Type* base;
};

/* ******************* Expressions & LValues ************************** */
enum { LV_NAMED = 200, LV_INDEXED };
struct LValue {
  int clazz;
  struct Type* type;

  const char* reg;
};

struct NamedLValue {
  struct LValue super;
  const char* name;

  struct Variable* variable;
};

struct IndexedLValue {
  struct LValue super;
  struct LValue* base;
  struct Expr* index;
};

enum { E_OR = 300, E_AND, E_ADD, E_SUB, E_MUL, E_DIV,
  E_LE, E_LT, E_GE, E_GT, E_EQ, E_NE, E_CMP,
  E_NOT, E_NEG, E_INT_CAST, E_DOUBLE_CAST,
  E_CALL, E_LVALUE,
  E_INT_LITERAL, E_DOUBLE_LITERAL, E_STRING_LITERAL };

struct Expr {
  int clazz;
  struct Type* type;

  const char* reg;
};

struct BinaryExpr {
  struct Expr super;
  struct Expr* l;
  struct Expr* r;
};

struct UnaryExpr {
  struct Expr super;
  struct Expr* expr;
};

struct LValueExpr {
  struct Expr super;
  struct LValue* lval;
};

struct CallExpr {
  struct Expr super;
  const char* name;
  List* args;
  struct Function* function;
};

struct LiteralExpr {
  struct Expr super;
  union {
    int i;
    double d;
    const char* s;
  } u;
};

/* ******************* Statements ************************** */
enum { S_IF = 400, S_WHILE, S_CALL, S_ASSIGN };
struct Stmt {
  int clazz;
};

struct Assignemt {
  struct Stmt super;
  struct LValue* lval;
  struct Expr* expr;
};

struct IfStmt {
  struct Stmt super;
  struct Expr* cond;
  List* if_true;
  List* if_false;
};

struct WhileStmt {
  struct Stmt super;
  struct Expr* cond;
  List* stmts;
};

struct CallStmt {
  struct Stmt super;
  struct CallExpr* call;
};

/* ******************* Variables ************************** */

enum { V_GLOBAL = 500, V_LOCAL, V_PARAMETER };
struct Variable {
  int clazz;
  const char* name;
  struct Type* type;
  int offset;
};

struct GlobalVariable {
  struct Variable super;
};

struct LocalVariable {
  struct Variable super;
};

struct Parameter {
  struct Variable super;
};

/* ********************************************* */

struct Program {
  List* globals;
  List* functions;
};

struct Function {
  const char* name;
  struct Variable* ret;
  List* params;
  List* locals;
  List* stmts;

  int args_size;
  int frame_size;
};

extern struct Type int_type;
extern struct Type double_type;
extern struct Type string_type;
extern struct Type void_type;
extern struct Type error_type;

extern struct Program* program;
struct Program* AstProgram(void);

struct Function* AstFunction(const char* name, List* params, struct Type* type, List* stmts);

struct Variable* AstGlobalVariable(const char* name, struct Type* type);
struct Variable* AstLocalVariable(const char* name, struct Type* type);
struct Variable* AstParameter(const char* name, struct Type* type);

struct Type* AstArrayType(int dim, struct Type* base);
struct Type* AstRefType(struct Type* base);

struct Expr* AstBinaryExpr(int clazz, struct Expr* left, struct Expr* right);
struct Expr* AstUnaryExpr(int clazz, struct Expr* expr);
struct Expr* AstIntLiteralExpr(int value);
struct Expr* AstDoubleLiteralExpr(double value);
struct Expr* AstStringLiteralExpr(const char* value);
struct Expr* AstLValueExpr(struct LValue *lval);
struct Expr* AstCallExpr(const char* name, List* args);

struct LValue* AstNamedLValue(const char* name);
struct LValue* AstIndexedLValue(struct LValue* base, struct Expr* index);

struct Stmt* AstAssignment(struct LValue* lval, struct Expr* expr);
struct Stmt* AstIfStmt(struct Expr* cond, List* if_true, List* if_false);
struct Stmt* AstWhileStmt(struct Expr* cond, List* stmts);
struct Stmt* AstCallStmt(struct CallExpr* call);

#endif /* HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3 */
