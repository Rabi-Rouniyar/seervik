%{
#include<stdio.h>
#include "Ass4a.tab.h"
%}

%%

[0-9]+"."[0-9]+   { yylval.dval = atof(yytext); return NUMBER; }
[0-9]+            { yylval.dval = atof(yytext); return NUMBER; }
[\t]              ;
[\n]              { return 0; }
.                 { return yytext[0]; }

%%

int yywrap()
{
    return 1;
}
