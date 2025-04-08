grammar Pfx;

/* Grammar rules */

program
  : (globalVariable | functionDefinition )* EOF
  ;

globalVariable
  : 'var' Name type
  ;

functionDefinition
  : 'func' Name '(' ')' block
  ;

block
  : '{' statement* '}'
  ;

statement
  : Name '=' expr   # AssignmentStmt
  | Name '(' ')'    # CallStmt
  ;

type
  : 'int'   # IntType
  | '[' IntNumber ']' type  # ArrayType
  ;

expr
  : expr '+' expr   # AddExpr
  | IntNumber # IntLiteralExpr
  | Name      # VariableExpr
  ;

/* Lexer rules */

fragment
Digit0
  : [0-9]
  ;

fragment
Digit1
  : [1-9]
  ;
  
fragment
Letter
  : [_A-Za-z]
  ;

fragment
LetterOrDigit
  : ( Letter | Digit0 )
  ;
  
Name
  : Letter LetterOrDigit*
  ;

IntNumber
  : '0' | Digit1 Digit0*
  ;

Comment
  : '#' ~[\n\r]* -> skip
  ;
  
Whitespace
  : [\p{White_Space}]+ -> skip
  ;
