%{
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>

void convert_case(char *str);
int yylex();
int yyerror(char *s);
%}

%union {
    char* str;
}

%token <str> WORD

%%
input:
      input WORD   { convert_case($2); free($2); }
    | /* empty */
    ;
%%

void convert_case(char *str) {
    for (int i = 0; str[i]; i++) {
        if (islower(str[i]))
            str[i] = toupper(str[i]);
        else if (isupper(str[i]))
            str[i] = tolower(str[i]);
    }
    printf("%s\n", str);
}

int main() {
    printf("Enter input:\n");
    yyparse();
    return 0;
}

int yyerror(char *s) {
    fprintf(stderr, "Error: %s\n", s);
    return 0;
}
