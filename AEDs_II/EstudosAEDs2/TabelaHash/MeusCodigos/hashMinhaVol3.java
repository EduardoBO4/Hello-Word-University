import java.util.*;

// ==========================================
// CLASSE PESSOA
// ==========================================
class Pessoa {
    private String nome;
    private int dia;
    private int mes;

    public Pessoa(String nome, int dia, int mes) {
        this.nome = nome;
        this.dia = dia;
        this.mes = mes;
    }

    public Pessoa() {
        this.nome = "";
        this.dia = 0; 
        this.mes = 0;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public void setDia(int dia) {
        this.dia = dia;
    }
    public void setMes(int mes) {
        this.mes = mes;
    }
    public String getNome() {
        return this.nome;
    }
    public int getDia() {
        return this.dia;
    }
    public int getMes() {
        return this.mes;
    }
}

// ==========================================
// CLASSE CELULA (NÓ DA LISTA FLEXÍVEL)
// ==========================================
class Celula {
    public Pessoa elemento; 
    public Celula prox; 

    // Construtor para células com dados
    public Celula(Pessoa pessoa) {
        this.elemento = pessoa;
        this.prox = null;
    }

    // Construtor vazio (usado para criar o Nó Cabeça)
    public Celula() {
        this.elemento = new Pessoa();
        this.prox = null;
    }
}

// ==========================================
// CLASSE PRINCIPAL DA TABELA HASH INDIRETA
// ==========================================
public class hashMinha {
    private Celula[] tabela; // Array de ponteiros/células

    public hashMinha() {
        // Inicializa a tabela com 366 posições (uma para cada dia do ano)
        tabela = new Celula[366];

        // Cria o "Nó Cabeça" de segurança para cada uma das posições
        for (int i = 0; i < 366; i++) {
            tabela[i] = new Celula(); 
        }
    }

    /**
     * FUNÇÃO HASH (A CALCULADORA)
     * Retorna o índice ideal (0 a 365) baseado na data.
     */
    public int funcaoHash(int dia, int mes) {
        int meses[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int cont = 0;
        
        for (int i = 0; i < mes; i++) {
            cont += meses[i];
        }
        cont += dia;
    
        return cont - 1; // ÚNICO return
    }

    /**
     * MÉTODO INSERIR
     * Insere a nova pessoa logo após o Nó Cabeça da posição calculada.
     * Como a lista é flexível, nunca dá "tabela cheia".
     */
    public boolean inserir(Pessoa p) {
        boolean sucesso = false;

        // 1. Calcula a posição do dia do ano
        int pos = funcaoHash(p.getDia(), p.getMes());

        // 2. Cria a nova célula com os dados da pessoa
        Celula nova = new Celula(p);

        // 3. Faz a inserção no início da lista (logo após o nó cabeça tabela[pos])
        nova.prox = tabela[pos].prox;
        tabela[pos].prox = nova;

        sucesso = true;
        System.out.println(p.getNome() + " inserido(a) com sucesso no índice " + pos);

        return sucesso; // ÚNICO return
    }

    /**
     * MÉTODO PESQUISAR
     * Vai direto na posição hash e varre a lista flexível procurando pelo nome.
     */
    public Pessoa pesquisar(int dia, int mes, String nome) {
        Pessoa pessoaEncontrada = null;
        boolean achou = false;

        // 1. Descobre em qual lista a pessoa deve estar
        int pos = funcaoHash(dia, mes);

        // 2. Ponteiro auxiliar começa na primeira célula com dados (depois do nó cabeça)
        Celula i = tabela[pos].prox;

        // 3. Caminha pela lista enquanto não chegar ao fim e não tiver encontrado
        while (i != null && !achou) {
            if (i.elemento.getNome().equals(nome)) {
                pessoaEncontrada = i.elemento; // Captura a pessoa
                achou = true;                  // Altera a flag para encerrar o laço
            } else {
                i = i.prox;                    // Vai para a próxima célula
            }
        }

        if (!achou) {
            System.out.println("Pessoa não encontrada.");
        }

        return pessoaEncontrada; // ÚNICO return
    }

    /**
     * MÉTODO REMOVER
     * Varre a lista a partir do Nó Cabeça, desvincula a célula e retorna a pessoa excluída.
     */
    public Pessoa remover(int dia, int mes, String nome) {
        Pessoa pessoaRemovida = null;
        boolean achou = false;

        // 1. Descobre a posição da lista
        int pos = funcaoHash(dia, mes);

        // 2. Ponteiro 'i' começa no próprio NÓ CABEÇA
        Celula i = tabela[pos];

        // 3. Percorre a lista olhando sempre para o próximo nó (i.prox)
        while (i.prox != null && !achou) {
            if (i.prox.elemento.getNome().equals(nome)) {
                pessoaRemovida = i.prox.elemento; // Guarda a pessoa antes de apagar
                i.prox = i.prox.prox;             // "Pula" a célula alvo, removendo-a da corrente
                System.out.println(nome + " removido(a) com sucesso!");
                achou = true;                     // Altera a flag para encerrar o laço
            } else {
                i = i.prox;                       // Avança o ponteiro auxiliar
            }
        }

        if (!achou) {
            System.out.println("Não foi possível remover: pessoa não encontrada.");
        }

        return pessoaRemovida; // ÚNICO return
    }
}