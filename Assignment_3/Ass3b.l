%{
    /*
    * Lexical Analyzer for C programming language without a symbol table.
    */
    #include <stdio.h>
%}
%%
[\t ]+                /* Ignore whitespace */ ;

"int"|"float"         { printf("%s: is a keyword\n", yytext); }
"printf"              { printf("%s: is a keyword\n", yytext); }

[0-9]+                { printf("%s: is a number\n", yytext); }
[a-zA-Z_][a-zA-Z0-9_]* { printf("%s: is an identifier\n", yytext); }

"="                   { printf("%s: is an assignment operator\n", yytext); }
"=="                  { printf("%s: is a comparison operator\n", yytext); }
"("                   { printf("%s: is an opening parenthesis\n", yytext); }
")"                   { printf("%s: is a closing parenthesis\n", yytext); }
"{"                   { printf("%s: is an opening brace\n", yytext); }
"}"                   { printf("%s: is a closing brace\n", yytext); }
";"                   { printf("%s: is a semicolon\n", yytext); }
","                   { printf("%s: is a comma\n", yytext); }
"\""                  { printf("%s: is a double quote\n", yytext); }
"%"                   { printf("%s: is a modulus operator\n", yytext); }
"+"                   { printf("%s: is an addition operator\n", yytext); }
"-"                   { printf("%s: is a subtraction operator\n", yytext); }
"*"                   { printf("%s: is a multiplication operator\n", yytext); }
"/"                   { printf("%s: is a division operator\n", yytext); }

.                     { printf("%s: is an unknown character\n", yytext); }

\n                    { /* Handle newlines */ }

%%

int main()
{
    yylex();
    return 0;
}

int yywrap()
{
    return 1;
}