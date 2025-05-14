%{
#include <stdio.h>
int yylex(void);
int yyerror(char *s);
int valid = 0;  // Global flag to ensure single output
%}

%token NEW UD WS ID OPEN_SQ CLOSE_SQ EQ COMMA DIGIT INVALID EOL

%%
start : declaration EOL {
            if (!valid) {
                printf("Valid Declaration\n");
                valid = 1;
            }
        }
      | error EOL {
            if (!valid) {
                printf("Not Valid Declaration\n");
                valid = 1;
            }
        }
      ;

declaration : varlist UD DIGIT         { valid = 0; }
            | varlist                  { valid = 0; }
            | varlist UD varlist       { valid = 0; }
            | varlist WS varlist       { printf("Not Valid Declaration\n"); valid = 1; }
            ;

varlist : ID
        | varlist COMMA ID
        ;
%%

int main()
{
    printf("Enter variable declaration: ");
    yyparse();
    return 0;
}

int yywrap()
{
    return 1;
}

int yyerror(char *s)
{
    return 1;
}
