import java.util.*;

/*O método hash (e o rehash) funciona como uma calculadora de endereços: 
ele recebe os dados e retorna apenas o índice (int) ou ponteiro da posição ideal 
na memória. Ele não grava nada. */

public class Pessoa{
    private String nome;
    private int dia;
    private int mes;
    public Pessoa(String nome, int dia, int mes){
        this.nome = nome;
        this.dia = dia;
        this.mes = mes;
    }
    void setNome(String nome){
        this.nome = nome;
    }
    void setDia(int dia){
        this.dia = dia;
    }
    void setMes(int mes){
        this.mes = mes;
    }
    String getNome(){
        return this.nome;
    }
    int getDia(){
        return this.dia;
    }
    int getMes(){
        return this.mes;
    }
}
public class hashMinha {

    private Pessoa[] tabela;
    private final int TAM_PRINCIPAL = 366;
    private final int TAM_TOTAL = 386; // 366 + 20 posições de reserva

    public hashMinha() {
        // Inicializa a tabela com o tamanho total (principal + reserva)
        tabela = new Pessoa[TAM_TOTAL];
    }

    /**
     * MÉTODO HASH (CALCULADORA PRINCIPAL)
     * Retorna o índice ideal (0 a 365) baseado na data.
     */
    public int funcaoHash(int dia, int mes) {
        int meses[] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int cont = 0;
        
        for (int i = 0; i < mes; i++) {
            cont += meses[i];
        }
        cont += dia;
        int posicaoCalculada = cont - 1;

        return posicaoCalculada; // ÚNICO return
    }

    /**
     * MÉTODO REHASH (CALCULADORA DE RESERVA)
     * Varre a área de reserva (índices 366 a 385) procurando uma vaga.
     * Retorna o índice livre encontrado, ou -1 se a reserva estiver lotada.
     */
    public int funcaoRehash() {
        int posicaoReserva = -1; // -1 significa "não encontrou vaga"

        // Percorre estritamente a área de reserva (de 366 até 385)
        for (int i = TAM_PRINCIPAL; i < TAM_TOTAL; i++) {
            if (tabela[i] == null) {
                posicaoReserva = i;
                i = TAM_TOTAL; // Força a saída do laço ao achar a primeira vaga
            }
        }

        return posicaoReserva; // ÚNICO return
    }

    /**
     * MÉTODO INSERIR
     * Retorna true se inseriu (na principal ou reserva) e false se falhou.
     */
    public boolean inserir(Pessoa p) {
        boolean sucesso = false;

        // 1. Tenta a posição ideal
        int pos = funcaoHash(p.getDia(), p.getMes());

        if (tabela[pos] == null) {
            tabela[pos] = p;
            sucesso = true;
            System.out.println(p.getNome() + " inserido na tabela principal (Índice " + pos + ").");
        } else {
            // 2. Colisão! Procura uma vaga na área de reserva usando o rehash
            System.out.println("Colisão para " + p.getNome() + " no índice " + pos + ". Buscando reserva...");
            int posReserva = funcaoRehash();

            if (posReserva != -1) {
                tabela[posReserva] = p;
                sucesso = true;
                System.out.println(p.getNome() + " inserido na área de reserva (Índice " + posReserva + ").");
            } else {
                System.out.println("Erro: Área de reserva cheia! Não foi possível inserir " + p.getNome());
            }
        }

        return sucesso; // ÚNICO return
    }

    /**
     * MÉTODO PESQUISAR
     * Retorna o objeto Pessoa se achar (na principal ou na reserva), ou null se não achar.
     */
    public Pessoa pesquisar(int dia, int mes, String nome) {
        Pessoa pessoaEncontrada = null;

        // 1. Procura na posição ideal
        int pos = funcaoHash(dia, mes);

        if (tabela[pos] != null && tabela[pos].getNome().equals(nome)) {
            pessoaEncontrada = tabela[pos];
        } else {
            // 2. Se não estava lá, percorre a área de reserva inteira procurando por nome
            System.out.println("Não está na posição ideal. Procurando na área de reserva...");
            for (int i = TAM_PRINCIPAL; i < TAM_TOTAL; i++) {
                if (tabela[i] != null && tabela[i].getNome().equals(nome)) {
                    pessoaEncontrada = tabela[i];
                    i = TAM_TOTAL; // Interrompe a busca ao encontrar
                }
            }
        }

        return pessoaEncontrada; // ÚNICO return
    }

    /**
     * MÉTODO REMOVER
     * Retorna o objeto Pessoa removido, ou null se não encontrar.
     */
    public Pessoa remover(int dia, int mes, String nome) {
        Pessoa pessoaRemovida = null;

        // 1. Tenta remover da posição ideal
        int pos = funcaoHash(dia, mes);

        if (tabela[pos] != null && tabela[pos].getNome().equals(nome)) {
            pessoaRemovida = tabela[pos];
            tabela[pos] = null; // Libera o espaço
            System.out.println(nome + " removido da tabela principal.");
        } else {
            // 2. Se não estava lá, procura e remove da área de reserva
            System.out.println("Não encontrado na principal. Buscando na reserva para remover...");
            for (int i = TAM_PRINCIPAL; i < TAM_TOTAL; i++) {
                if (tabela[i] != null && tabela[i].getNome().equals(nome)) {
                    pessoaRemovida = tabela[i];
                    tabela[i] = null; // Libera o espaço na reserva
                    System.out.println(nome + " removido da área de reserva.");
                    i = TAM_TOTAL; // Interrompe a busca
                }
            }
        }

        return pessoaRemovida; // ÚNICO return
    }
}



