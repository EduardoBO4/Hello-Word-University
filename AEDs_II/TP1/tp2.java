import java.util.Random;

public class tp2 {
	
    	// método que faz a substituição
    	public static String realizarSubstituicao(String s, char letraAnt, char letraNova) {
        	// converte para array para poder alterar os índices 
        	char[] caracteres = s.toCharArray();

        	for (int i = 0; i < caracteres.length; i++) {
            	if (caracteres[i] == letraAnt) {
                	caracteres[i] = letraNova;
            	}
        	}

        	// tacar array de volta em String
        	return new String(caracteres);
    	}

    	public static void main(String[] args) {
        	// fora do while para manter a sequência
        	Random gerador = new Random();
        	gerador.setSeed(4);

        	// leitura da primeira linha
        	String entrada = MyIO.readLine();

        	// loop até encontrar "FIM"
        	while (!entrada.equals("FIM")) {
            
            	// sorteio das duas letras segundo a atividade do github mostrou
            	char c1 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));
            	char c2 = (char) ('a' + (Math.abs(gerador.nextInt()) % 26));

            	// guarda o resultado 
            	String resultado = realizarSubstituicao(entrada, c1, c2);

       	
            	MyIO.println(resultado);

            	// lê a próxima linha para continuar ou encerrar o loop
            	entrada = MyIO.readLine();
        	}
    	}
}
