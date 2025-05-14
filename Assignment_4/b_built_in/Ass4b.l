%{
#include "Ass4b.tab.h"
#include <string.h>
#include <stdlib.h>
%}

%%

"sin"       { return SIN; }
"cos"       { return COS; }
"tan"       { return TAN; }
"sqrt"      { return SQRT; }
"log"       { return LOG; }
"exp"       { return EXP; }
"pow"       { return POW; }
"strlen"    { return STRLEN; }

[0-9]+(\.[0-9]+)?    { yylval.fval = atof(yytext); return NUMBER; }

\"[^\"]*\"          {
                      yytext[yyleng - 1] = '\0';
                      yylval.sval = strdup(yytext + 1);
                      return STRING;
                    }

[ \t]       { /* ignore whitespace */ }
\n          { return '\n'; }
","         { return ','; }
.           { return yytext[0]; }

%%

int yywrap(void) {
    return 1;
}


