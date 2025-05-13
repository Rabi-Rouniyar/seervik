%{
#include <stdlib.h>
#include <math.h>
#include <string.h>
#include "Ass4b.tab.h"
char strbuf[256];
%}

%%

[a-z]               { yylval.varindex = yytext[0] - 'a'; return NAME; }
[0-9]+"."[0-9]+     { yylval.nvalue = atof(yytext); return NUMBER; }
[0-9]+              { yylval.nvalue = atof(yytext); return NUMBER; }
"abs"[ \t]*\(       { return ABS; }
"sqrt"[ \t]*\(      { return SQRT; }
"strlen"[ \t]*\(    { return STRLEN; }
"sin"[ \t]*\(       { return SIN; }
"cos"[ \t]*\(       { return COS; }
"pow"[ \t]*\(       { return POW; }
"log"[ \t]*\(       { return LOG; }
\"[^\"]*\"          {
                        strncpy(strbuf, yytext+1, yyleng-2);
                        strbuf[yyleng-2] = '\0';
                        yylval.svalue = strdup(strbuf);
                        return STRING;
                    }
[ \t]               ; // Ignore whitespace
\n                  { return '\n'; }
.                   { return yytext[0]; }

%%

int yywrap() { return 1; }
