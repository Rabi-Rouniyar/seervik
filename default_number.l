%{
#include <stdio.h>
%}

IDENTIFIER    [a-zA-Z_][a-zA-Z0-9_]*
NUMBER        [0-9]+
%%
"int"|"char"|"float"|"printf"                   { printf("%s -> Keyword\n", yytext); }
{IDENTIFIER}                                    { printf("%s -> Identifier\n", yytext); }
{NUMBER}                                        { printf("%s -> Constant\n", yytext); }
"="                                             { printf("= -> Assignment Operator\n"); }
"=="|"!="|"<"|">"|"<="|">="                     { printf("%s -> Relational Operator\n", yytext); }
"+"|"-"|"*"|"/"                                 { printf("%s -> Arithmetic Operator\n", yytext); }
";"                                             { printf("; -> Semicolon\n"); }
","                                             { printf(", -> Comma\n"); }
"("                                             { printf("( -> Left Parenthesis\n"); }
")"                                             { printf(") -> Right Parenthesis\n"); }
"{"                                             { printf("{ -> Left Brace\n"); }
"}"                                             { printf("} -> Right Brace\n"); }
\"(\\.|[^\\"])*\"                               { printf("%s -> String Literal\n", yytext); }
[ \t\n]+                                        { /* ignore whitespace */ }
.                                               { printf("%s -> Unknown Token\n", yytext); }
%%

int main() {
    printf("Enter the C code input:\n");
    yylex();
    return 0;
}
