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

        

            // O MELHORRRR
        public No inserir(No Raiz, int valor) {
            No i = Raiz; // gosto de fazer isso 
            if (i == null) {
                return new No(valor);
            }

            if (valor < i.elemento) {
                i.esq = inserir(i.esq, valor);


            } else if (valor > i.elemento) {
                i.dir = inserir(i.dir, valor);
            }

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

         if (raiz == null) {
            return false;
        }

        if (raiz.elemento == valor) {
            return true;
        }

        return existeVal(raiz.esq, valor) || existeVal(raiz.dir, valor);
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

    
// Exercício 3 do slide C de árvores binárias
    private int somar(No i) {
        int soma;
        if (i == null) {
            return 0; 
        }
        return  soma =i.elemento + somar(i.esq) + somar(i.dir);
    }
    private int quantElem(No i) {
        int soma; 
        if (i == null) {
            return 0; 
        }
        return soma = 1 + quantElem(i.esq) + quantElem(i.dir);
    }


    // Exercício 4 do slide C de árvores binárias
    public static int elementoPar(No Raiz){
        No i = Raiz;
        int soma ;
        if (i== null){
            return 0;
        }
    
        if((i.elemento % 2) == 0){
            return soma = 1 + elementoPar(i.esq) + elementoPar(i.dir);
        }else{
            return soma = 0 + elementoPar(i.esq) + elementoPar(i.dir);// sera que o max vai ficar puto ??
        }
    }
    
    public static int elementoPar(No Raiz){
                No i = Raiz;
                int soma ;
                if (i== null){
                return 0;
                }
                if((i.elemento % 2) == 0){
                        return soma = 1 + elementoPar(i.esq) + elementoPar(i.dir);
                }else{
                        return soma = 0 + elementoPar(i.esq) + elementoPar(i.dir);
                }

        }


        public static boolean iguais(No raiz1, No raiz2){

                if(raiz1== null && raiz2 == null){
                        return true;
                }
                if((raiz1.elemento != raiz2.elemento)|| (raiz1 != null &&  raiz2 == null) ||(raiz1 == null &&  raiz2 != null)  ){
                        return false;
                }

                return iguais(raiz1.esq, raiz2.esq) && iguais(raiz1.dir, raiz2.dir);

        }

        public static boolean divPorOnze(No raiz){// chamada pricnipal 
                if(raiz==null){// quando vai parar 
                        return false;
                }
                if((raiz.elemento %11) == 0){
                        return true;// unico caso de ser verdadeiro e parar 
                }else{
                        return divPorOnze(raiz.dir) ||  divPorOnze(raiz.esq);// entregar para quem chama essa info, bastando um dos lados ser verdadeiro  
                }
        }
	 public static No resultArvore(Celula p1.prox, CelulaDupla p2.prox){// o max fez uma linha inicializando cada um com p1 = p.prox;
                No raiz = null
                while(p1 != null && p2 != null){
                        raiz = inserir (raiz, p1.elemento);
                        raiz = inserir (raiz, p2.elemento);
                        p1 = p1.prox;
                        p2 = p2.prox
                }
                while(p1 != null && p2 == null){
                        raiz = inserir (raiz, p2.elemento);
                        p2= p2.prox;
                }
                while(p1 == null && p2 != null){
                        raiz = inserir (raiz, p1.elemento);
                        p1= p1.prox;
                }
                return raiz;   }



	public static boolean pesPala(No Raiz, String nome){
		if(raiz == null){
			return false;
		}
		if(raiz.palavra == nome){
			return true;
		}else{
			return pesPala(raiz.esq, nome) || pesPala(raiz.dir, nome);
		}
	}

	public static boolean pesqLetra(No raiz1, String nome){

		if(nome.charAt(0)== raiz1.letra){
			return acharPala(raiz1.prox, nome);
		}

		else if(nome.charAt(0)> raiz1.letra){
			return pesqLetra(raiz.esq, nome);
		}

		else if(nome.charAT(0)< raiz1.letra){
			return pesqLetra(raiz.dir, nome);
		}

		else{
			return false;
		}
	} public static No resultArvore(Celula p1.prox, CelulaDupla p2.prox){// o max fez uma linha inicializando cada um com p1 = p.prox;
                No raiz = null
                while(p1 != null && p2 != null){
                        raiz = inserir (raiz, p1.elemento);
                        raiz = inserir (raiz, p2.elemento);
                        p1 = p1.prox;
                        p2 = p2.prox
                }
                while(p1 != null && p2 == null){
                        raiz = inserir (raiz, p2.elemento);
                        p2= p2.prox;
                }
                while(p1 == null && p2 != null){
                        raiz = inserir (raiz, p1.elemento);
                        p1= p1.prox;
                }
                return raiz;   }







    public static void main(String[] args){

    }
}