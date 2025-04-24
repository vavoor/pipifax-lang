/* A Bison parser, made by GNU Bison 3.7.4.  */

/* Bison interface for Yacc-like parsers in C

   Copyright (C) 1984, 1989-1990, 2000-2015, 2018-2020 Free Software Foundation,
   Inc.

   This program is free software: you can redistribute it and/or modify
   it under the terms of the GNU General Public License as published by
   the Free Software Foundation, either version 3 of the License, or
   (at your option) any later version.

   This program is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
   GNU General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this program.  If not, see <http://www.gnu.org/licenses/>.  */

/* As a special exception, you may create a larger work that contains
   part or all of the Bison parser skeleton and distribute that work
   under terms of your choice, so long as that work isn't itself a
   parser generator using the skeleton or a modified version thereof
   as a parser skeleton.  Alternatively, if you modify or redistribute
   the parser skeleton itself, you may (at your option) remove this
   special exception, which will cause the skeleton and the resulting
   Bison output files to be licensed under the GNU General Public
   License without this special exception.

   This special exception was added by the Free Software Foundation in
   version 2.2 of Bison.  */

/* DO NOT RELY ON FEATURES THAT ARE NOT DOCUMENTED in the manual,
   especially those whose name start with YY_ or yy_.  They are
   private implementation details that can be changed or removed.  */

#ifndef YY_YY_PFX_TAB_H_INCLUDED
# define YY_YY_PFX_TAB_H_INCLUDED
/* Debug traces.  */
#ifndef YYDEBUG
# define YYDEBUG 0
#endif
#if YYDEBUG
extern int yydebug;
#endif

/* Token kinds.  */
#ifndef YYTOKENTYPE
# define YYTOKENTYPE
  enum yytokentype
  {
    YYEMPTY = -2,
    YYEOF = 0,                     /* "end of file"  */
    YYerror = 256,                 /* error  */
    YYUNDEF = 257,                 /* "invalid token"  */
    T_FUNC = 258,                  /* T_FUNC  */
    T_VAR = 259,                   /* T_VAR  */
    T_IF = 260,                    /* T_IF  */
    T_ELSE = 261,                  /* T_ELSE  */
    T_WHILE = 262,                 /* T_WHILE  */
    T_INT = 263,                   /* T_INT  */
    T_DOUBLE = 264,                /* T_DOUBLE  */
    T_STRING = 265,                /* T_STRING  */
    T_LE = 266,                    /* T_LE  */
    T_GE = 267,                    /* T_GE  */
    T_EQ = 268,                    /* T_EQ  */
    T_NE = 269,                    /* T_NE  */
    T_CMP = 270,                   /* T_CMP  */
    T_AND = 271,                   /* T_AND  */
    T_OR = 272,                    /* T_OR  */
    T_NAME = 273,                  /* T_NAME  */
    T_INT_LITERAL = 274,           /* T_INT_LITERAL  */
    T_DOUBLE_LITERAL = 275,        /* T_DOUBLE_LITERAL  */
    T_STRING_LITERAL = 276         /* T_STRING_LITERAL  */
  };
  typedef enum yytokentype yytoken_kind_t;
#endif

/* Value type.  */
#if ! defined YYSTYPE && ! defined YYSTYPE_IS_DECLARED
typedef int YYSTYPE;
# define YYSTYPE_IS_TRIVIAL 1
# define YYSTYPE_IS_DECLARED 1
#endif


extern YYSTYPE yylval;

int yyparse (void);

#endif /* !YY_YY_PFX_TAB_H_INCLUDED  */
