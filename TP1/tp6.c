#include <stdio.h>
#include <stdlib.h>

// Retorna a quantidade de caracteres da string (excluindo o '\0').
int contaTamanho(char string[]){
    int tam = 0;
    while((string[tam]) != '\0'){
        tam++;
    }
    return tam;
}

// Funçãozinha pra deixar tudo minusculo e facilitar nossa vida (trata A e a como iguais)
void para_minusculo(char string[]){
    int i = 0;
    while(string[i] != '\0'){
        if(string[i] >= 'A' && string[i] <= 'Z'){
            string[i] += 32; // +32 na tabela ASCII desce a letra pro formato minusculo
        }
        i++;
    }
}

// Retorna 0 se a string for "FIM", sinalizando a parada do loop.
int verificaFim(char string[]){
    if(string[0] == 'F' && string[1] == 'I' && string[2] == 'M' && string[3] == '\0'){
        return 0; // achou o FIM, manda 0 pra parar tudo
    }else{
        return 1; // não é FIM, a vida segue normal
    }
}

// Função que realmente faz o trampo de ver se é anagrama
void testa_anagrama(char palavra1[], char palavra2[]){
    int tam1 = contaTamanho(palavra1);
    int tam2 = contaTamanho(palavra2);

    // Já converte as duas pra minusculo pra não dar B.O com diferença de maiuscula
    para_minusculo(palavra1);
    para_minusculo(palavra2);

    if(tam1 != tam2){
        // Se o tamanho já é diferente, morre aqui, impossivel ser anagrama
        printf("NAO\n"); // SEM ACENTO pro juiz automático não chiar
    } else {
        int usado[tam2]; // vetorzinho pra marcar qual letra da palavra2 a gente já "queimou"

        // inicia nosso vetor de marcação tudo com 0
        for(int i = 0; i < tam2; i++){
            usado[i] = 0; 
        }

        // vai olhando letra por letra da primeira palavra
        for(int i = 0; i < tam1; i++){
            // e tenta achar uma gemea pra ela na segunda palavra
            for(int j = 0; j < tam2; j++){
                // se achou a letra igual e ela ainda não foi usada (0)
                if(palavra1[i] == palavra2[j] && usado[j] == 0){
                    usado[j] = 1; // marca como usada (1) pra não repetir
                    break; // achei a parça dela, já posso parar de procurar e ir pra proxima letra (nosso break maroto)
                }
            }
        }

        // agora a gente conta quantas letras bateram certinho
        int total_achado = 0;
        for(int x = 0; x < tam2; x++){
            if(usado[x] == 1){
                total_achado++;
            }
        }

        // se o total de letras que bateram for igual ao tamanho da palavra, deu bom!
        if(total_achado == tam2){
            printf("SIM\n");
        }else{
            printf("NAO\n");
        }
    }
}

int main(){
    char palavra1[2000];
    char palavra2[2000];

    // Lê só a primeira palavra (ou o FIM)
    scanf("%s", palavra1);

    while(verificaFim(palavra1) != 0){
        // Se não for FIM, lê a segunda palavra que tá na mesma linha (o %s ignora o espaço)
        scanf("%s", palavra2);
        
        // Manda as duas lá pra nossa função resolver
        testa_anagrama(palavra1, palavra2);
        
        // Lê a proxima palavra da proxima linha pra ver se é FIM e recomeçar o loop
        scanf("%s", palavra1);
    }
    
    return 0;
}
