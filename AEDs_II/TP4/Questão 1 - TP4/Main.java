import java.io.*;
import java.util.*;

class Hora {
    private int hora;
    private int minuto;

    public Hora(int hora, int minuto) {
        this.hora = hora;
        this.minuto = minuto;
    }

    public int getHora() { return hora; }
    public int getMinuto() { return minuto; }
    public void setHora(int hora) { this.hora = hora; }
    public void setMinuto(int minuto) { this.minuto = minuto; }

    public static Hora parseHora(String s) {
        Scanner scan = new Scanner(s);
        scan.useDelimiter(":");
        // as vezes o scanner da pau se o formato vir errado, mas por enquanto ok
        int hora = scan.nextInt();
        int minuto = scan.nextInt();
        scan.close();
        return new Hora(hora, minuto);
    }

    public String formatar() {
        return String.format("%02d:%02d", this.hora, this.minuto);
    }
}

class Data {
    private int ano;
    private int mes;
    private int dia;

    public Data(int ano, int mes, int dia) {
        this.ano = ano;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAno() { return ano; }
    public int getMes() { return mes; }
    public int getDia() { return dia; }
    public void setAno(int ano) { this.ano = ano; }
    public void setMes(int mes) { this.mes = mes; }
    public void setDia(int dia) { this.dia = dia; }

    public static Data parseData(String s) {
        Scanner scan = new Scanner(s);
        scan.useDelimiter("-");
        int ano = scan.nextInt();
        int mes = scan.nextInt();
        int dia = scan.nextInt();
        scan.close();
        return new Data(ano, mes, dia);
    }

    public String formatar() {
        // lembrando que a ordem aqui é dia/mes/ano tp mando
        return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
    }
}

class Restaurante {
    private int idRestaurante;
    private String nome;
    private String cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixa_preco; // mantendo o nome assim mesmo
    private Hora horarioAbertura;
    private Hora horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;

    public Restaurante(int idRestaurante, String nome, String cidade, int capacidade, double avaliacao,
            String[] tiposCozinha, int faixa_preco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto) {
        this.idRestaurante = idRestaurante;
        this.nome = nome;
        this.cidade = cidade;
        this.capacidade = capacidade;
        this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha;
        this.faixa_preco = faixa_preco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura;
        this.aberto = aberto;
    }

    public int getIdRestaurante() { return idRestaurante; }
    public String getNome() { return nome; }
    public String getCidade() { return cidade; }
    public int getCapacidade() { return capacidade; }
    public double getAvaliacao() { return avaliacao; }
    public String[] getTiposCozinha() { return tiposCozinha; }
    public int getFaixa_Preco() { return faixa_preco; }
    public Hora getHorarioAbertura() { return horarioAbertura; }
    public Hora getHorarioFechamento() { return horarioFechamento; }
    public Data getDataAbertura() { return dataAbertura; }
    public boolean isAberto() { return aberto; }

    public static int pegarFaixa_Preco(String s) {
        int cont = 0;
        for (int i = 0; i < s.length(); i++){
            if (s.charAt(i) == '$') {
                cont++;
            }
        }
        return cont;
    }

    public static Restaurante parseRestaurante(String s) {
        Scanner scan = new Scanner(s);
        scan.useLocale(Locale.US);
        scan.useDelimiter(",");
        int id = scan.nextInt();
        String nome = scan.next();
        String cidade = scan.next();
        int capacidade = scan.nextInt();
        double avaliacao = scan.nextDouble();
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
        
        // ternario aqui NUNCA
        boolean aberto;
        if (abertoStr.equals("true")) {
            aberto = true;
        } else {
            aberto = false;
        }
        scan.close();

        // n sei se pode usar arraylist
        String[] aux = new String[10];
        int cout = 0;
        Scanner scanTp = new Scanner(tpCozinha);
        scanTp.useDelimiter(";");
        while (scanTp.hasNext()) {
            String palavra = scanTp.next();
            if (palavra.length() > 0) {
                aux[cout] = palavra;
                cout++;
            }
        }
        scanTp.close();
        String[] tipoCozinha = new String[cout];
        for (int i = 0; i < cout; i++) {
            tipoCozinha[i] = aux[i];
        }

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
        
        return String.format("[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]",
                idRestaurante, nome, cidade, capacidade, avaliacao, strCozinhas,
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

    public void lerCsv(String path) throws Exception {
        File arquivo = new File(path);
        Scanner scan = new Scanner(arquivo);
        if (scan.hasNextLine()) scan.nextLine(); // pula cabecalho
        int i = 0;
        while (scan.hasNextLine()) {
            String linha = scan.nextLine();
            restaurantes[i] = Restaurante.parseRestaurante(linha);
            i++;
        }
        scan.close();
    }

    public static ColecaoRestaurantes lerCsv() throws Exception {
        File arquivo = new File("/tmp/restaurantes.csv");
        Scanner scan = new Scanner(arquivo);
        int tam = 0;
        while (scan.hasNextLine()) { 
            scan.nextLine(); 
            tam++; 
        }
        scan.close();
        ColecaoRestaurantes novaCol = new ColecaoRestaurantes(tam - 1);
        novaCol.lerCsv("/tmp/restaurantes.csv");
        return novaCol;
    }

    public Restaurante buscarPorId(int id) {
        for (int i = 0; i < tamanho; i++) {
            if (restaurantes[i].getIdRestaurante() == id) {
                return restaurantes[i];
            }
        }
        return null;
    }
}

class No {
    public Restaurante elemento;
    public No dir, esq;
    public int altura;

    public No(Restaurante x) {
        this.elemento = x;
        this.dir = this.esq = null;
        this.altura = 1;
    }
}

class ArvoreAVL {
    public No raiz;
    public long comparacoes;

    public ArvoreAVL() {
        this.raiz = null;
        this.comparacoes = 0;
    }

    private int getAltura(No no) {
        if (no == null) {
            return 0;
        } else {
            return no.altura;
        }
    }

    private void atualizarAltura(No no) {
        int altEsq = getAltura(no.esq);
        int altDir = getAltura(no.dir);
        if (altEsq > altDir) {
            no.altura = 1 + altEsq;
        } else {
            no.altura = 1 + altDir;
        }
    }

    private int fatorBalanceamento(No no) {
        if (no == null) return 0;
        return getAltura(no.esq) - getAltura(no.dir);
    }

    private No rotacaoDir(No y) {
        No x = y.esq;
        No T2 = x.dir;
        x.dir = y;
        y.esq = T2;
        atualizarAltura(y);
        atualizarAltura(x);
        return x;
    }

    private No rotacaoEsq(No x) {
        No y = x.dir;
        No T2 = y.esq;
        y.esq = x;
        x.dir = T2;
        atualizarAltura(x);
        atualizarAltura(y);
        return y;
    }

    public void inserir(Restaurante x) {
        raiz = inserir(x, raiz);
    }

    private No inserir(Restaurante x, No i) {
        if (i == null) return new No(x);

        if (i.elemento.getNome().compareTo(x.getNome()) > 0) {
            i.esq = inserir(x, i.esq);
        } else if (i.elemento.getNome().compareTo(x.getNome()) < 0) {
            i.dir = inserir(x, i.dir);
        } else {
            return i;
        }

        atualizarAltura(i);
        int fb = fatorBalanceamento(i);

        // logica do balanceament vqi que da
        if (fb > 1 && x.getNome().compareTo(i.esq.elemento.getNome()) < 0) {
            return rotacaoDir(i);
        }
        if (fb < -1 && x.getNome().compareTo(i.dir.elemento.getNome()) > 0) {
            return rotacaoEsq(i);
        }
        if (fb > 1 && x.getNome().compareTo(i.esq.elemento.getNome()) > 0) {
            i.esq = rotacaoEsq(i.esq);
            return rotacaoDir(i);
        }
        if (fb < -1 && x.getNome().compareTo(i.dir.elemento.getNome()) < 0) {
            i.dir = rotacaoDir(i.dir);
            return rotacaoEsq(i);
        }
        return i;
    }

    public boolean pesquisar(String x) {
        System.out.print("raiz ");
        return pesquisar(x, raiz);
    }

    private boolean pesquisar(String x, No i) {
        if (i == null) {
            return false;
        } else {
            comparacoes++;
            if (i.elemento.getNome().compareTo(x) == 0) {
                return true;
            } else if (i.elemento.getNome().compareTo(x) > 0) {
                System.out.print("esq ");
                return pesquisar(x, i.esq);
            } else {
                System.out.print("dir ");
                return pesquisar(x, i.dir);
            }
        }
    }

    public void caminhaCentral(No i) {
        if (i != null) {
            caminhaCentral(i.esq);
            System.out.println(i.elemento.formatar());
            caminhaCentral(i.dir);
        }
    }
}

public class Main {
    static final String MATRICULA = "890309";

    public static void main(String[] args) throws Exception {
        Scanner scan = new Scanner(System.in);
        ColecaoRestaurantes cr = ColecaoRestaurantes.lerCsv();

        String linha = scan.next();
        ArvoreAVL arvore = new ArvoreAVL();

        // loop to iti
        while (linha.compareTo("-1") != 0) {
            int id = Integer.parseInt(linha);
            Restaurante r = cr.buscarPorId(id);
            if (r != null) {
                arvore.inserir(r);
            }
            linha = scan.next();
        }

        scan.nextLine();
        linha = scan.nextLine();

        long inicio = System.nanoTime();

        while (linha.compareTo("FIM") != 0) {
            if (arvore.pesquisar(linha)) {
                System.out.println("SIM");
            } else {
                System.out.println("NAO");
            }
            linha = scan.nextLine();
        }

        long fim = System.nanoTime();
        double tempoMs = (fim - inicio) / 1000000.0;

        arvore.caminhaCentral(arvore.raiz);
        scan.close();

        FileWriter arq = new FileWriter(MATRICULA + "_arvore_avl.txt");
        PrintWriter gravarArq = new PrintWriter(arq);
        gravarArq.printf("%s\t%d\t%.2f\n", MATRICULA, arvore.comparacoes, tempoMs);
        gravarArq.close();
    }
}