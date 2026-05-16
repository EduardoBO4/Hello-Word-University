import java.util.Scanner;

class UnidadeMatriz {
    public int dado;
    public UnidadeMatriz esquerda, direita, acima, abaixo;

    public UnidadeMatriz(int dado) {
        this.dado = dado;
        this.esquerda = this.direita = this.acima = this.abaixo = null;
    }

    public UnidadeMatriz() {
        this(0);
    }
}

class GradeDinamica {
    private UnidadeMatriz raiz;
    private int totalLinhas, totalColunas;

    public GradeDinamica(int linhas, int colunas) {
        this.totalLinhas = linhas;
        this.totalColunas = colunas;

        UnidadeMatriz temp = new UnidadeMatriz();
        this.raiz = temp;

        for (int j = 1; j < colunas; j++) {
            temp.direita = new UnidadeMatriz();
            temp.direita.esquerda = temp;
            temp = temp.direita;
        }

        UnidadeMatriz linhaDeCima = this.raiz;

        for (int i = 1; i < linhas; i++) {
            UnidadeMatriz novaLinha = new UnidadeMatriz();
            linhaDeCima.abaixo = novaLinha;
            novaLinha.acima = linhaDeCima;

            UnidadeMatriz cursorCima = linhaDeCima.direita;
            UnidadeMatriz cursorBaixo = novaLinha;

            for (int j = 1; j < colunas; j++) {
                UnidadeMatriz novo = new UnidadeMatriz();
                cursorBaixo.direita = novo;
                novo.esquerda = cursorBaixo;

                cursorCima.abaixo = novo;
                novo.acima = cursorCima;

                cursorBaixo = cursorBaixo.direita;
                cursorCima = cursorCima.direita;
            }
            linhaDeCima = novaLinha;
        }
    }

    public void preencherDados(Scanner leitor) {
        UnidadeMatriz linhaAtual = this.raiz;
        for (int i = 0; i < totalLinhas; i++) {
            UnidadeMatriz colAtual = linhaAtual;
            for (int j = 0; j < totalColunas; j++) {
                colAtual.dado = leitor.nextInt();
                colAtual = colAtual.direita;
            }
            linhaAtual = linhaAtual.abaixo;
        }
    }

    public void exibirDiagonalPrincipal() {
        if (this.totalLinhas != this.totalColunas) return;
        UnidadeMatriz atual = this.raiz;
        boolean primeiro = true;
        
        while (atual != null) {
            if (!primeiro) {
                System.out.print(" ");
            }
            System.out.print(atual.dado);
            primeiro = false;
            
            atual = atual.direita;
            if (atual != null) {
                atual = atual.abaixo;
            }
        }
        System.out.println();
    }

    public void exibirDiagonalSecundaria() {
        if (this.totalLinhas != this.totalColunas) return;
        UnidadeMatriz atual = this.raiz;
        
        while (atual.direita != null) {
            atual = atual.direita;
        }
        
        boolean primeiro = true;
        while (atual != null) {
            if (!primeiro) {
                System.out.print(" ");
            }
            System.out.print(atual.dado);
            primeiro = false;
            
            atual = atual.esquerda;
            if (atual != null) {
                atual = atual.abaixo;
            }
        }
        System.out.println();
    }

    public GradeDinamica somarMatrizes(GradeDinamica outra) {
        GradeDinamica resultante = new GradeDinamica(this.totalLinhas, this.totalColunas);
        UnidadeMatriz l1 = this.raiz;
        UnidadeMatriz l2 = outra.raiz;
        UnidadeMatriz lR = resultante.raiz;

        while (l1 != null) {
            UnidadeMatriz c1 = l1;
            UnidadeMatriz c2 = l2;
            UnidadeMatriz cR = lR;

            while (c1 != null) {
                cR.dado = c1.dado + c2.dado;
                c1 = c1.direita;
                c2 = c2.direita;
                cR = cR.direita;
            }

            l1 = l1.abaixo;
            l2 = l2.abaixo;
            lR = lR.abaixo;
        }

        return resultante;
    }

    public GradeDinamica multiplicarMatrizes(GradeDinamica outra) {
        GradeDinamica resultante = new GradeDinamica(this.totalLinhas, outra.totalColunas);
        UnidadeMatriz linhaMinha = this.raiz;
        UnidadeMatriz linhaRes = resultante.raiz;

        while (linhaMinha != null) {
            UnidadeMatriz colOutraInicio = outra.raiz;
            UnidadeMatriz colRes = linhaRes;

            while (colOutraInicio != null) {
                int produtoSoma = 0;
                UnidadeMatriz a = linhaMinha;
                UnidadeMatriz b = colOutraInicio;

                while (a != null && b != null) {
                    produtoSoma += a.dado * b.dado;
                    a = a.direita;
                    b = b.abaixo;
                }

                colRes.dado = produtoSoma;
                colRes = colRes.direita;
                colOutraInicio = colOutraInicio.direita;
            }

            linhaMinha = linhaMinha.abaixo;
            linhaRes = linhaRes.abaixo;
        }

        return resultante;
    }

    public void imprimirGrade() {
        UnidadeMatriz linha = this.raiz;
        while (linha != null) {
            UnidadeMatriz col = linha;
            boolean primeiro = true;
            
            while (col != null) {
                if (!primeiro) {
                    System.out.print(" ");
                }
                System.out.print(col.dado);
                primeiro = false;
                col = col.direita;
            }
            System.out.println();
            linha = linha.abaixo;
        }
    }
}

public class Principal {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);
        
        if (!entrada.hasNextInt()) {
            entrada.close();
            return;
        }

        int numCasos = entrada.nextInt();

        for (int k = 0; k < numCasos; k++) {
            int qtdL = entrada.nextInt();
            int qtdC = entrada.nextInt();

            GradeDinamica matriz1 = new GradeDinamica(qtdL, qtdC);
            matriz1.preencherDados(entrada);

            GradeDinamica matriz2 = new GradeDinamica(qtdL, qtdC);
            matriz2.preencherDados(entrada);

            matriz1.exibirDiagonalPrincipal();
            matriz2.exibirDiagonalSecundaria();

            GradeDinamica gradeSoma = matriz1.somarMatrizes(matriz2);
            gradeSoma.imprimirGrade();

            GradeDinamica gradeProduto = matriz1.multiplicarMatrizes(matriz2);
            gradeProduto.imprimirGrade();
        }

        entrada.close();
    }
}