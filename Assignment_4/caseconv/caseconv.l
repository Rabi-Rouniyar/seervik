%{
#include "caseconv.tab.h"
#include<string.h>
%}

%%
[A-Za-z]+        { yylval.str = strdup(yytext); return WORD; }
[ \t\n]          ; // Ignore whitespace
.                ; // Ignore other characters
%%

int yywrap() {
    return 1;
}
