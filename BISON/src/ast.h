#ifndef HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3
#define HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3

#include "list.h"

enum { E_OR, E_AND, E_ADD, E_SUB, E_MUL, E_DIV,
  E_NOT, E_NEG, E_INT, E_DOUBLE,
  E_LE, E_LT, E_GE, E_GT, E_EQ, E_NE, E_CMP };
struct Expr {
  int type;
};

enum { S_IF, S_WHILE, S_CALL, S_ASSIGN };
struct Stmt {
  int type;
};

struct Program {
  List* globals;
  List* functions;
};


#endif /* HEADER_4d0ed137_ccd9_4761_a4e5_d869e0364eb3 */
