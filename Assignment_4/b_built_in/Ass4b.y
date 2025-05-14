%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

void yyerror(const char *s);
int yylex(void);
%}

%union {
    double fval;
    char* sval;
}

%token <fval> NUMBER
%token <sval> STRING

%token SIN COS TAN SQRT LOG EXP POW STRLEN

%left '+' '-'
%left '*' '/'
%right '^'

%type <fval> expr

%%

input:
    expr '\n' { printf("Result = %f\n", $1); }
    ;

expr:
      expr '+' expr      { $$ = $1 + $3; }
    | expr '-' expr      { $$ = $1 - $3; }
    | expr '*' expr      { $$ = $1 * $3; }
    | expr '/' expr      {
                            if ($3 == 0) {
                                yyerror("Division by zero!");
                                $$ = 0;
                            } else $$ = $1 / $3;
                         }
    | expr '^' expr      { $$ = pow($1, $3); }
    | '(' expr ')'       { $$ = $2; }
    | NUMBER             { $$ = $1; }

    /* built-in functions */
    | SIN '(' expr ')'   { $$ = sin($3); }
    | COS '(' expr ')'   { $$ = cos($3); }
    | TAN '(' expr ')'   { $$ = tan($3); }
    | SQRT '(' expr ')'  { $$ = sqrt($3); }
    | LOG '(' expr ')'   { $$ = log($3); }
    | EXP '(' expr ')'   { $$ = exp($3); }
    | POW '(' expr ',' expr ')' { $$ = pow($3, $5); }
    | STRLEN '(' STRING ')'     { $$ = strlen($3); free($3); }
    ;

%%

int main() {
    printf("Enter expression:\n");
    return yyparse();
}

void yyerror(const char *s) {
    fprintf(stderr, "Error: %s\n", s);
}
