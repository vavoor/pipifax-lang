grammar Pfx;

/* Grammar rules */

program
  : globalVariable* EOF
  ;

globalVariable
  : 'var' Name type
  ;

type
  : 'int'
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

Comment
  : '#' ~[\n\r]* -> skip
  ;
  
Whitespace
  : [\p{White_Space}]+ -> skip
  ;
