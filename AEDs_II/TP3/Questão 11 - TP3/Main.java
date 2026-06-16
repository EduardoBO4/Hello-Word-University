import java.io.*;
import java.util.*;

class Hora {
    private int hr;
    private int min;

    public Hora(int hr, int min) {
        this.hr = hr;
        this.min = min;
    }

    public int getHr() { return hr; }
    public int getMin() { return min; }
    public void setHr(int hr) { this.hr = hr; }
    public void setMin(int min) { this.min = min; }

    public static Hora analisarHora(String str) {
        Scanner leitor = new Scanner(str);
        leitor.useDelimiter(":");
        int h = leitor.nextInt();
        int m = leitor.nextInt();
        Hora novaHora = new Hora(h, m);
        return novaHora;
    }

    public String formatar() {
        return String.format("%02d:%02d", this.hr, this.min);
    }
}   

class Data {
    private int anoRef;
    private int mesRef;
    private int diaRef;

    public Data(int anoRef, int mesRef, int diaRef) {
        this.anoRef = anoRef;
        this.mesRef = mesRef;
        this.diaRef = diaRef;
    }

    public int getAnoRef() { return anoRef; }
    public int getMesRef() { return mesRef; }
    public int getDiaRef() { return diaRef; }
    public void setAnoRef(int anoRef) { this.anoRef = anoRef; }
    public void setMesRef(int mesRef) { this.mesRef = mesRef; }
    public void setDiaRef(int diaRef) { this.diaRef = diaRef; }

    public static Data analisarData(String str) { 
        Scanner leitor = new Scanner(str); 
        leitor.useDelimiter("-");
        int a = leitor.nextInt();
        int m = leitor.nextInt();
        int d = leitor.nextInt();
        leitor.close();
        return new Data(a, m, d);
    }
   
    public String formatar() {
        return String.format("%02d/%02d/%04d", this.diaRef, this.mesRef, this.anoRef);
    }
}
    
class Restaurante {
    private int codigoId;
    private String titulo;
    private String municipio;
    private int lotacao;
    private double nota;
    private String[] categorias;
    private int precoF;
    private Hora abrir;
    private Hora fechar;
    private Data inauguracao;
    private boolean funcionamento;

    public Restaurante(int codigoId, String titulo, String municipio, int lotacao, double nota,
            String[] categorias, int precoF, Hora abrir, Hora fechar, Data inauguracao, boolean funcionamento) {
        this.codigoId = codigoId;
        this.titulo = titulo;
        this.municipio = municipio;
        this.lotacao = lotacao;
        this.nota = nota;
        this.categorias = categorias;
        this.precoF = precoF;
        this.abrir = abrir;
        this.fechar = fechar;
        this.inauguracao = inauguracao;
        this.funcionamento = funcionamento;
    }

    public int getCodigoId() { return codigoId; }
    public void setCodigoId(int codigoId) { this.codigoId = codigoId; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }
    public int getLotacao() { return lotacao; }
    public void setLotacao(int lotacao) { this.lotacao = lotacao; }
    public double getNota() { return nota; }
    public void setNota(double nota) { this.nota = nota; }
    public String[] getCategorias() { return categorias; }
    public void setCategorias(String[] categorias) { this.categorias = categorias; }
    public int getPrecoF() { return precoF; }
    public void setPrecoF(int precoF) { this.precoF = precoF; }
    public Hora getAbrir() { return abrir; }
    public void setAbrir(Hora abrir) { this.abrir = abrir; }
    public Hora getFechar() { return fechar; }
    public void setFechar(Hora fechar) { this.fechar = fechar; }
    public Data getInauguracao() { return inauguracao; }
    public void setInauguracao(Data inauguracao) { this.inauguracao = inauguracao; }
    public boolean isFuncionamento() { return funcionamento; }
    public void setFuncionamento(boolean funcionamento) { this.funcionamento = funcionamento; }

    public static int contarCifroes(String str) {
        int c = 0;    
        for(int idx = 0; idx < str.length(); idx++)
            if(str.charAt(idx) == '$') c++;
        return c;
    }

    public static Restaurante converterStringParaRestaurante(String str) {
        Scanner leitorStr = new Scanner(str);
        leitorStr.useLocale(Locale.US);
        leitorStr.useDelimiter(",");
        int idLocal = leitorStr.nextInt();
        String nm = leitorStr.next();
        String cd = leitorStr.next();
        int cap = leitorStr.nextInt();
        double aval = leitorStr.nextDouble();
        String tipos = leitorStr.next();  
        int cifroes = contarCifroes(leitorStr.next());
        String horariosStr = leitorStr.next();
        
        Scanner leitorH = new Scanner(horariosStr);
        leitorH.useDelimiter("-");
        Hora hA = Hora.analisarHora(leitorH.next());
        Hora hF = Hora.analisarHora(leitorH.next());
        leitorH.close();
        
        Data dtAbertura = Data.analisarData(leitorStr.next());
        String condicao = leitorStr.next();
        boolean estadoAberto = condicao.equals("true");

        leitorStr.close();

        String[] temporario = new String[10];
        int qtdCozinhas = 0;
        Scanner leitorTipos = new Scanner(tipos);
        leitorTipos.useDelimiter(";");
        
        while(leitorTipos.hasNext()) {
            String p = leitorTipos.next();
            if(p.length() > 0) {
                temporario[qtdCozinhas] = p;
                qtdCozinhas++;
            }
        }
        leitorTipos.close();
    
        String[] vetorCozinhas = new String[qtdCozinhas];
        for(int k = 0; k < qtdCozinhas; k++) {
            vetorCozinhas[k] = temporario[k];
        }
        
        return new Restaurante(idLocal, nm, cd, cap, aval, vetorCozinhas, 
                               cifroes, hA, hF, dtAbertura, estadoAberto);
    }

    public String formatar() {
        String catsFormatadas = "";
        for(int k = 0; k < categorias.length; k++) {
            catsFormatadas += categorias[k];
            if(k < categorias.length - 1) {
                catsFormatadas += ","; 
            }
        }
        
        String prc = "";
        for(int k = 0; k < this.precoF; k++) {
            prc += '$';
        }
        
        String nStr = this.nota + "";

        return String.format("[%d ## %s ## %s ## %d ## %s ## [%s] ## %s ## %s-%s ## %s ## %b]", 
                codigoId, titulo, municipio, lotacao, nStr, catsFormatadas, 
                prc, abrir.formatar(), fechar.formatar(), 
                inauguracao.formatar(), funcionamento);
    }
}

class AcervoRestaurantes {
    private int tam;
    private Restaurante[] vetorRestaurantes;

    public AcervoRestaurantes() {
        this.tam = 0;
        this.vetorRestaurantes = null;
    }

    public AcervoRestaurantes(int tam) {
        this.tam = tam;
        this.vetorRestaurantes = new Restaurante[tam];
    }

    public void processarCsv(String caminho) throws Exception {
        File arqCsv = new File(caminho);
        Scanner leitorArq = new Scanner(arqCsv);
        
        if(leitorArq.hasNextLine())
            leitorArq.nextLine();

        int idx = 0;
        while(leitorArq.hasNextLine()) {
           String linhaLida = leitorArq.nextLine();
           vetorRestaurantes[idx] = Restaurante.converterStringParaRestaurante(linhaLida);
           idx++;
        }
        leitorArq.close();
    }

    public static AcervoRestaurantes carregarBase() throws Exception {
        File f = new File("/tmp/restaurantes.csv");
        Scanner s = new Scanner(f);

        int contLinhas = 0;
        while(s.hasNext()) {
            s.nextLine();
            contLinhas++;
        }

        s.close();
        AcervoRestaurantes novoAcervo = new AcervoRestaurantes(contLinhas - 1);
        novoAcervo.processarCsv("/tmp/restaurantes.csv");
        
        return novoAcervo;
    }

    public Restaurante encontrarPorId(int idBusca) {
         for(int k = 0; k < tam; k++) {
            if(vetorRestaurantes[k].getCodigoId() == idBusca) {
                return vetorRestaurantes[k];
            }
        }
        return null;
     }
}

class ElementoDuplo {
    public Restaurante dados;
    public ElementoDuplo proximo, anterior;

    public ElementoDuplo() {
        this.dados = null;
        this.proximo = this.anterior = null;
    }

    public ElementoDuplo(Restaurante dados) {
        this.dados = dados;
        this.proximo = null;
        this.anterior = null;
    }
}

class ListaDuplamenteEncadeada {
    public ElementoDuplo cabeca, cauda;

    public ListaDuplamenteEncadeada() {
        cabeca = new ElementoDuplo();
        cauda = cabeca;        
    }

    public ElementoDuplo buscarPosicao(int indice) {
        ElementoDuplo r = cabeca.proximo;
        for(int k = 0; k < indice; k++)
            r = r.proximo;
        return r;
    }
 
    public int calcularTamanho() {
        int contador = 0;
        for(ElementoDuplo p = cabeca; p != cauda; p = p.proximo)
            contador++;
        return contador;
    }
 
    public void adicionarNoFim(Restaurante item) {
        cauda.proximo = new ElementoDuplo(item);
        cauda.proximo.anterior = cauda;
        cauda = cauda.proximo;
    }
 
    public void exibirElementos() {
        for(ElementoDuplo p = cabeca.proximo; p != null; p = p.proximo) {
            System.out.println(p.dados.formatar());
        }
    }
}

public class Main {
    public static int transicoes = 0;
    public static int afericoes = 0;
    
    public static void trocarNodos(ListaDuplamenteEncadeada lista, int pos1, int pos2) {
        ElementoDuplo nodo1 = lista.buscarPosicao(pos1);
        ElementoDuplo nodo2 = lista.buscarPosicao(pos2);
        Restaurante temp = nodo1.dados;
        nodo1.dados = nodo2.dados;
        nodo2.dados = temp;
        transicoes += 3;
    }
 
    public static void algoritmoQuicksort(ListaDuplamenteEncadeada colecao, int limitEsq, int limitDir) {
        int indI = limitEsq, indJ = limitDir;
        int centro = (limitEsq + limitDir) / 2;

        double notaPivo = colecao.buscarPosicao(centro).dados.getNota();
        String nomePivo = colecao.buscarPosicao(centro).dados.getTitulo();
 
        while (indI <= indJ) {
            afericoes++;
            while (colecao.buscarPosicao(indI).dados.getNota() < notaPivo ||
                  (colecao.buscarPosicao(indI).dados.getNota() == notaPivo &&
                   colecao.buscarPosicao(indI).dados.getTitulo().compareTo(nomePivo) < 0)) {
                afericoes++;
                indI++;
            }
            afericoes++;
            while (colecao.buscarPosicao(indJ).dados.getNota() > notaPivo ||
                  (colecao.buscarPosicao(indJ).dados.getNota() == notaPivo &&
                   colecao.buscarPosicao(indJ).dados.getTitulo().compareTo(nomePivo) > 0)) {
                afericoes++;
                indJ--;
            }

            if (indI <= indJ) {
                trocarNodos(colecao, indI, indJ);
                indI++;
                indJ--;
            }
        }
 
        if (limitEsq < indJ) algoritmoQuicksort(colecao, limitEsq, indJ);
        if (indI < limitDir) algoritmoQuicksort(colecao, indI, limitDir);
    }   

    public static void processarOrdenacao(ListaDuplamenteEncadeada lista, int qtdElementos) {
        algoritmoQuicksort(lista, 0, qtdElementos - 1);
    }
    
    public static void main(String[] args) throws Exception {
        Scanner entrada = new Scanner(System.in);
        AcervoRestaurantes acervo = AcervoRestaurantes.carregarBase();
        String valorLido = entrada.next();
        
        double tempoStart, tempoEnd, duracaoFinal;
        int totalEncontrado = 0;
        ListaDuplamenteEncadeada listaFlex = new ListaDuplamenteEncadeada();
        
        while(!valorLido.equals("-1")) {
           int matriculaId = Integer.parseInt(valorLido);
           Restaurante resAchado = acervo.encontrarPorId(matriculaId);
           if(resAchado != null) {
              listaFlex.adicionarNoFim(resAchado);
              totalEncontrado++;
           }
           valorLido = entrada.next();
        }
         
        entrada.close();

        tempoStart = System.nanoTime();
        processarOrdenacao(listaFlex, totalEncontrado);
        tempoEnd = System.nanoTime();

        duracaoFinal = (tempoEnd - tempoStart) / 1_000_000.0;

        FileWriter docLog = new FileWriter("890309_quicksort.txt");
        PrintWriter escritorLog = new PrintWriter(docLog);

        escritorLog.printf("890309\t Comparacoes: %d\t Movimentacao: %d\t Tempo: %.4f\n", afericoes, transicoes, duracaoFinal);

        escritorLog.close();
        listaFlex.exibirElementos();
    }
}