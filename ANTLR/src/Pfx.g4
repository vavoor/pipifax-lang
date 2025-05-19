grammar Pfx;

/* Grammar rules */

program
  : (globalVariable | functionDefinition )* EOF
  ;

globalVariable
  : 'var' Name type
  ;

functionDefinition
  : 'func' Name '(' ( param ( ',' param )* )? ')' type? block
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
  | 'if' expr block ( 'else' block)?  # IfStmt
  | 'while' expr block # WhileStmt
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
  | 'double'                # DoubleType
  | 'string'                # StringType
  | '[' IntNumber ']' type  # ArrayType
  ;

ptype
  : type
  | refType
  ;

refType
  : '*' '[' ']' type
  ;

expr
  : expr ('*' | '/') expr   # MultiplicativeExpr
  | expr ('+' | '-') expr   # AdditiveExpr
  | expr ('<' | '>' | '<=' | '>=' | '==' | '!=' | '<=>') expr  #ComparativeExpr
  | expr '&&' expr    # AndExpr
  | expr '||' expr    # OrExpr
  | '!' expr          # NotExpr
  | '-' expr          # NegExpr
  | '(' 'int' ')' expr    # IntCastExpr
  | '(' 'double' ')' expr # DoubleCastExpr
  | '(' expr ')'          # BracketExpr
  | IntNumber               # IntLiteralExpr
  | DoubleNumber            # DoubleLiteralExpr
  | StringLiteral           # StringLiteralExpr
  | call                    # CallExpr
  | lvalue                  # LValueExpr
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
Exp
  : [eE] ('+'|'-')? Digit0+
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

DoubleNumber
  : IntNumber ('.' Digit0+ Exp? | Exp)
  ;
  
IntNumber
  : '0' | Digit1 Digit0*
  ;

StringLiteral
  : '"' (~["]|'\\' .)* '"'
  ;

Comment
  : '#' ~[\n\r]* -> skip
  ;
  
Whitespace
  : [\p{White_Space}]+ -> skip
  ;
