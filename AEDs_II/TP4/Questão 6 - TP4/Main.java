import java.io.*;
import java.util.*;

class Hora {
    private int hora; private int minuto;
    public Hora(int hora, int minuto) { this.hora = hora; this.minuto = minuto; }
    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }
    public static Hora parseHora(String s) {
        Scanner scan = new Scanner(s); scan.useDelimiter(":");
        int hora = scan.nextInt(); int minuto = scan.nextInt(); scan.close();
        return new Hora(hora, minuto);
    }
    public String formatar() { return String.format("%02d:%02d", this.hora, this.minuto); }
}

class Data {
    private int ano; private int mes; private int dia;
    public Data(int ano, int mes, int dia) { this.ano = ano; this.mes = mes; this.dia = dia; }
    public static Data parseData(String s) {
        Scanner scan = new Scanner(s); scan.useDelimiter("-");
        int ano = scan.nextInt(); int mes = scan.nextInt(); int dia = scan.nextInt(); scan.close();
        return new Data(ano, mes, dia);
    }
    public String formatar() { return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano); }
}

class Restaurante {
    private int idRestaurante; private String nome; private String cidade;
    private int capacidade; private double avaliacao; private String[] tiposCozinha;
    private int faixa_preco; private Hora horarioAbertura; private Hora horarioFechamento;
    private Data dataAbertura; private boolean aberto;

    public Restaurante(int idRestaurante, String nome, String cidade, int capacidade, double avaliacao,
            String[] tiposCozinha, int faixa_preco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto) {
        this.idRestaurante = idRestaurante; this.nome = nome; this.cidade = cidade;
        this.capacidade = capacidade; this.avaliacao = avaliacao; this.tiposCozinha = tiposCozinha;
        this.faixa_preco = faixa_preco; this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento; this.dataAbertura = dataAbertura; this.aberto = aberto;
    }
    public int getIdRestaurante() { return idRestaurante; }
    public String getNome() { return nome; }
    public int getCapacidade() { return capacidade; }
    public static int pegarFaixa_Preco(String s) {
        int c = 0; for (int i = 0; i < s.length(); i++) if (s.charAt(i) == '$') c++; return c;
    }
    public static Restaurante parseRestaurante(String s) {
        Scanner scan = new Scanner(s); scan.useLocale(Locale.US); scan.useDelimiter(",");
        int id = scan.nextInt(); String nome = scan.next(); String cidade = scan.next();
        int capacidade = scan.nextInt(); double avaliacao = scan.nextDouble();
        String tpCozinha = scan.next(); int faixa_preco = pegarFaixa_Preco(scan.next());
        String horarios = scan.next();
        Scanner scanH = new Scanner(horarios); scanH.useDelimiter("-");
        Hora hA = Hora.parseHora(scanH.next()); Hora hF = Hora.parseHora(scanH.next()); scanH.close();
        Data dataAbertura = Data.parseData(scan.next());
        String abertoStr = scan.next(); boolean aberto = (abertoStr.compareTo("true") == 0); scan.close();
        String[] aux = new String[10]; int cout = 0;
        Scanner scanTp = new Scanner(tpCozinha); scanTp.useDelimiter(";");
        while (scanTp.hasNext()) { String p = scanTp.next(); if (p.length() > 0) { aux[cout] = p; cout++; } }
        scanTp.close();
        String[] tipoCozinha = new String[cout];
        for (int i = 0; i < cout; i++) tipoCozinha[i] = aux[i];
        return new Restaurante(id, nome, cidade, capacidade, avaliacao, tipoCozinha,
                faixa_preco, hA, hF, dataAbertura, aberto);
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
                idRestaurante, nome, cidade, capacidade, strAvaliacao, strCozinhas,
                faixa_p, horarioAbertura.formatar(), horarioFechamento.formatar(),
                dataAbertura.formatar(), aberto);
    }
}

class ColecaoRestaurantes {
    private int tamanho; private Restaurante[] restaurantes;
    public ColecaoRestaurantes(int tamanho) { this.tamanho = tamanho; this.restaurantes = new Restaurante[tamanho]; }
    public void lerCsv(String path) throws Exception {
        File arquivo = new File(path); Scanner scan = new Scanner(arquivo);
        if (scan.hasNextLine()) scan.nextLine();
        int i = 0;
        while (scan.hasNextLine()) { restaurantes[i] = Restaurante.parseRestaurante(scan.nextLine()); i++; }
        scan.close();
    }
    public static ColecaoRestaurantes lerCsv() throws Exception {
        File arquivo = new File("/tmp/restaurantes.csv"); Scanner scan = new Scanner(arquivo);
        int tam = 0; while (scan.hasNext()) { scan.nextLine(); tam++; } scan.close();
        ColecaoRestaurantes novaCol = new ColecaoRestaurantes(tam - 1);
        novaCol.lerCsv("/tmp/restaurantes.csv"); return novaCol;
    }
    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++)
            if (restaurantes[i].getIdRestaurante() == id) return restaurantes[i];
        return null;
    }
}

class NoAVL {
    public Restaurante elemento;
    public NoAVL dir, esq;
    public int altura;
    public NoAVL(Restaurante x) { this.elemento = x; this.dir = this.esq = null; this.altura = 1; }
}

class ArvoreAVLInterna {
    public NoAVL raiz;
    public long comparacoes;
    public ArvoreAVLInterna() { this.raiz = null; this.comparacoes = 0; }

    private int getAltura(NoAVL no) { return (no == null) ? 0 : no.altura; }
    private void atualizarAltura(NoAVL no) { no.altura = 1 + Math.max(getAltura(no.esq), getAltura(no.dir)); }
    private int fatorBalanceamento(NoAVL no) { return (no == null) ? 0 : getAltura(no.esq) - getAltura(no.dir); }

    private NoAVL rotacaoDir(NoAVL y) {
        NoAVL x = y.esq; NoAVL T2 = x.dir;
        x.dir = y; y.esq = T2;
        atualizarAltura(y); atualizarAltura(x); return x;
    }
    private NoAVL rotacaoEsq(NoAVL x) {
        NoAVL y = x.dir; NoAVL T2 = y.esq;
        y.esq = x; x.dir = T2;
        atualizarAltura(x); atualizarAltura(y); return y;
    }

    public void inserir(Restaurante x) { raiz = inserir(x, raiz); }
    private NoAVL inserir(Restaurante x, NoAVL i) {
        if (i == null) return new NoAVL(x);
        if (i.elemento.getNome().compareTo(x.getNome()) > 0) i.esq = inserir(x, i.esq);
        else if (i.elemento.getNome().compareTo(x.getNome()) < 0) i.dir = inserir(x, i.dir);
        else return i;
        atualizarAltura(i);
        int fb = fatorBalanceamento(i);
        if (fb > 1 && x.getNome().compareTo(i.esq.elemento.getNome()) < 0) return rotacaoDir(i);
        if (fb < -1 && x.getNome().compareTo(i.dir.elemento.getNome()) > 0) return rotacaoEsq(i);
        if (fb > 1 && x.getNome().compareTo(i.esq.elemento.getNome()) > 0) { i.esq = rotacaoEsq(i.esq); return rotacaoDir(i); }
        if (fb < -1 && x.getNome().compareTo(i.dir.elemento.getNome()) < 0) { i.dir = rotacaoDir(i.dir); return rotacaoEsq(i); }
        return i;
    }

    public boolean pesquisar(String x) {
        System.out.print("raiz ");
        return pesquisar(x, raiz);
    }
    private boolean pesquisar(String x, NoAVL i) {
        if (i == null) return false;
        comparacoes++;
        int cmp = i.elemento.getNome().compareTo(x);
        if (cmp == 0) {
            System.out.print("SIM " + i.elemento.formatar());
            return true;
        } else if (cmp > 0) {
            System.out.print("esq ");
            return pesquisar(x, i.esq);
        } else {
            System.out.print("dir ");
            return pesquisar(x, i.dir);
        }
    }
}

class NoBSTExterno {
    public int chave;
    public ArvoreAVLInterna avl;
    public NoBSTExterno esq, dir;
    public NoBSTExterno(int chave) {
        this.chave = chave; this.avl = new ArvoreAVLInterna(); this.esq = this.dir = null;
    }
}

class ArvoreHibrida {
    public NoBSTExterno raiz;
    public ArvoreHibrida() { this.raiz = null; }

    public void inserir(Restaurante r) {
        int chave = r.getCapacidade() % 15;
        raiz = inserir(r, chave, raiz);
    }

    private NoBSTExterno inserir(Restaurante r, int chave, NoBSTExterno no) {
        if (no == null) {
            no = new NoBSTExterno(chave);
            no.avl.inserir(r);
            return no;
        }
        if (chave < no.chave) no.esq = inserir(r, chave, no.esq);
        else if (chave > no.chave) no.dir = inserir(r, chave, no.dir);
        else no.avl.inserir(r);
        return no;
    }

    public boolean pesquisar(NoBSTExterno no, String dir, String nome) {
        System.out.print(dir + " ");
        if (no == null) return false;
        if (no.avl.pesquisar(nome)) return true;
        if (pesquisar(no.esq, "ESQ", nome)) return true;
        if (pesquisar(no.dir, "DIR", nome)) return true;
        return false;
    }

    public long getComparacoes() {
        return somarComparacoes(raiz);
    }
    private long somarComparacoes(NoBSTExterno no) {
        if (no == null) return 0;
        return no.avl.comparacoes + somarComparacoes(no.esq) + somarComparacoes(no.dir);
    }
}

public class Main {
    static final String MATRICULA = "890309";

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        ColecaoRestaurantes cr = ColecaoRestaurantes.lerCsv();

        String linha = scan.next();
        ArvoreHibrida arvore = new ArvoreHibrida();

        while (linha.compareTo("-1") != 0) {
            int id = Integer.parseInt(linha);
            Restaurante r = cr.buscarPorId(id);
            if (r != null) arvore.inserir(r);
            linha = scan.next();
        }

        scan.nextLine();
        linha = scan.nextLine();

        long inicio = System.nanoTime();

        while (linha.compareTo("FIM") != 0) {
            boolean achou = arvore.pesquisar(arvore.raiz, "RAIZ", linha);
            if (!achou) System.out.print("NAO");
            System.out.println();
            linha = scan.nextLine();
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1_000_000.0;
        scan.close();

        FileWriter arq = new FileWriter(MATRICULA + "_hibrida_arvore_arvore.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("%s\t%d\t%.2f\n", MATRICULA, arvore.getComparacoes(), tempoMs);
        gravarArq.close();
    }
}
