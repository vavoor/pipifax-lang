%{
#include <stdio.h>
#include "ast.h"

int yylex(void);
extern int yylineno;
void yyerror(const char* msg);

static void enter_scope(void);
static void exit_scope(void);
static void* lookup(const char* kay);
static void* insert(const char* key, void* value);

List* scopes;
%}

%union {
  const char* s;
  int i;
  double d;

  List* l;

  struct Type* type;
  struct Variable* var;
  struct Expr* expr;
  struct LValue* lval;
  struct Stmt* stmt;
}

%token T_FUNC T_VAR T_IF T_ELSE T_WHILE
%token T_INT T_DOUBLE T_STRING
%token T_LE T_GE T_EQ T_NE T_CMP T_AND T_OR
%token<s> T_NAME
%token<i> T_INT_LITERAL
%token<d> T_DOUBLE_LITERAL
%token<s> T_STRING_LITERAL

%type<type> type ptype opt_type
%type<expr> expr or_expr and_expr cmp_expr add_expr mult_expr unary_expr value_expr call
%type<lval> lvalue
%type<l> opt_expr_list expr_list opt_param_list param_list block stmts_or_vars
%type<var> param
%type<stmt> stmt

%start program

%%

program
  : %empty
    { program = AstProgram(); scopes = ListMake(); enter_scope(); }
  | program var_decl
  | program func_def
  ;

var_decl
  : T_VAR T_NAME type
    { struct Variable* v = AstGlobalVariable($2, $3); ListAppend(program->globals, v); }
  ;

func_def
  : T_FUNC T_NAME '(' opt_param_list ')' opt_type
    { function = AstFunction($2, $4, $6); ListAppend(program->functions, function); }
    block
  ;

opt_param_list
  : %empty
    { $$ = ListMake(); }
  | param_list
  ;

param_list
  : param
    { $$ = ListMake(); ListAppend($$, $1); }
  | param_list ',' param
    { $$ = $1; ListAppend($$, $3); }
  ;

param
  : T_NAME ptype
    { $$ = AstParameter($1, $2); }
  ;

opt_type
  : %empty
    { $$ = &void_type; }
  | type
  ;

block
  : '{' stmts_or_vars '}'
    { $$ = $2; }
  ;

stmts_or_vars
  : %empty
    { $$ = ListMake(); }
  | stmts_or_vars stmt
    { $$ = $1; ListAppend($$, $2); }
  | stmts_or_vars local_var_decl
    { $$ = $1; }
  ;

local_var_decl
  : T_VAR T_NAME type
    { struct Variable* v = AstLocalVariable($2, $3); ListAppend(function->locals, v); }
  ;

stmt
  : T_IF expr block
    { $$ = AstIfStmt($2, $3, ListMake()); }
  | T_IF expr block T_ELSE block
    { $$ = AstIfStmt($2, $3, $5); }
  | T_WHILE expr block
    { $$ = AstWhileStmt($2, $3); }
  | lvalue '=' expr
    { $$ = AstAssignment($1, $3); }
  | call
    { $$ = AstCallStmt((struct CallExpr*) $1); }
  ;

type
  : T_INT
    { $$ = &int_type; }
  | T_DOUBLE
    { $$ = &double_type; }
  | T_STRING
    { $$ = &string_type; }
  | '[' T_INT_LITERAL ']' type
    { $$ = AstArrayType($2, $4); }
  ;

ptype
  : type
  | '*' '[' ']' type
    { $$ = AstRefType(AstArrayType(-1, $4)); }
  ;

expr
  : or_expr
  ;

or_expr
  : and_expr
  | or_expr T_OR and_expr
    { $$ = AstBinaryExpr(E_OR, $1, $3); }
  ;

and_expr
  : cmp_expr
  | and_expr T_AND cmp_expr
    { $$ = AstBinaryExpr(E_AND, $1, $3); }
  ;

cmp_expr
  : add_expr
  | add_expr '<' add_expr
    { $$ = AstBinaryExpr(E_LT, $1, $3); }
  | add_expr '>' add_expr
    { $$ = AstBinaryExpr(E_GT, $1, $3); }
  | add_expr T_LE add_expr
    { $$ = AstBinaryExpr(E_LE, $1, $3); }
  | add_expr T_GE add_expr
    { $$ = AstBinaryExpr(E_GE, $1, $3); }
  | add_expr T_EQ add_expr
    { $$ = AstBinaryExpr(E_EQ, $1, $3); }
  | add_expr T_NE add_expr
    { $$ = AstBinaryExpr(E_NE, $1, $3); }
  | add_expr T_CMP add_expr
    { $$ = AstBinaryExpr(E_CMP, $1, $3); }
  ;

add_expr
  : mult_expr
  | add_expr '+' mult_expr
    { $$ = AstBinaryExpr(E_ADD, $1, $3); }
  | add_expr '-' mult_expr
    { $$ = AstBinaryExpr(E_SUB, $1, $3); }
  ;

mult_expr
  : unary_expr
  | mult_expr '*' unary_expr
    { $$ = AstBinaryExpr(E_MUL, $1, $3); }
  | mult_expr '/' unary_expr
    { $$ = AstBinaryExpr(E_DIV, $1, $3); }
  ;

unary_expr
  : value_expr
  | '-' unary_expr
    { $$ = AstUnaryExpr(E_NEG, $2); }
  | '!' unary_expr
    { $$ = AstUnaryExpr(E_NOT, $2); }
  | '(' T_INT ')' unary_expr
    { $$ = AstUnaryExpr(E_INT_CAST, $4); }
  | '(' T_DOUBLE ')' unary_expr
    { $$ = AstUnaryExpr(E_DOUBLE_CAST, $4); }
  ;

value_expr
  : T_INT_LITERAL
    { $$ = AstIntLiteralExpr($1); }
  | T_DOUBLE_LITERAL
    { $$ = AstDoubleLiteralExpr($1); }
  | T_STRING_LITERAL
    { $$ = AstStringLiteralExpr($1); }
  | call
  | lvalue
    { $$ = AstLValueExpr($1); }
  | '(' expr ')'
    { $$ = $2; }
  ;

lvalue
  : T_NAME
    { $$ = AstNamedLValue($1); }
  | lvalue '[' expr ']'
    { $$ = AstIndexedLValue($1, $3); }
  ;

call
  : T_NAME '(' opt_expr_list ')'
    { $$ = AstCallExpr($1, $3); }
  ;

opt_expr_list
  : %empty
    { $$ = ListMake(); }
  | expr_list
  ;

expr_list
  : expr
    { $$ = ListMake(); ListAppend($$, $1); }
  | expr_list ',' expr
    { $$ = $1; ListAppend($$, $3); }
  ;

%%

void yyerror(const char* msg)
{
  fprintf(stderr, "Line %d: %s\n", yylineno, msg);
}

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

static void* insert(const char* key, void* value)
{
  Map* m = ListTop(scopes);
  if (m != NULL) {
    void* previous = MapPut(m, key, value);
    return previous;
  }
  return NULL; /* TODO : is this correct? */
}

