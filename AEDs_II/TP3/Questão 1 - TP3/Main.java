import java.util.*;
import java.io.*;



class Data {
    private int dia, mes, ano;
 
    public Data() { this.dia = 0; this.mes = 0; this.ano = 0; }
 
    public Data(int dia, int mes, int ano) {
        this.dia = dia; this.mes = mes; this.ano = ano;
    }
 
    public int getDia() { return dia; }
    public int getMes() { return mes; }
    public int getAno() { return ano; }
    public void setDia(int d) { this.dia = d; }
    public void setMes(int m) { this.mes = m; }
    public void setAno(int a) { this.ano = a; }
 
    public static Data parseData(String entrada) {
        Scanner s = new Scanner(entrada);
        s.useDelimiter("-");
        int ano = s.nextInt(), mes = s.nextInt(), dia = s.nextInt();
        s.close();
        return new Data(dia, mes, ano);
    }
 
    public String formatar() {
        return String.format("%02d/%02d/%04d", dia, mes, ano);
    }
}


class Hora {
    private int horas, minutos;
 
    public Hora() { this.horas = 0; this.minutos = 0; }
 
    public Hora(int horas, int minutos) {
        this.horas = horas; this.minutos = minutos;
    }
 
    public int getHoras()   { return horas; }
    public int getMinutos() { return minutos; }
    public void setHoras(int h)   { this.horas = h; }
    public void setMinutos(int m) { this.minutos = m; }
 
    public static Hora parseHora(String entrada) {
        Scanner s = new Scanner(entrada);
        s.useDelimiter(":");
        int h = s.nextInt(), m = s.nextInt();
        s.close();
        return new Hora(h, m);
    }
 
    public String formatar() {
        return String.format("%02d:%02d", horas, minutos);
    }
}
 
class Restaurante {
    private int id;
    private String nome, cidade;
    private int capacidade;
    private double avaliacao;
    private String[] tiposCozinha;
    private int faixaPreco;
    private Hora horarioAbertura, horarioFechamento;
    private Data dataAbertura;
    private boolean aberto;
 
    public Restaurante() {
        this.id = 0; this.nome = ""; this.cidade = "";
        this.capacidade = 0; this.avaliacao = 0.0;
        this.tiposCozinha = new String[0]; this.faixaPreco = 0;
        this.horarioAbertura = new Hora(); this.horarioFechamento = new Hora();
        this.dataAbertura = new Data(); this.aberto = false;
    }
 
    public Restaurante(int id, String nome, String cidade, int capacidade,
                       double avaliacao, String[] tiposCozinha, int faixaPreco,
                       Hora horarioAbertura, Hora horarioFechamento,
                       Data dataAbertura, boolean aberto) {
        this.id = id; this.nome = nome; this.cidade = cidade;
        this.capacidade = capacidade; this.avaliacao = avaliacao;
        this.tiposCozinha = tiposCozinha; this.faixaPreco = faixaPreco;
        this.horarioAbertura = horarioAbertura;
        this.horarioFechamento = horarioFechamento;
        this.dataAbertura = dataAbertura; this.aberto = aberto;
    }
 
    public int getId()        { return id; }
    public String getNome()   { return nome; }
    public String getCidade() { return cidade; }
 
    public static Restaurante parseRestaurante(String s) {
        s = s.replace("\r", "");
        Scanner leitor = new Scanner(s);
        leitor.useLocale(Locale.US);
        leitor.useDelimiter(",");
 
        int id           = leitor.nextInt();
        String nome      = leitor.next();
        String cidade    = leitor.next();
        int capacidade   = leitor.nextInt();
        double avaliacao = leitor.nextDouble();
 
        String tipoCozinhaRaw = leitor.next();
        Scanner sc1 = new Scanner(tipoCozinhaRaw); sc1.useDelimiter(";");
        int qtd = 0;
        while (sc1.hasNext()) { sc1.next(); qtd++; }
        sc1.close();
        String[] tiposCozinha = new String[qtd];
        Scanner sc2 = new Scanner(tipoCozinhaRaw); sc2.useDelimiter(";");
        for (int i = 0; i < qtd; i++) tiposCozinha[i] = sc2.next();
        sc2.close();
 
        String faixaPrecoRaw = leitor.next();
        int faixaPreco = faixaPrecoRaw.length();
 
        String horarioRaw = leitor.next();
        Scanner sh = new Scanner(horarioRaw); sh.useDelimiter("-");
        Hora horarioAbertura   = Hora.parseHora(sh.next());
        Hora horarioFechamento = Hora.parseHora(sh.next());
        sh.close();
 
        Data dataAbertura = Data.parseData(leitor.next());
        boolean aberto    = Boolean.parseBoolean(leitor.next().trim());
        leitor.close();
 
        return new Restaurante(id, nome, cidade, capacidade, avaliacao,
                tiposCozinha, faixaPreco, horarioAbertura, horarioFechamento,
                dataAbertura, aberto);
    }
 
    public String formatar() {
        StringBuilder tipos = new StringBuilder();
        for (int i = 0; i < tiposCozinha.length; i++) {
            if (i > 0) tipos.append(",");
            tipos.append(tiposCozinha[i]);
        }
        StringBuilder faixaStr = new StringBuilder();
        for (int i = 0; i < faixaPreco; i++) faixaStr.append("$");
 
        return String.format("[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %b]",
                id, nome, cidade, capacidade, avaliacao,
                tipos, faixaStr,
                horarioAbertura.formatar(), horarioFechamento.formatar(),
                dataAbertura.formatar(), aberto);
    }
}
 
class ColecaoRestaurantes {
    private int tamanho;
    private Restaurante[] restaurantes;
 
    public ColecaoRestaurantes() {
        this.tamanho = 0;
        this.restaurantes = new Restaurante[1000];
    }
 
    public int getTamanho()              { return tamanho; }
    public Restaurante[] getRestaurantes() { return restaurantes; }
 
    public void lerCsv(String path) {
        try {
            Scanner leitor = new Scanner(new File(path));
            if (leitor.hasNextLine()) leitor.nextLine(); // skip header
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (!linha.trim().isEmpty()) {
                    restaurantes[tamanho++] = Restaurante.parseRestaurante(linha);
                }
            }
            leitor.close();
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo nao encontrado: " + path);
        }
    }
 
    public static ColecaoRestaurantes lerCsv() {
        ColecaoRestaurantes c = new ColecaoRestaurantes();
        c.lerCsv("/tmp/restaurantes.csv");
        return c;
    }
}
 
// ===== Ordenação Parcial por Seleção =====
class OrdenacaoParcialSelecao {
 
    private long comparacoes  = 0;
    private long movimentacoes = 0;
 
    public long getComparacoes()   { return comparacoes; }
    public long getMovimentacoes() { return movimentacoes; }
 

    public void ordenarParcial(Restaurante[] v, int n, int k) {
        comparacoes   = 0;
        movimentacoes = 0;
        int limite = Math.min(k, n);
 
        for (int i = 0; i < limite; i++) {
            int menorIdx = i;
 
           

            for (int j = i + 1; j < n; j++) {
                comparacoes++;
                if (v[j].getNome().compareToIgnoreCase(v[menorIdx].getNome()) < 0) {
                    menorIdx = j;
                }
            }
            
            if (menorIdx != i) {
                Restaurante tmp = v[i];
                v[i]       = v[menorIdx];
                v[menorIdx] = tmp;
                movimentacoes += 3; 
            }
        }
    }
}
 
public class Main {
 
    private static final String MATRICULA = "890309"; 
    private static final int K = 10; 
 
    public static void main(String[] args) {
 
        
        ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
        Restaurante[] todos  = colecao.getRestaurantes();
        int total            = colecao.getTamanho();
 
        Scanner leitorStdin = new Scanner(System.in);
        Restaurante[] vetor = new Restaurante[total];
        int n = 0;
 
        while (leitorStdin.hasNextLine()) {
            String linha = leitorStdin.nextLine().trim();
            if (linha.equals("-1")) break;
            if (linha.isEmpty()) continue;
 
            int idBuscado = Integer.parseInt(linha);
            for (int i = 0; i < total; i++) {
                if (todos[i].getId() == idBuscado) {
                    vetor[n++] = todos[i];
                    break;
                }
            }
        }
        leitorStdin.close();

        OrdenacaoParcialSelecao sorter = new OrdenacaoParcialSelecao();
 
        long inicio = System.nanoTime();
        sorter.ordenarParcial(vetor, n, K);
        long fim    = System.nanoTime();
 
        double tempoMs = (fim - inicio) / 1_000_000.0;
 
        for (int i = 0; i < n; i++) {
            System.out.println(vetor[i].formatar());
         }
 
        String nomeLog = MATRICULA + "_selecao_parcial.txt";
        try {
            PrintWriter pw = new PrintWriter(new FileWriter(nomeLog));
            pw.printf("%s\t%d\t%d\t%.2f%n",
                    MATRICULA,
                    sorter.getComparacoes(),
                    sorter.getMovimentacoes(),
                    tempoMs);
            pw.close();
        } catch (IOException e) {
            System.err.println("Erro ao gravar log: " + e.getMessage());
        }
    }
}