import java.util.*;
public class LabFilaFlexicel{
    class Celula{
        private int elemento;
        private Celula prox;

        public Celula(int valor){
            this.elemento = valor;
            this.prox = null;
        }

    }
    class Fila{
        private Celula inicio;
        private Celula fim;

        public Fila(){
            this.inicio = null;
            this.fim = null;
        }
        public Celula criarListaFlex( int numDeElementos){
            Fila mFila = new Fila();
            int entradaVal=0;
            for (int i = 1; i <= numDeElementos; i++) {
                System.out.println("Digite o " + i + "º elemento da fila: "); 
                entradaVal = new Scanner(System.in).nextInt();
                mFila.inicio = new Celula(entradaVal);
            }
            return mFila.inicio;
        }

        public static void passInvert(Celula i, Fila f) {
            if (i != null) {
                // 1. Primeiro, vamos até o fim da pilha (recursão)
                passInvert(i.prox, f);
        
                // 2. Na volta, inserimos o elemento na fila
                f.inserir(i.elemento);
             }
        }

        public static void contarExist(Celula ref) {
            if (ref == null){
                System.out.println("Fila vazia");
            }else{
                int cont =0;
                for(Celula i = ref; i != null; i = i.prox){
                    cont++;
                }
                System.out.println("Número de elementos na fila: " + cont);
            }
        }
        public static void main(String[] args) {

    }
}