grammar Pfx;

/* Grammar rules */

program
    : EOF
    ;

/* Lexer rules */

Comment : '#' ~[\n\r]* -> skip ;
Whitespace : [ \t\f\n\r]+ -> skip;
