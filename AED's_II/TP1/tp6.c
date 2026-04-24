#include <stdio.h>


        // função pra verificar se a string digitada é "FIM"
int verificar_fim(char* string){
        // confere letra por letra se é F I M e depois o fim da string
        if(string[0]=='F' && string[1]=='I' && string[2]=='M' && string[3]=='\0'){// cada posição da string deve ser restritarmente essa "F"I"M"\0"
                return 1; // se for FIM retorna 1
        }else{
                return 0; // se não for retorna 0
        }
}

int tamanho_string(char* string){// // função pra calcular o tamanho da string (tipo strlen)
        int tam=0;
        while(string[tam] != '\0'){ // conta até achar o \0 ou seja fim da string
        tam++;
        }
        return tam; // delvolve o tamanho
}


void para_minusculo(char* string){// coloca todas as letras para minusculos para serem tratadas da mesma forma 
    int i = 0;
    while(string[i] != '\0'){
        if(string[i] >= 'A' && string[i] <= 'Z'){// se forem maiusculas vão para minusculas 
            string[i] += 32;// +32 para ser equivalente ao ser formato minusculo 
        }
        i++;// próxima casa 
    }
}


void verificar_anagrama(char string1[],int tam1, char string2[], int tam2){ // manda a string na primeira casa e devolve a soma dos valores de cada casa 
 	      
    para_minusculo(string1);// tratar problema de diferença de maiusculo pra minusculoo
    para_minusculo(string2);

    if(tam1 != tam2){
        printf("NÃO\n"); // se for de tamanho diferente já mata 
    }else{

        int usado[tam2];// confirmação de casas sem repetir ex: aabe e abex

        for(int i = 0; i < tam2; i++){
            usado[i] = 0;// inicia com 0 tudo 
        }

        for(int i = 0; i < tam1; i++){

            for(int j = 0; j < tam2; j++){

                if(string1[i] == string2[j] && usado[j] == 0){
                    usado[j] = 1;// muda as casas que foram encontradas
                    j = tam2; // meu brack cazeiro 
                }
            }
        }

        int soma=0;
        for(int x=0; x<tam2; x++){
            if(usado[x]==1){// verificar quantas casas foram encontradas
                soma++;
            }
        }

        if(soma==tam2){// se o numero de casas encontardas sem repetir foi igual ao de existente então volta SIM.
            printf("SIM\n");
        }else{
            printf("NÃO\n");
        }
    }
}




int main(){
    	char string1[1001];// vetor com entrada de tamanho 1000 so pra não dar erro 
	char string2[1001];

	scanf("%s", string1);
        while(verificar_fim(string1) != 1 ){ 
                char ifen;
		
		scanf(" %c", &ifen);// o - obrigatorio 
		scanf("%s", string2);
		verificar_anagrama(string1, tamanho_string(string1), string2, tamanho_string(string2) );/* manda as stringas e retorna como tamanho o valor calculado pela função tamanho_string para a função verificar_anagrama*/ 
                scanf("%s", string1); 
        }

}
