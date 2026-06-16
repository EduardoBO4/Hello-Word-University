#include <stdio.h>

// função pra ver se digitou FIM e parar o programa
int verificar_fim(char* s) {
    if (s[0] == 'F' && s[1] == 'I' && s[2] == 'M' && s[3] == '\0') {
        return 1;
    }
    return 0;
}

// conta o tamanho da string pra eu usar nos for
int tamanho_string(char* s) {
    int t = 0;
    while (s[t] != '\0') {
        t++;
    }
    return t;
}

int string_repet(char s[]) {
    int resultado = 0;
    int tam = tamanho_string(s); // chamei o tamanho aqui dentro agora

    // esse for escolhe onde a busca vai começar
    for (int i = 0; i < tam; i++) {
        int cont = 0;
        int parou = 0; // flag pra avisar que achou letra repetida

        // esse for vai andando pra frente a partir do i
        for (int j = i; j < tam && parou == 0; j++) {
            int ja_tem = 0;

            // aqui eu checo se a letra de agora ja apareceu antes nesse pedaço
            for (int k = i; k < j; k++) {
                if (s[j] == s[k]) {
                    ja_tem = 1; // se achou igual, marca que ja tem
                }
            }

            if (ja_tem == 0) {
                cont++;
                // se o que eu achei agora for maior que o antigo, eu atualizo
                if (cont > resultado) {
                    resultado = cont;
                }
            } else {
                parou = 1; // se ja tinha a letra, para de contar essa parte
            }
        }
    }
    return resultado;
}

int main() {
    char str[100];
    
    // lendo a primeira string antes de entrar no loop
    scanf("%s", str);

    // fica rodando enquanto a função de fim não retornar 1
    while (verificar_fim(str) == 0) {
        // chama a função direto dentro do printf como voce pediu
        printf("%d\n", string_repet(str));
        
        // le a proxima string no final do loop
        scanf("%s", str);
    }
    
    return 0;
}
