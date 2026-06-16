import java.util.*;
import java.io.*;

class Data{
		private int dia;
		private int mes;
		private int ano;

	public Data(){
		this.dia= 0;
		this.mes= 0;
		this.ano= 0;
	}

	public Data (int dia , int mes, int ano){
		this.dia= dia;
        	this.mes= mes;
        	this.ano= ano;
	}

	public int getDia(){
		return dia;
	}
	public int getMes(){
        	return mes;
	}
	public int getAno(){
        	return ano;
	}

	public void  setDia(int dia){
		this.dia = dia;
	}
	public void  setMes(int mes){
        	this.mes = mes;
	}
	public void  setAno(int ano){
        	this.ano = ano;
	}



	public static Data parseData(String entrada){
		Scanner leitor = new Scanner(entrada);
		leitor.useDelimiter("-");

		int ano = leitor.nextInt();
    		int mes = leitor.nextInt();
    		int dia = leitor.nextInt();
		leitor.close();
		return new Data(dia, mes, ano);
	}



	public String formatar(){

   	   return String.format("%02d/%02d/%04d", this.dia, this.mes, this.ano);
	}
}

class Hora{
                private int horas;
                private int minutos;

        public Hora(){
                this.horas= 0;
                this.minutos= 0;
        }

        public Hora (int horas , int minutos){
                this.horas= horas;
                this.minutos= minutos;
        }

        public int getHoras(){
                return horas;
        }
        public int getMinutos(){
                return minutos;
        }

        public void  setHoras(int horas){
                this.horas = horas;
        }
        public void  setMinutos(int minutos){
                this.minutos = minutos;
        }


	public static Hora parseHora(String entrada){
        	Scanner leitor = new Scanner(entrada);

        	leitor.useDelimiter(":");

        	int horas = leitor.nextInt();
        	int minutos = leitor.nextInt();
        	leitor.close();
        	return new Hora(horas, minutos);

	}

	public String formatar(){
        	return String.format("%02d:%02d", this.horas, this.minutos);
	}
}

class Restaurante{
	private int id;
	private String nome;
	private String cidade;
	private int capacidade;
	private double avaliacao;
       	private String[] tiposCozinha;
	private int faixaPreco;
	private Hora horarioAbertura;
	private Hora horarioFechamento;
	private Data dataAbertura;
	private boolean aberto;


	public Restaurante(int id, String nome, String cidade, int capacidade, double avaliacao, String[] tiposCozinha, int faixaPreco, Hora horarioAbertura, Hora horarioFechamento, Data dataAbertura, boolean aberto){
		this.id = id;
		this.nome = nome;
		this.cidade = cidade;
		this.capacidade = capacidade;
		this.avaliacao = avaliacao;
		this.tiposCozinha = tiposCozinha;
       		this.faixaPreco = faixaPreco;
		this.horarioAbertura = horarioAbertura;
		this.horarioFechamento = horarioFechamento;
		this.dataAbertura= dataAbertura;
		this.aberto = aberto;
	}

	public Restaurante(){
        	this.id = 0;
        	this.nome = "";
        	this.cidade = "";
        	this.capacidade = 0;
        	this.avaliacao = 0.0;
        	this.tiposCozinha = new String[0];
        	this.faixaPreco = 0;
        	this.horarioAbertura = new Hora();
        	this.horarioFechamento = new Hora();
        	this.dataAbertura= new Data();
        	this.aberto = false;
        }

	public int getId() { return id; }
	public String getNome() { return nome; }
	public String getCidade() { return cidade; }
	public int getCapacidade() { return capacidade; }
	public double getAvaliacao() { return avaliacao; }
	public String[] getTiposCozinha() { return tiposCozinha; }
	public int getFaixaPreco() { return faixaPreco; }
	public Hora getHorarioAbertura() { return horarioAbertura; }
	public Hora getHorarioFechamento() { return horarioFechamento; }
	public Data getDataAbertura() { return dataAbertura; }
	public boolean isAberto() { return aberto; }

	public void setId(int id) { this.id = id; }
	public void setNome(String nome) { this.nome = nome; }
	public void setCidade(String cidade) { this.cidade = cidade; }
	public void setCapacidade(int capacidade) { this.capacidade = capacidade; }
	public void setAvaliacao(double avaliacao) { this.avaliacao = avaliacao; }
	public void setTiposCozinha(String[] tiposCozinha) { this.tiposCozinha = tiposCozinha; }
	public void setFaixaPreco(int faixaPreco) { this.faixaPreco = faixaPreco; }
	public void setHorarioAbertura(Hora horarioAbertura) { this.horarioAbertura = horarioAbertura; }
	public void setHorarioFechamento(Hora horarioFechamento) { this.horarioFechamento = horarioFechamento; }
	public void setDataAbertura(Data dataAbertura) { this.dataAbertura = dataAbertura; }
	public void setAberto(boolean aberto) { this.aberto = aberto; }

	public static Restaurante parseRestaurante(String s){

		s = s.replace("\r", "");

		Scanner leitor = new Scanner(s);
		leitor.useLocale(Locale.US);
		leitor.useDelimiter(",");

		int id             = leitor.nextInt();
		String nome        = leitor.next();
		String cidade      = leitor.next();
		int capacidade     = leitor.nextInt();
		double avaliacao   = leitor.nextDouble();


		String tipoCozinhaRaw = leitor.next();
		Scanner leitorTipos = new Scanner(tipoCozinhaRaw);
		leitorTipos.useDelimiter(";");
		int qtdTipos = 0;

		while(leitorTipos.hasNext()){
			leitorTipos.next();
			qtdTipos++;
		}
		leitorTipos.close();
		String[] tiposCozinha = new String[qtdTipos];

		Scanner leitorTipos2 = new Scanner(tipoCozinhaRaw);
		leitorTipos2.useDelimiter(";");
		for(int i = 0; i < qtdTipos; i++){
			tiposCozinha[i] = leitorTipos2.next();
		}
		leitorTipos2.close();



		String faixaPrecoRaw = leitor.next();
		int faixaPreco = faixaPrecoRaw.length();


		String horarioRaw = leitor.next();
		Scanner leitorHorario = new Scanner(horarioRaw);
		leitorHorario.useDelimiter("-");
		Hora horarioAbertura   = Hora.parseHora(leitorHorario.next());
		Hora horarioFechamento = Hora.parseHora(leitorHorario.next());
		leitorHorario.close();

		String dataRaw    = leitor.next();
		Data dataAbertura = Data.parseData(dataRaw);


		boolean aberto = Boolean.parseBoolean(leitor.next().trim());

		leitor.close();


		return new Restaurante(
			id, nome, cidade, capacidade, avaliacao, tiposCozinha, faixaPreco,
			horarioAbertura, horarioFechamento, dataAbertura, aberto);
	}


	public String formatar(){

		StringBuilder tipos = new StringBuilder();
		for(int i = 0; i < tiposCozinha.length; i++){
			if(i > 0) tipos.append(",");
			tipos.append(tiposCozinha[i]);
		}


		StringBuilder faixaStr = new StringBuilder();
		for(int i = 0; i < this.faixaPreco; i++){
			faixaStr.append("$");
		}


		return String.format("[%d ## %s ## %s ## %d ## %.1f ## [%s] ## %s ## %s-%s ## %s ## %b]",
			this.id,
			this.nome,
			this.cidade,
			this.capacidade,
			this.avaliacao,
			tipos.toString(),
			faixaStr.toString(),
			this.horarioAbertura.formatar(),
			this.horarioFechamento.formatar(),
			this.dataAbertura.formatar(),
			this.aberto
		);
	}

}

class ColecaoRestaurantes {
	private int tamanho;
	private Restaurante[] restaurantes;

	public ColecaoRestaurantes() {
		this.tamanho = 0;
		this.restaurantes = new Restaurante[1000];
	}

	public int getTamanho() {
		return tamanho;
	}

	public Restaurante[] getRestaurantes() {
		return restaurantes;
	}


	public void lerCsv(String path) {
		try {
			Scanner leitor = new Scanner(new File(path));

			if (leitor.hasNextLine()) {
				leitor.nextLine();
			}

			while (leitor.hasNextLine()) {
				String linha = leitor.nextLine();
				if (!linha.trim().isEmpty()) {
					restaurantes[tamanho] = Restaurante.parseRestaurante(linha);
					tamanho++;
				}
			}
			leitor.close();
		} catch (FileNotFoundException e) {
			System.err.println("Arquivo nao encontrado: " + path);
		}
	}


	public static ColecaoRestaurantes lerCsv() {
		ColecaoRestaurantes colecao = new ColecaoRestaurantes();
		colecao.lerCsv("/tmp/restaurantes.csv");
		return colecao;
	}
}


class Lista {
	private Restaurante[] array;
	private int n;

	public Lista() {
		this(1000);
	}

	public Lista(int tamanho) {
		this.array = new Restaurante[tamanho];
		this.n = 0;
	}

	public int getTamanho() {
		return n;
	}

	public void inserirInicio(Restaurante r) throws Exception {
		if (n >= array.length) {
			throw new Exception("Erro ao inserir (lista cheia)!");
		}
		for (int i = n; i > 0; i--) {
			array[i] = array[i - 1];
		}
		array[0] = r;
		n++;
	}

	public void inserir(Restaurante r, int posicao) throws Exception {
		if (n >= array.length || posicao < 0 || posicao > n) {
			throw new Exception("Erro ao inserir (posicao invalida)!");
		}
		for (int i = n; i > posicao; i--) {
			array[i] = array[i - 1];
		}
		array[posicao] = r;
		n++;
	}

	public void inserirFim(Restaurante r) throws Exception {
		if (n >= array.length) {
			throw new Exception("Erro ao inserir (lista cheia)!");
		}
		array[n] = r;
		n++;
	}

	public Restaurante removerInicio() throws Exception {
		if (n == 0) {
			throw new Exception("Erro ao remover (lista vazia)!");
		}
		Restaurante removido = array[0];
		n--;
		for (int i = 0; i < n; i++) {
			array[i] = array[i + 1];
		}
		return removido;
	}

	public Restaurante remover(int posicao) throws Exception {
		if (n == 0 || posicao < 0 || posicao >= n) {
			throw new Exception("Erro ao remover (posicao invalida)!");
		}
		Restaurante removido = array[posicao];
		n--;
		for (int i = posicao; i < n; i++) {
			array[i] = array[i + 1];
		}
		return removido;
	}

	public Restaurante removerFim() throws Exception {
		if (n == 0) {
			throw new Exception("Erro ao remover (lista vazia)!");
		}
		n--;
		return array[n];
	}

	public void mostrar() {
		for (int i = 0; i < n; i++) {
			System.out.println(array[i].formatar());
		}
	}
}


public class Main {

	public static Restaurante buscarPorId(Restaurante[] restaurantes, int total, int id) {
		for (int i = 0; i < total; i++) {
			if (restaurantes[i].getId() == id) {
				return restaurantes[i];
			}
		}
		return null;
	}

	public static void main(String[] args) throws Exception {

		ColecaoRestaurantes colecao = ColecaoRestaurantes.lerCsv();
		Restaurante[] restaurantes  = colecao.getRestaurantes();
		int total                   = colecao.getTamanho();

		Lista lista = new Lista(1000);
		Scanner leitor = new Scanner(System.in);

		while (leitor.hasNextLine()) {
			String linha = leitor.nextLine().trim();
			if (linha.equals("-1")) break;
			if (linha.isEmpty()) continue;

			int id = Integer.parseInt(linha);
			Restaurante r = buscarPorId(restaurantes, total, id);
			if (r != null) {
				lista.inserirFim(r);
			}
		}

		int n = Integer.parseInt(leitor.nextLine().trim());

		for (int k = 0; k < n; k++) {
			String linha = leitor.nextLine().trim();
			Scanner sc = new Scanner(linha);
			String op = sc.next();

			if (op.equals("II")) {
				int id = sc.nextInt();
				Restaurante r = buscarPorId(restaurantes, total, id);
				lista.inserirInicio(r);

			} else if (op.equals("I*")) {
				int pos = sc.nextInt();
				int id  = sc.nextInt();
				Restaurante r = buscarPorId(restaurantes, total, id);
				lista.inserir(r, pos);

			} else if (op.equals("IF")) {
				int id = sc.nextInt();
				Restaurante r = buscarPorId(restaurantes, total, id);
				lista.inserirFim(r);

			} else if (op.equals("RI")) {
				Restaurante removido = lista.removerInicio();
				System.out.println("(R)" + removido.getNome());

			} else if (op.equals("R*")) {
				int pos = sc.nextInt();
				Restaurante removido = lista.remover(pos);
				System.out.println("(R)" + removido.getNome());

			} else if (op.equals("RF")) {
				Restaurante removido = lista.removerFim();
				System.out.println("(R)" + removido.getNome());
			}

			sc.close();
		}

		leitor.close();

		lista.mostrar();
	}
}