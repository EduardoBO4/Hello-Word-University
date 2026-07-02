import Pratica.No;

public class Arvores {
    class No {
        int valor;
        No esquerda;
        No direita;
        boolean cor; // true para preto, false para branco 
        //num construtor 
        public No(int valor) {
            this.valor = valor;
            this.esquerda = null;
            this.direita = null;
            this.cor = false; // Inicialmente, o nó nasce branco
        }
        // num 2 ver se é de 4 
        public boolean isNoTipo4() {
            if (this.direita != null && this.esquerda != null) {
                // Mantendo a lógica original onde o tipo 4 é baseado na cor false
                if (this.direita.cor == false && this.esquerda.cor == false) {
                    return true;
                }
            }
            return false;
        }
    }
    // num 3 arvore bi(color)(ssexual)(naria)(campeam)(polar)(pede)()
    class ArvoreBicolor {
        No raiz;

        public ArvoreBicolor() {
            this.raiz = null;
        }
        // num 4 fragmentar
        private void fragmentar(No no) {
            if (no.isNoTipo4()) {
                no.esquerda.cor = true; // Muda para preto
                no.direita.cor = true;  // Muda para preto
                if (no != raiz) {
                    no.cor = false; // Muda para branco
                }
            }
        }
         // NUm 5
        private No rotacionarDir(No no) {
            No noEsq = no.esquerda;
            No noEsqDir = noEsq.direita;
            noEsq.direita = no;
            no.esquerda = noEsqDir;
            return noEsq;
        }

        private No rotacionarEsq(No no) {
            No noDir = no.direita;
            No noDirEsq = noDir.esquerda;
            noDir.esquerda = no;
            no.direita = noDirEsq;
            return noDir;
        }

        private No rotacionarDirEsq(No no) {
            no.direita = rotacionarDir(no.direita);
            return rotacionarEsq(no);
        }

        private No rotacionarEsqDir(No no) {
            no.esquerda = rotacionarEsq(no.esquerda);
            return rotacionarDir(no);
        }

        // num 6 balancear e mudar as cores
       private void balancear(No bisavo, No avo, No pai, No i) {

            if (avo == null || pai == null || i == null) {
                return;
            }

            if (pai.cor == false) {
                return;
            }

            No novaRaiz = null;

            if (pai.valor > avo.valor && i.valor > pai.valor) {
                novaRaiz = rotacionarEsq(avo);

            } else if (pai.valor > avo.valor && i.valor < pai.valor) {
                novaRaiz = rotacionarDirEsq(avo);

            } else if (pai.valor < avo.valor && i.valor < pai.valor) {
                novaRaiz = rotacionarDir(avo);

            } else if (pai.valor < avo.valor && i.valor > pai.valor) {
                novaRaiz = rotacionarEsqDir(avo);
            }


            if (novaRaiz != null) {

                novaRaiz.cor = true;

                if (novaRaiz.esquerda != null)
                    novaRaiz.esquerda.cor = false;

                if (novaRaiz.direita != null)
                    novaRaiz.direita.cor = false;


                if (bisavo == null) {
                    this.raiz = novaRaiz;

                } else if (novaRaiz.valor < bisavo.valor) {
                    bisavo.esquerda = novaRaiz;

                } else {
                    bisavo.direita = novaRaiz;
                }
            }
        }

        // num 7 e 8 inserção dos dois modos
        public void inserir(int x) {
            if (this.raiz == null) {
                this.raiz = new No(x);
                this.raiz.cor = true; // Raiz global é preta
            } else {
                inserirRecursivo(x, null, null, null, this.raiz);
            }
        }

        private void inserirRecursivo(int x, No bisavo, No avo, No pai, No i) {
            if (i.isNoTipo4()) {
                fragmentar(i);
                if (pai != null && pai.cor == true) {
                    balancear(bisavo, avo, pai, i);
                    inserirRecursivo(x, null, null, null, this.raiz);
                    return;
                }
            }

            if (x < i.valor) {
                if (i.esquerda == null) {
                    i.esquerda = new No(x);
                    if (i.cor == true) balancear(avo, pai, i, i.esquerda);
                } else {
                    inserirRecursivo(x, avo, pai, i, i.esquerda);
                }
            } else if (x > i.valor) {
                if (i.direita == null) {
                    i.direita = new No(x);
                    if (i.cor == true) balancear(avo, pai, i, i.direita);
                } else {
                    inserirRecursivo(x, avo, pai, i, i.direita);
                }
            }
            this.raiz.cor = true;
        }

        // num 9 Caminhamento
        public void caminhamentoCentral() {
            caminhamentoCentralRecursivo(this.raiz);
        }

        private void caminhamentoCentralRecursivo(No no) {
            if (no != null) {
                caminhamentoCentralRecursivo(no.esquerda);
                System.out.print(no.valor + "(cor=" + (no.cor ? "1" : "0") + ") ");
                caminhamentoCentralRecursivo(no.direita);
            }
        }
    }

    public static void main(String[] args) {// não sei como o pedro vai pedir dps eu altero a main
        ArvoreBicolor arvore = new Arvores().new ArvoreBicolor();
        int[] elementos = {4, 35, 10, 13, 3, 30, 15, 12, 7, 40, 20};
        
        for (int e : elementos) arvore.inserir(e);
        
        System.out.println("Caminhamento Central:");
        arvore.caminhamentoCentral();
    }
}