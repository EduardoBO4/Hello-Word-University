import java.util.*; 
class ArvoreBi{
    //Declaração base da estrutura de um nó da árvore binária
    class No{
        private int elemento;
        private No esq;
        private No dir;

        public No(int valor){
            this.elemento = valor;
            this.esq = null;
            this.dir = null;
        }
    }

    // Achar elemento na árvore:
    public static boolean existeVal(No raiz, int valor) {
        // 1. Caso Base: Se o nó é nulo, o valor não está aqui
        if (raiz == null) {
            return false;
        }

        // 2. Verificamos se o nó ATUAL tem o que procuramos
        // Isso deve ser feito ANTES de olhar os filhos ou desistir
        if (raiz.elemento == valor) {
            return true;
        }

    // 3. Se não achou aqui, procura nos dois lados
    // O operador || (OU) é mágico: se achar na esquerda, ele nem tenta a direita
    return existeVal(raiz.esq, valor) || existeVal(raiz.dir, valor);
    }

    public static void main(String[] args){

    }
}