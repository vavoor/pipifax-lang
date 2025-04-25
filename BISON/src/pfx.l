%{
#include "list.h"
#include "pfx.tab.h"
#include <stdlib.h>
#include <string.h>
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

{INT}"."{D0}+{EXP}?   { yylval.d = atof(yytext); return T_DOUBLE_LITERAL; }
{INT}{EXP}            { yylval.d = atof(yytext); return T_DOUBLE_LITERAL; }
{INT}                 { yylval.i = atoi(yytext); return T_INT_LITERAL; }
\"([^"]|\\.)*\"       { yylval.s = strndup(&yytext[1], yyleng-2); return T_STRING_LITERAL; }

{L}{LD}*              { yylval.s = strdup(yytext); return T_NAME; }

{WS}                  /* ignore */
"#"[^\n]*             /* ignore */

.                     return yytext[0];
