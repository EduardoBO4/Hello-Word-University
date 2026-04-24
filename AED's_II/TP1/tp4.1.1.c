#include<stdio.h>
#include<stdlib.h>
#include<locale.h>

// nosso strlen caseiro pra saber o tamanho da palavra
int conta_letras(char texto[]) 
{
        int qnt = 0;
        while (texto[qnt] != '\0') // vai rodando até bater de frente com o \0 (fim da string)
        {
                qnt++; // vai somando
        }
        return qnt; // devolve o tamanho que achou
}


// funçãozinha pra ver se duas strings são iguais (tipostrcmp só que do nosso jeito)
int checa_se_igual(char palavra_A[], char palavra_B[])
{
    int tamA = conta_letras(palavra_A);
    int tamB = conta_letras(palavra_B);
    int eh_igual = 1; // começa na fé achando que as palavras são iguais (1)

    if (tamA != tamB) // se o tamanho não bater, já era
    {
        return 0; // devolve 0 avisando que é diferente
    }
    else
    {
        for (int i = 0; i < tamA; i++) // olha casinha por casinha
        {
            if (palavra_A[i] != palavra_B[i])
            {
                eh_igual = 0; // se uma letrinha for diferente, avisa que deu ruim
            }
        }
    }

    if (eh_igual == 0)
    {
        return 0; // manda 0 (falso, não são iguais)
    }
    else
    {
        return 1; // manda 1 (verdadeiro, são idênticas)
    }
}

// função que inverte a string recebida
void coloca_de_tras_pra_frente(char frase[]) 
{
    int tamanho_total = conta_letras(frase);
    
    for(int i = tamanho_total - 1; i >= 0; i--){ // anda no vetor de trás para frente
        
        // gambiarra pra tratar problema de acento e caracteres chatos que bugam
        if(frase[i] == '~' || frase[i] == 'ç' || frase[i] == '`' || frase[i] == '^'){
            printf("%c %c", frase[i + 1], frase[i]); // imprime invertido pra arrumar
        }
        
        printf("%c", frase[i]); // vai printando as letrinhas
    }

    printf("\n"); // pula linha pra não ficar tudo colado
    
}


int main(){
    setlocale(LC_ALL,""); // pra tentar arrumar os acentos no terminal
    
    // aloca um espaço gigante na memoria de 2000 so pra não dar erro
    char *linha_digitada = (char*)malloc(2000 * sizeof(char)); 

    scanf("%[^\n]", linha_digitada); // pega a linha toda até o enter
    getchar(); // limpa o buffer do teclado pra não bugar a proxima leitura

    // vai rodando enquanto a nossa função ver que a palavra NÃO é "FIM"
    while(checa_se_igual(linha_digitada, "FIM") == 0){ 
        coloca_de_tras_pra_frente(linha_digitada); // chama a função pra inverter a parada toda
        
        scanf("%[^\n]", linha_digitada); // pega a proxima linha ou o FIM
        getchar(); // limpa o buffer de novo
    }
}
