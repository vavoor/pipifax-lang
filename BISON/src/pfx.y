%{
#include <stdio.h>

int yylex(void);
extern int yylineno;
void yyerror(const char* msg);
%}

%token T_FUNC T_VAR T_IF T_ELSE T_WHILE
%token T_INT T_DOUBLE T_STRING
%token T_LE T_GE T_EQ T_NE T_CMP T_AND T_OR
%token T_NAME
%token T_INT_LITERAL
%token T_DOUBLE_LITERAL
%token T_STRING_LITERAL

%start program

%%

program
  : %empty
  | program var_decl
  | program func_def
  ;

var_decl
  : T_VAR T_NAME type
  ;

func_def
  : T_FUNC T_NAME '(' opt_param_list ')' opt_type block
  ;

opt_param_list
  : %empty
  | param_list
  ;

param_list
  : param
  | param_list ',' param
  ;

param
  : T_NAME ptype
  ;

opt_type
  : %empty
  | type
  ;

block
  : '{' stmts_or_vars '}'
  ;

stmts_or_vars
  : %empty
  | stmts_or_vars stmt
  | stmts_or_vars local_var_decl
  ;

local_var_decl
  : T_VAR T_NAME type
  ;

stmt
  : T_IF expr block
  | T_IF expr block T_ELSE block
  | T_WHILE expr block
  | lvalue '=' expr
  | call
  ;

type
  : T_INT
  | T_DOUBLE
  | T_STRING
  | '[' T_INT_LITERAL ']' type
  ;

ptype
  : type
  | '*' '[' ']' type
  ;

expr
  : or_expr
  ;

or_expr
  : and_expr
  | or_expr T_OR and_expr
  ;

and_expr
  : cmp_expr
  | and_expr T_AND cmp_expr
  ;

cmp_expr
  : add_expr
  | add_expr '<' add_expr
  | add_expr '>' add_expr
  | add_expr T_LE add_expr
  | add_expr T_GE add_expr
  | add_expr T_EQ add_expr
  | add_expr T_NE add_expr
  | add_expr T_CMP add_expr
  ;

add_expr
  : mult_expr
  | add_expr '+' mult_expr
  | add_expr '-' mult_expr
  ;

mult_expr
  : unary_expr
  | mult_expr '*' unary_expr
  | mult_expr '/' unary_expr
  ;

unary_expr
  : value_expr
  | '-' unary_expr
  | '!' unary_expr
  | '(' T_INT ')' unary_expr
  | '(' T_DOUBLE ')' unary_expr
  ;

value_expr
  : T_INT_LITERAL
  | T_DOUBLE_LITERAL
  | T_STRING_LITERAL
  | call
  | lvalue
  | '(' expr ')'
  ;

lvalue
  : T_NAME
  | lvalue '[' expr ']'
  ;

call
  : T_NAME '(' opt_expr_list ')'
  ;

opt_expr_list
  : %empty
  | expr_list
  ;

expr_list
  : expr
  | expr_list ',' expr
  ;

%%

void yyerror(const char* msg)
{
  fprintf(stderr, "Line %d: %s\n", yylineno, msg);
}
