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


int somar_digitos(char string[0]){ // manda a string na primeira casa e devolve a soma dos valores de cada casa 
	if(string[0]=='\0'){//se a primeira casa for \0 na para e devolve 0
		return 0; 
	}else{
		return (string[0] - '0') + somar_digitos(string + 1);// chama calculo '3' - '0' → 51 - 48 = 3  + chamada da proxima casa até a parada  .
	}
}


int main(){
    char vetor[2001];// vetor com entrada de tamanho 2000 so pra não dar erro 
    scanf("%s", vetor);

        while(verificar_fim(vetor) != 1 ){ // vai rodando até o retorno da função achar FIM ser 1 
                printf("%d\n", somar_digitos(vetor));// mostra o valor da soma 
                scanf("%s", vetor); //próximo valor ou para FIM
        }

}
