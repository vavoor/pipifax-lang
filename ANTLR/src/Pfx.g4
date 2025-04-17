grammar Pfx;

/* Grammar rules */

program
  : (globalVariable | functionDefinition )* EOF
  ;

globalVariable
  : 'var' Name type
  ;

functionDefinition
  : 'func' Name '(' ( param ( ',' param )* )? ')' block
  ;

param
  : Name ptype
  ;

block
  : '{' statementOrDeclaration* '}'
  ;

statementOrDeclaration
  : statement
  | localVariable
  ;

statement
  : lvalue '=' expr   # AssignmentStmt
  | call              # CallStmt
  ;

lvalue
  : Name                # NamedLValue
  | lvalue '[' expr ']' # IndexedLValue
  ;

localVariable
  : 'var' Name type
  ;
  
type
  : 'int'                   # IntType
  | '[' IntNumber ']' type  # ArrayType
  ;

ptype
  : type
  ;

expr
  : expr '+' expr   # AddExpr
  | IntNumber       # IntLiteralExpr
  | lvalue          # LValueExpr
  ;

call
  : Name '(' ( expr ( ',' expr)* )? ')'
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
