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

        /*public static void inserir(No raiz, int valor) {
            // 1. Caso Base: Se o nó é nulo, criamos um novo nó com o valor
            if (raiz == null) {
                raiz = new No(valor);
                return;
            }else{// != null
                No atual = raiz;
                if(valor < atual.elemento){
                    if(atual.esq == null){
                        atual.esq = new No(valor);
                    }else{
                        inserir(atual.esq, valor);
                    }
                }else{
                    if(atual.dir == null){
                        atual.dir = new No(valor);
                    }else{
                        inserir(atual.dir, valor);
                    }
                }
            }

        }*/ //O MEUUUU
        

            // O MELHORRRR
        public No inserir(No Raiz, int valor) {
            // 1. Caso Base: Se cheguei no null, encontrei o lugar! 
            // Crio o nó e o retorno para quem me chamou.
            No i = Raiz;
            if (i == null) {
                return new No(valor);
            }

            // 2. Navegação: "Pai, o seu novo filho será o resultado dessa busca"
            if (valor < i.elemento) {
                i.esq = inserir(i.esq, valor);


            } else if (valor > i.elemento) {
                i.dir = inserir(i.dir, valor);
            }

            // 3. Retorna o próprio nó (sem alterações se não for o lugar da inserção)
            return i;
        }
        
        private No remover(int x, No i) throws Exception {

        if (i == null) {
            throw new Exception("Erro ao remover!");
        } 
        else if (x < i.elemento) {
            i.esq = remover(x, i.esq);

        } 
        else if (x > i.elemento) {
            i.dir = remover(x, i.dir);

        // Sem no a direita.
        } 
        else if (i.dir == null) {
            i = i.esq;

        // Sem no a esquerda.
        } 
        else if (i.esq == null) {
            i = i.dir;

        // No a esquerda e no a direita.
        } 
        else {
            i.esq = maiorEsq(i, i.esq);
            }

		return i;
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


        public static int acharMenor(No raiz) {
            if (raiz == null) {
                System.out.println("A árvore está vazia");//throw new IllegalArgumentException("A árvore está vazia");
                return 0;
            }else{
                No atual = raiz;
                while (atual.esq != null) {
                    atual = atual.esq;
                }
                return atual.elemento;
            } 
        }

        public static int acharMaior(No raiz) {
            if (raiz == null) {
                System.out.println("A árvore está vazia");//throw new IllegalArgumentException("A árvore está vazia");
                return 0;
            }
            No atual = raiz;
            while (atual.dir != null) {
                atual = atual.dir;
            }
            return atual.elemento;
        }

        public static int acharMenor(No i) {
            // 1. Caso Base: Se o nó é nulo, retornamos o maior valor possível 
            // para que ele não ganhe na comparação de "menor valor"
            if (i == null) {
                return Integer.MAX_VALUE; 
            }

            // 2. Pegamos o valor do nó atual
            int menor = i.elemento;

            // 3. Chamamos a recursão para os dois lados
            int menorEsq = acharMenor(i.esq);
            int menorDir = acharMenor(i.dir);

            // 4. Comparamos os 3 candidatos e ficamos com o campeão
            if (menorEsq < menor) {
                menor = menorEsq;
            }
            if (menorDir < menor) {
                menor = menorDir;
            }
            return menor;
        }
        // CENTRAL
        public void caminharCentral(No i) {
            if (i != null) {
                caminharCentral(i.esq); // ESQUERDA
                System.out.print(i.elemento + " "); // RAIZ (no meio)
                caminharCentral(i.dir); // DIREITA
            }
        }

        // PRÉ-ORDEM
        public void caminharPre(No i) {
            if (i != null) {
                System.out.print(i.elemento + " "); // RAIZ (primeiro)
                caminharPre(i.esq);// ESQUERDA
                caminharPre(i.dir);// DIREITA
            }
        }
        // PÓS-ORDEM
        public void caminharPos(No i) {
            if (i != null) {
                caminharPos(i.esq);// ESQUERDA
                caminharPos(i.dir);// DIREITA
                System.out.print(i.elemento + " "); // RAIZ (último)
            }
        }
    }
    public static void main(String[] args){

    }
}