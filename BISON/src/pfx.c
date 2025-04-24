#include <stdio.h>

extern int yyparse(void);
extern FILE* yyin;

int main(int argc, const char* argv[]) {
  if (argc != 2) {
    fprintf(stderr,"Usage: pfxc FILE\n");
    goto exit;
  }

  yyin = fopen(argv[1], "r");
  if (yyin == NULL) {
    fprintf(stderr, "Cannot open file %s\n", argv[1]);
    goto exit;
  }

  if (yyparse() != 0) {
    goto exit;
  }

  fclose(yyin);

  return 0;

exit:
  return 1;
}
