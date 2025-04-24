%{
#include "pfx.tab.h"
%}

D0    [0-9]
D1    [1-9]
INT   0|{D1}{D0}*
EXP   [eE][-+]?{D0}+
L     [A-Za-z_]
LD    [A-Za-z_0-9]
WS    [ \t\n\r\f]

%option yylineno noyywrap

%%

"func"                return T_FUNC;
"var"                 return T_VAR;
"int"                 return T_INT;
"double"              return T_DOUBLE;
"string"              return T_STRING;
"if"                  return T_IF;
"else"                return T_ELSE;
"while"               return T_WHILE;
"<="                  return T_LE;
">="                  return T_GE;
"=="                  return T_EQ;
"!="                  return T_NE;
"<=>"                 return T_CMP;
"&&"                  return T_AND;
"||"                  return T_OR;

{INT}"."{D0}+{EXP}?   { return T_DOUBLE_LITERAL; }
{INT}{EXP}            { return T_DOUBLE_LITERAL; }
{INT}                 { return T_INT_LITERAL; }
\"([^"]|\\.)*\"       { return T_STRING_LITERAL; }

{L}{LD}*              { return T_NAME; }

{WS}                  /* ignore */
"#"[^\n]*             /* ignore */
.                     return yytext[0];
