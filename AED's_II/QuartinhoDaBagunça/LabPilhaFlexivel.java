import java.util.*;
public class LabPilhaFlexivel {
    public class Celula{
        private int elemento;
        private Celula prox;

        public Celula(int valor){
            this.elemento = valor;
            this.prox = null;
        }
        public Pilha(){
            topo=NULL; 
        }
        public static void nostarInverso(Celula topo){
            if(topo != null){
                nostarInverso(topo.prox);
                System.out.println(topo.elemento);
            }else{
                return;
            }
        }
        public static void maiorElemento(Celula topo){
            if(topo == null){
                System.out.println("Pilha vazia");
            }else{
                int maior = topo.elemento;
                for(Celula i= topo; i != null; i= i.prox){
                    if(i.elemento > maior){
                        maior = i.elemento;
                    }
                }
                System.out.println("Eis o maior elemento da pilha: " + maior);
            }
        }
        public static Celula invertParaB(Celula topoA){
            Celula topoB = null;
            Celula tmp= null;
            if(topoA == null){
                System.out.println("Pilha A vazia");
            }else{
                topoB = new Celula(topoA.elemento);
                int quantidade = 1;
                for(Celula i= topoA.prox; i != null; i= i.prox){
                    tmp = new Celula(i.elemento);
                    tmp.prox = topoB;
                    topoB = tmp;
                    tmp = null;
                    quantidade++;
                }
                System.out.println("Foi passado os " + quantidade + " elementos da pilha A para a pilha B");
            }
            return topoB;
        }

        public static Celula invertPilha(Celula topo){
                if(topo == null){
                    System.out.println("Pilha vazia");
                }else if(topo.prox == null){
                    System.out.println("Pilha com apenas um elemento");
                }else{
                    celula tmp = null;
                    Celula anterior = null;
                    Celula atual = topo;
                    while(atual != null){
                        tmp = atual.prox;
                        atual.prox = anterior;
                        anterior = atual;
                        atual = tmp;
                    }
                    topo = anterior;
                }
            }
            return topo;
        }


        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            System.out.print("Enter a string: ");
            String str = sc.nextLine();
            String reversed = new StringBuilder(str).reverse().toString();
            System.out.println("Reversed string: " + reversed);
        }
    }
}