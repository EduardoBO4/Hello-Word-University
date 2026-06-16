public class tp1 {// ja feito dia 16/03

    // método que cifra a string
    public static String cifrar(String s) {
        String resp = "";

        // percorre a string inteira
        for (int i = 0; i < s.length(); i++) {
            // pega caractere na posição i
            char c = s.charAt(i);
            
            // desloca 3 posições na tabela ASCII
            char novo = (char) (c + 3);
            
            // concatena na variável de resposta
            resp = resp + novo;
        }

        return resp;
    }

    public static void main(String[] args) {
        // leitura da primeira linha
        String entrada = MyIO.readLine();

        // loop de parada se for FIM
        while (!entrada.equals("FIM")) {
            
            // chama a função e imprime
            MyIO.println(cifrar(entrada));

            // lê próxima linha
            entrada = MyIO.readLine();
        }
    }
}
