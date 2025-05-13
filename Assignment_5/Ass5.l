%{
#include "Ass5.tab.h"
%}
%%

[0-9]+      { yylval.sym = (char)yytext[0]; return NUMBER; }
[a-zA-Z]+   { yylval.sym = (char)yytext[0]; return LETTER; }
[\^]        { return '^'; }
[ \t\r]+    ; // Ignore whitespace
\n          { return 0; }
.           { return yytext[0]; }
%%
int yywrap()
{
    return 1;
}
