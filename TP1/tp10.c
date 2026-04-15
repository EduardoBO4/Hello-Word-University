#include <stdio.h>

// verifica se a string é "FIM"
int verificar_fim(char* string){
    if(string[0]=='F' && string[1]=='I' && string[2]=='M' && string[3]=='\0'){
        return 1;
    }else{
        return 0;
    }
}

// verifica se é vogal (sem acentos pra evitar erro no C)
int eh_vogal(char c){
    return (c=='a'||c=='e'||c=='i'||c=='o'||c=='u'||
            c=='A'||c=='E'||c=='I'||c=='O'||c=='U');
}

// verifica se a string tem apenas vogais
int so_vogais(char* s, int i){

    if(s[i] == '\0'){
        return 1;
    }

    // ignora espaços e pontuação
    if(s[i] == ' ' || s[i] == ',' || s[i] == '.' ||
       s[i] == '!' || s[i] == '?' || s[i] == '-' ){
        return so_vogais(s, i+1);
    }

    if(!eh_vogal(s[i])){
        return 0;
    }

    return so_vogais(s, i+1);
}

// verifica se a string tem apenas consoantes
int so_consoantes(char *s, int i){

    if(s[i] == '\0'){
        return 1;
    }

    // ignora espaços e pontuação
    if(s[i] == ' ' || s[i] == ',' || s[i] == '.' ||
       s[i] == '!' || s[i] == '?' || s[i] == '-' ){
        return so_consoantes(s, i+1);
    }

    // não pode ser vogal
    if(eh_vogal(s[i])){
        return 0;
    }

    // precisa ser letra
    if(!((s[i] >= 'A' && s[i] <= 'Z') ||
         (s[i] >= 'a' && s[i] <= 'z'))){
        return 0;
    }

    return so_consoantes(s, i+1);
}

// verifica se é número inteiro
int so_inteiros(char *s, int i){

    if(s[i] == '\0'){
        return 1;
    }

    if(s[i] == ' '){
        return so_inteiros(s, i+1);
    }

    if(!(s[i] >= '0' && s[i] <= '9')){
        return 0;
    }

    return so_inteiros(s, i+1);
}

// verifica se é número real
int so_reais(char *s, int i, int ponto){

    if(s[i] == '\0'){
        return 1;
    }

    if(s[i] == ' '){
        return so_reais(s, i+1, ponto);
    }

    if(s[i] == '.' || s[i] == ','){
        if(ponto == 1){
            return 0;
        }
        return so_reais(s, i+1, 1);
    }

    if(!(s[i] >= '0' && s[i] <= '9')){
        return 0;
    }

    return so_reais(s, i+1, ponto);
}

int main(){
    char string[10000];

    scanf(" %[^\n]", string);

    while(verificar_fim(string) != 1 ){

        if (so_vogais(string, 0)){
            printf("SIM ");
        } else {
            printf("NAO ");
        }

        if (so_consoantes(string, 0)){
            printf("SIM ");
        } else {
            printf("NAO ");
        }

        if (so_inteiros(string, 0)){
            printf("SIM ");
        } else {
            printf("NAO ");
        }

        if (so_reais(string, 0, 0)){
            printf("SIM\n");
        } else {
            printf("NAO\n");
        }

        scanf(" %[^\n]", string);
    }

    return 0;
}
