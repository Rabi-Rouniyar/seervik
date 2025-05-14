sudo apt update
sudo apt install bison flex gcc
flex filename.l
bison -d filename.y
gcc lex.yy.c filename.tab.c -o output
./output
gcc lex.yy.c filename.tab.c -o output -lm
gcc lex.yy.c filename.tab.c -o output -lfl
nano filename.l or nano filename.y
ctrl + x to close
to save close the file and while closing it asks to save the file
for undo alt + u
to delete line ctrl + k
