import java.ultil.*; 
public class Arvores {
    class No{
        int valor;
        No esquerda;
        No direita;
        boolean cor; // true para preto false para branco
        public No(int valor){
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
            this.cor = false; // Inicialmente, o nó é branco
        }
        public boolean isNotipo4 (){
            if(this.direita!= null && this.esquerda!=null){
            
                if(this.direita.cor== true && this.esquerda.cor == true){
                    system.out.println("O nó " + this.valor + " é do tipo 4");
                    return true;
                }else{
                    system.out.println("O nó " + this.valor + " não é do tipo 4");
                    return false;
                }
            
            }else{
                return false; 
            }
        }
    }

    class ArvoreBicolor{
        No raiz;
        public ArvoreBicolor(){
            this.raiz = null;
        }

        private void fragmentar(No no){
            if(no.isNotipo4()){
                no.esquerda.cor = false;
                no.direita.cor = false;
                no.cor = true; 
            }else{
                System.out.println("O nó " + no.valor + " não é do tipo 4, não pode ser fragmentado.");
            }
        }
        
    }
    class Arvore{
        No raiz;
        public Arvore(){
            this.raiz = null;
        }
        public void inserir(int valor){
            this.raiz = inserirRecursivo(this.raiz, valor);
        }
        private No inserirRecursivo(No raiz, int valor){
            if(raiz == null){
                raiz = new No(valor);
                return raiz;
            }
            if(valor < raiz.valor){
                raiz.esquerda = inserirRecursivo(raiz.esquerda, valor);
            } else if(valor > raiz.valor){
                raiz.direita = inserirRecursivo(raiz.direita, valor);
            }
            return raiz;
        }
    }

    public static void main(String[] args) {

    }
}