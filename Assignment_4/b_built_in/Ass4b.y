%{
#include <stdio.h>
#include <math.h>
#include <stdlib.h>
#include <string.h>
int yylex(void);
void yyerror(const char *msg)
{
    printf("ERROR(PARSER): %s\n", msg);
}
long variables[26];
%}

%union {
    float nvalue;
    int ivalue;
    int varindex;
    char* svalue;
}

%token <nvalue> NUMBER
%token <ivalue> INT
%token <varindex> NAME
%token <svalue> STRING
%token ABS SQRT STRLEN SIN COS POW LOG
%type <nvalue> expr
%type <nvalue> term
%type <nvalue> varOrNum
%type <svalue> strexpr

%%

statementList : statement '\n'
              | statement '\n' statementList
              ;

statement : NAME '=' expr { variables[$1] = $3; }
          | expr { printf("RESULT: %f\n", $1); }
          | strexpr { printf("RESULT: %s\n", $1); free($1); }
          ;

expr: expr '+' term { $$ = $1 + $3; }
    | expr '-' term { $$ = $1 - $3; }
    | term { $$ = $1; }
    ;

term: '-' term { $$ = 0 - $2; }
    | ABS expr ')' { $$ = fabs($2); }
    | SQRT expr ')' { $$ = sqrt($2); }
    | STRLEN strexpr ')' { $$ = strlen($2); free($2); }
    | SIN expr ')' { $$ = sin($2); }
    | COS expr ')' { $$ = cos($2); }
    | POW expr ',' expr ')' { $$ = pow($2, $4); }
    | LOG expr ')' { $$ = log($2); }
    | term '*' varOrNum { $$ = $1 * $3; }
    | term '/' varOrNum { $$ = $1 / $3; }
    | varOrNum { $$ = $1; }
    ;

varOrNum : NUMBER { $$ = $1; }
         | NAME { $$ = variables[$1]; }
         | ABS expr ')' { $$ = fabs($2); }
         | SQRT expr ')' { $$ = sqrt($2); }
         | SIN expr ')' { $$ = sin($2); }
         | COS expr ')' { $$ = cos($2); }
         | POW expr ',' expr ')' { $$ = pow($2, $4); }
         | LOG expr ')' { $$ = log($2); }
         ;

strexpr : STRING { $$ = $1; }
        ;

%%

int main() {
    int i;
    for (i=0; i<26; i++) variables[i] = 0;
    yyparse();
    return 0;
}
