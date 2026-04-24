#include <stdio.h>

int verificar_fim(char* string){// função pra verificar se a string digitada é "FIM"
	if(string[0]=='F' && string[1]=='I' && string[2]=='M' && string[3]=='\0'){
		return 1; // se for FIM confirma e para o While  
	}else{
		return 0; // se não for segue a vida 
	}
}

void Inverte(char* vetor, int tam){// função que inverte a string recebida

    char result[tam + 1];

    for(int i = 0; i < tam; i++){
        result[i] = vetor[tam - i - 1]; // anda e colocar o vetor de trás para frente 
    }

    result[tam] = '\0';// define "manualmente " o fim da string para não dar erros 

    printf("%s\n", result);
}

int tamanho_string(char* string){// // função pra calcular o tamanho da string (tipo strlen)
	int tam=0;
	while(string[tam] != '\0'){ // conta até achar o \0 ou seja fim da string 
        tam++;
	}
	return tam; // delvolve o tamanho 
}

int main(){
	
    char vetor[2001];// vetor com entrada de tamanho 2000 so pra não dar erro 
    int tam = 0;
    scanf("%s", vetor);

	while(verificar_fim(vetor) != 1 ){ // vai rodando até o retorno da função achar FIM ser 1 
		Inverte(vetor, tamanho_string(vetor));

    		scanf("%s", vetor);
	}
	
}
