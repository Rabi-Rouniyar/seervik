#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>

#define MAX 100

typedef struct {
    char result[10];
    char arg1[10];
    char op;
    char arg2[10];
    int isUsed;
} TAC;

TAC code[MAX];
int n = 4; // Hardcoded number of lines

int isConstant(char *s) {
    for (int i = 0; i < strlen(s); i++)
        if (!isdigit(s[i])) return 0;
    return 1;
}

void loadHardcodedTAC() {
    // Hardcoded input TAC lines
    strcpy(code[0].result, "t1"); strcpy(code[0].arg1, "4"); code[0].op = '+'; strcpy(code[0].arg2, "5");

    strcpy(code[1].result, "t2"); strcpy(code[1].arg1, "a"); code[1].op = '+'; strcpy(code[1].arg2, "b");

    strcpy(code[2].result, "t3"); strcpy(code[2].arg1, "a"); code[2].op = '+'; strcpy(code[2].arg2, "b");

    strcpy(code[3].result, "t4"); strcpy(code[3].arg1, "t2"); code[3].op = '+'; strcpy(code[3].arg2, "t3");

    for (int i = 0; i < n; i++) code[i].isUsed = 0;
}

void printInputTAC() {
    printf("Input:\nEnter number of TAC lines: %d\nEnter TAC lines:\n", n);
    for (int i = 0; i < n; i++) {
        if (code[i].op == '=')
            printf("%s = %s\n", code[i].result, code[i].arg1);
        else
            printf("%s = %s %c %s\n", code[i].result, code[i].arg1, code[i].op, code[i].arg2);
    }
}

void constantFolding() {
    for (int i = 0; i < n; i++) {
        if (isConstant(code[i].arg1) && isConstant(code[i].arg2)) {
            int a = atoi(code[i].arg1);
            int b = atoi(code[i].arg2);
            int res;
            switch (code[i].op) {
                case '+': res = a + b; break;
                case '-': res = a - b; break;
                case '*': res = a * b; break;
                case '/': res = b != 0 ? a / b : 0; break;
                default: continue;
            }
            sprintf(code[i].arg1, "%d", res);
            strcpy(code[i].arg2, "");
            code[i].op = '=';
        }
    }
}

void algebraicSimplification() {
    for (int i = 0; i < n; i++) {
        if ((strcmp(code[i].arg1, "1") == 0 || strcmp(code[i].arg2, "1") == 0) && code[i].op == '*') {
            if (strcmp(code[i].arg1, "1") == 0)
                strcpy(code[i].arg1, code[i].arg2);
            code[i].op = '=';
            strcpy(code[i].arg2, "");
        }

        if ((strcmp(code[i].arg1, "0") == 0 || strcmp(code[i].arg2, "0") == 0) && code[i].op == '+') {
            if (strcmp(code[i].arg1, "0") == 0)
                strcpy(code[i].arg1, code[i].arg2);
            code[i].op = '=';
            strcpy(code[i].arg2, "");
        }
    }
}

void commonSubexpressionElimination() {
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (code[i].op == code[j].op &&
                strcmp(code[i].arg1, code[j].arg1) == 0 &&
                strcmp(code[i].arg2, code[j].arg2) == 0) {
                strcpy(code[j].arg1, code[i].result);
                code[j].op = '=';
                strcpy(code[j].arg2, "");
            }
        }
    }
}

void markUsedVars() {
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            if (strcmp(code[i].result, code[j].arg1) == 0 ||
                strcmp(code[i].result, code[j].arg2) == 0) {
                code[i].isUsed = 1;
                break;
            }
        }
    }
}

void printOptimizedCode() {
    printf("\nOutput:\nOptimized Three Address Code:\n");
    for (int i = 0; i < n; i++) {
        if (code[i].isUsed || i == n - 1) {
            if (code[i].op == '=')
                printf("%s = %s\n", code[i].result, code[i].arg1);
            else
                printf("%s = %s %c %s\n", code[i].result, code[i].arg1, code[i].op, code[i].arg2);
        }
    }
}

int main() {
    loadHardcodedTAC();
    printInputTAC();

    constantFolding();
    algebraicSimplification();
    commonSubexpressionElimination();
    markUsedVars();
    printOptimizedCode();

    return 0;
}
