%{
    #include<stdio.h>
    #include<math.h>
    int flag = 0;
    void yyerror(const char *s);
    int yylex(void);
%}

%union {
    double dval;
}

%token <dval> NUMBER

%left '+' '-'
%left '*' '/' '%'
%left '(' ')'

%type <dval> E

%%
ArithmeticExpression: E {
         printf("\nResult = %lf\n", $1);
         return 0;
        }
;

E: E '+' E { $$ = $1 + $3; }
 | E '-' E { $$ = $1 - $3; }
 | E '*' E { $$ = $1 * $3; }
 | E '/' E { $$ = $1 / $3; }
 | E '%' E { $$ = fmod($1, $3); }
 | '-' E   { $$ = -$2; }
 | '(' E ')' { $$ = $2; }
 | NUMBER { $$ = $1; }
;

%%

int main()
{
   printf("\nEnter Any Arithmetic Expression which can have operations Addition, Subtraction, Multiplication, Division, Modulus and Round brackets:\n");
   yyparse();
   if (flag == 0)
       printf("\nEntered arithmetic expression is Valid\n\n");
   return 0;
}

void yyerror(const char *s)
{
   fprintf(stderr, "\nEntered arithmetic expression is Invalid: %s\n\n", s);
   flag = 1;
}
