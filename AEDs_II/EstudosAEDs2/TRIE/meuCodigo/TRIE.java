import java.util.*;
public static int hash(char letra){
    return (int)letra; 
}
class No{
    public char elemento;
    public No[] prox = new No[255] ; 
    /* 🌐 ALOCAÇÃO DE MEMÓRIA E INDEXAÇÃO: 
       Cada nó possui um ponteiro 'prox' que aponta para o início de um espaço de memória 
       reservado para 255 referências. Quando a função hash transforma uma letra em um 
       número inteiro, nós usamos esse número como índice para acessar diretamente a posição 
       desejada dentro desse espaço. Se a próxima letra já foi inserida, haverá um nó ali; 
       caso contrário, a posição estará nula (vazia), indicando que o caminho não existe. */
    public boolean folha ;

    public No(char letra) {
        this.elemento = letra; 
        this.folha = false; 
        this.prox = new No[255]; // 🚪 Cria as 255 portas na memória!
        // Opcional: Garante que todas as posições comecem vazias (null)
        for (int i = 0; i < 255; i++) {
            this.prox[i] = null;
        }
    }
    public No() {
        this.elemento = ' ';
        this.folha = false; 
        this.prox = new No[255]; 
        for (int i = 0; i < 255; i++) {
            this.prox[i] = null;
        }
    }
}

public class TRIE{
    public No raiz;
    public TRIE (){
        this.raiz = new No();
    }

    
}
