%{
#include <stdio.h>
%}

%%
/* Verbs */
[Dd]read|[Rr]un|[Aa]rrives|[Aa]re|[Aa]m|[Ii]s|[Ss]top|[Hh]ave|[Gg]ive|[Kk]ill|[Ii]magines|[Ww]ent|[Cc]omes|[Ee]xisting       { printf("%s -> Verb\n", yytext); }

/* Pronouns */
[Ii]t|[Yy]ou|[Ii]|[Yy]ou|[Ww]e|[Tt]hey|[Hh]e|[Ss]he|[Yy]our|[Mm]e|[Mm]y|[Tt]hat|[Ww]ho|[Ww]hich                            { printf("%s -> Pronoun\n", yytext); }

/* Nouns */
[Dd]estiny|[Tt]hing|[Cc]uriosity|[Rr]eason|[Pp]ower|[Gg]ap|[Gg]eneration|[Oo]ne|[Hh]uman|[Bb]eings|[Tt]hinking               { printf("%s -> Noun\n", yytext); }

/* Adjectives */
[Ii]mportant|[Ff]ine|[Ss]trong|[Bb]inary|[Mm]ore|[Ww]iser|[Dd]ramatic|[Dd]istinct                                          { printf("%s -> Adjective\n", yytext); }

/* Adverbs */
[Nn]ot|[Aa]ll|[Ss]ame|[Bb]efore|[Aa]fter                                                                                  { printf("%s -> Adverb\n", yytext); }

/* Prepositions */
[Ff]rom|[Tt]o|[Ff]or|[Ii]n|[Oo]n|[Ww]ith|[Bb]etween|[Oo]f                                                                  { printf("%s -> Preposition\n", yytext); }

/* Determiners */
[Tt]he|[Aa]n|[Aa]                                                                                                         { printf("%s -> Determiner\n", yytext); }

/* Conjunctions */
[Aa]nd|[Oo]r|[Bb]ut|[Ii]f                                                                                                 { printf("%s -> Conjunction\n", yytext); }

/* Interjections */
[Hh]ello|[Tt]hank|[Yy]ou                                                                                                  { printf("%s -> Interjection\n", yytext); }

/* Whitespace and punctuation */
[ \t\n]+       { /* ignore whitespace */ }
[.,?!’']+      { /* ignore punctuation */ }

/* Default */
.              { printf("%s -> Unknown\n", yytext); }

%%

int main() {
    printf("Enter the input text:\n");
    yylex();
    return 0;
}
