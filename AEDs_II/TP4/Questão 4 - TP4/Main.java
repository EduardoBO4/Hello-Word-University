import java.io.*;
import java.util.*;

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) { this.hora = hora; this.minuto = minuto; }
    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }
    public void setHora(int hora) { this.hora = hora; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public static Hora parseHora(String s) {
        Scanner scan = new Scanner(s);
        scan.useDelimiter(":");
        int hora = scan.nextInt(); int minuto = scan.nextInt();
        scan.close();
        return new Hora(hora, minuto);
    }

    public String formatar() { return String.format("%02d:%02d", this.hora, this.minuto); }
}

class Data {
    private int ano, mes, dia; 

    public Data(int ano, int mes, int dia) { this.ano = ano; this.mes = mes; this.dia = dia; }
    public int getAno() { return ano; } public int getMes() { return mes; } public int getDia() { return dia; }

    public static Data parseData(String s) {
        Scanner scan = new Scanner(s);
        scan.useDelimiter("-");
        int ano = scan.nextInt(); int mes = scan.nextInt(); int dia = scan.nextInt();
        scan.close();
        return new Data(ano, mes, dia);
    }

    public String formatar() { return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano); }
}

class Restaurante {
    private int idRestaurante;
    private String nome;
    private String city; 
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixa_preco;
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;

    public Restaurante(int idRestaurante, String nome, String cidade, int capacidade, double avaliacao,
            String[] tiposCozinha, int faixa_preco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto) {
        this.idRestaurante = idRestaurante; this.nome = nome; this.city = cidade;
        this.capacidade = capacidade; this.avaliacao = avaliacao; this.tiposCozinha = tiposCozinha;
        this.faixa_preco = faixa_preco; this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento; this.dataAbertura = dataAbertura; this.aberto = aberto;
    }

    public int getIdRestaurante() { return idRestaurante; }
    public String getNome() { return nome; }

    public static int pegarFaixa_Preco(String s) {
        int cont = 0;
        for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '$') cont++;
        return cont;
    }

    public static Restaurante parseRestaurante(String s) {
        Scanner scan = new Scanner(s);
        scan.useLocale(Locale.US);
        scan.useDelimiter(",");
        int id = scan.nextInt();
        String nome = scan.next(); String cidade = scan.next();
        int capacidade = scan.nextInt(); double avaliacao = scan.nextDouble();
        String tpCozinha = scan.next();
        int faixa_preco = pegarFaixa_Preco(scan.next());
        String horarios = scan.next();
        Scanner scanHorarios = new Scanner(horarios);
        scanHorarios.useDelimiter("-");
        Hora horaAbertura = Hora.parseHora(scanHorarios.next());
        Hora horaFechamento = Hora.parseHora(scanHorarios.next());
        scanHorarios.close();
        Data dataAbertura = Data.parseData(scan.next());
        String abertoStr = scan.next();
        boolean aberto = (abertoStr.compareTo("true") == 0);
        scan.close();
        
        String[] aux = new String[10]; int cout = 0;
        Scanner scanTp = new Scanner(tpCozinha); scanTp.useDelimiter(";");
        while (scanTp.hasNext()) { 
            String p = scanTp.next(); 
            if (p.length() > 0) { 
                aux[cout] = p; 
                cout++; 
            } 
        }
        scanTp.close();
        String[] tipoCozinha = new String[cout];
        for (int i = 0; i < cout; i++) tipoCozinha[i] = aux[i];
        return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipoCozinha,
                faixa_preco, horaAbertura, horaFechamento, dataAbertura, aberto);
    }

    public String formatar() {
        String strCozinhas = "";
        for (int i = 0; i < tiposCozinha.length; i++) {
            strCozinhas += tiposCozinha[i];
            if (i < tiposCozinha.length - 1) strCozinhas += ",";
        }
        String faixa_p = "";
        for (int i = 0; i < faixa_preco; i++) faixa_p += '$';
        String strAvaliacao = avaliacao + "";
        return String.format("[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]",
                idRestaurante, nome, city, capacidade, strAvaliacao, strCozinhas,
                faixa_p, horarioAbertura.formatar(), horarioFechamento.formatar(),
                dataAbertura.formatar(), aberto);
    }
}

class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;

    public ColecaoRestaurantes(int tamanho) {
        this.tamanho = tamanho;
        this.restaurantes = new Restaurante[tamanho];
    }

    public int getTamanho() { return tamanho; }

    public void lerCsv(String path) throws Exception {
        File arquivo = new File(path);
        Scanner scan = new Scanner(arquivo);
        if (scan.hasNextLine()) scan.nextLine();
        int i = 0;
        while (scan.hasNextLine()) {
            restaurantes[i] = Restaurante.parseRestaurante(scan.nextLine());
            i++;
        }
        scan.close();
    }

    public static ColecaoRestaurantes lerCsv() throws Exception {
        File arquivo = new File("/tmp/restaurantes.csv");
        Scanner scan = new Scanner(arquivo);
        int tam = 0;
        while (scan.hasNext()) { scan.nextLine(); tam++; }
        scan.close();
        ColecaoRestaurantes novaCol = new ColecaoRestaurantes(tam - 1);
        novaCol.lerCsv("/tmp/restaurantes.csv");
        return novaCol;
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++)
            if (restaurantes[i].getIdRestaurante() == id) return restaurantes[i];
        return null;
    }
}

class HashRehash {
    private static final int TAM_TAB = 83;
    private Restaurante[] tabela;
    public long comparacoes;

    public HashRehash() {
        this.tabela = new Restaurante[TAM_TAB];
        this.comparacoes = 0;
        for (int i = 0; i < TAM_TAB; i++) tabela[i] = null;
    }

    // calcula o ascii somando letra por letra
    private int somaAscii(String nome) {
        int soma = 0;
        for (int i = 0; i < nome.length(); i++) soma += (int) nome.charAt(i);
        return soma;
    }

    private int h1(String nome) { return somaAscii(nome) % TAM_TAB; }
    private int h2(String nome) { return (somaAscii(nome) + 1) % TAM_TAB; }

    public void inserir(Restaurante r) {
        int pos1 = h1(r.getNome());
        if (tabela[pos1] == null) {
            tabela[pos1] = r;
            return;
        }
        
        // colidiu no h1, tenta a segunda funcao de hash (rehash)
        int pos2 = h2(r.getNome());
        if (tabela[pos2] == null) {
            tabela[pos2] = r;
            return;
        }
        
        System.out.println(r.getNome());
    }

    public int pesquisar(String nome) {
        int pos1 = h1(nome);
        comparacoes++;
   if (tabela[pos1] != null && tabela[pos1].getNome().equals(nome)) {
            return pos1;
   }
        
        int pos2 = h2(nome);
        comparacoes++;
        if (tabela[pos2] != null && tabela[pos2].getNome().equals(nome)) {
            return pos2;
        }
        return -1;
    }

    public Restaurante getRestaurante(int pos) {
        return tabela[pos];
    }
}

public class Main {
    static final String MATRICULA = "890309";

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        ColecaoRestaurantes cr = ColecaoRestaurantes.lerCsv();

        String linha = scan.next();
        HashRehash hash = new HashRehash();

        // lendo ate vir o flag de parada
        while (linha.compareTo("-1") != 0) {
            int id = Integer.parseInt(linha);
            Restaurante r = cr.buscarPorId(id);
         if (r != null) hash.inserir(r);
            linha = scan.next();
        }

        scan.nextLine();
        linha = scan.nextLine();

        long inicio = System.nanoTime();

        while (linha.compareTo("FIM") != 0) {
            int pos = hash.pesquisar(linha);
            if (pos == -1) {
                System.out.println(-1);
            } else {
                System.out.println(pos + " " + hash.getRestaurante(pos).formatar());
            }
            linha = scan.nextLine();
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0; // tirado o literal com underline pra ficar padrao antigo
        scan.close();

        // saida padrao tp
        FileWriter arq = new FileWriter(MATRICULA + "_hash_rehash.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("%s\t%d\t%.2f\n", MATRICULA, hash.comparacoes, tempoMs);
        gravarArq.close();
    }
}