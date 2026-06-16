#include <stdio.h>

// verifica se a string é "FIM"
int verificar_fim(char* string){
    if(string[0]=='F' && string[1]=='I' && string[2]=='M' && string[3]=='\0'){
        return 1;
    }
    return 0;
}

// remove o '\n' sem usar break
void remover_enter(char *str){// // fgets coloca \n, preciso tirar isso por causa do FIM que deve ser FIM\0 e não FIM\n\0
    int i = 0;
    while(str[i] != '\0' && str[i] != '\n'){//anda até achar o \n ou \0
        i++;
    }
    if(str[i] == '\n'){// achou \n viro \0
        str[i] = '\0';
    }
}

void cesar_rec(char *mensagem, int i){
    if(mensagem[i] == '\0'){// caso de parada é quando chegar no final da string
        printf("%s\n", mensagem);
        return;
    }

    mensagem[i] += 3;// 3 valores acima na tabela ascii

    cesar_rec(mensagem, i + 1);// chama até o \0 com i +1 para ir ora próxima casa 
}

int main(){
    char string[100000];// tem entradas enormes preciso de garantia de espaço

    fgets(string, 100000, stdin);// para entrada com espaços ' ' 
    remover_enter(string);

    while(verificar_fim(string) != 1){
        cesar_rec(string, 0);// chama e manda a string e o valor na casa 0

        fgets(string, 100000, stdin);
        remover_enter(string);// sempre remover \n
    }

    return 0;
}
