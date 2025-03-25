grammar Pfx;

/* Grammar rules */

program
  : EOF
  ;

/* Lexer rules */

Comment : '#' ~[\n\r]* -> skip ;
Whitespace : [\p{White_Space}]+ -> skip;
