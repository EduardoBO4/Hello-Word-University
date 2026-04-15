#include <stdio.h>
#include <stdlib.h>

int somar_digitos(int n){
    if(n == 0){
        return 0; // caso base da recursão
    }
    // Modulo pega o ultimo digito e a divisão remove ele pra proxima chamada (pois divide por 10)
    return (n % 10) + somar_digitos(n / 10); 
}

int main(){
    char vetor[2001]; // vetor com entrada de tamanho 2000 so pra não dar erro 

    while(scanf("%s", vetor) != EOF){ // vai rodando até acabar o arquivo
        
        int num = atoi(vetor); // Aprendi a função atoi na net pra transformar em numero, então decidi usar
        
        printf("%d\n", somar_digitos(num)); // mostra o valor da soma 
    }
}
