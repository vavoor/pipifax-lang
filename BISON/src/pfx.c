#include <stdio.h>

extern int yyparse(void);
extern FILE* yyin;

int main(int argc, const char* argv[]) {
  int errors = 0;
  yyin = fopen(argv[1], "r");
  if (yyin != NULL) {
    errors += yyparse() != 0;
    fclose(yyin);
  }
  else {
    errors++;
  }

  return errors != 0;
}
