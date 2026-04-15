public class tp3 {

    // testa se a string tem apenas vogais
    public static boolean v(String s) {
        boolean ok = true;
        // percorre cada letra da string
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            // se nao for uma das 10 vogais (maiuscula/minuscula), invalida
            if (!(x == 'a' || x == 'e' || x == 'i' || x == 'o' || x == 'u' ||
                  x == 'A' || x == 'E' || x == 'I' || x == 'O' || x == 'U')) {
                ok = false;
                i = s.length(); // forca a parada do loop
            }
        }
        return ok;
    }

    // testa se a string tem apenas consoantes
    public static boolean c(String s) {
        boolean ok = true;
        for (int i = 0; i < s.length(); i++) {
            char x = s.charAt(i);
            // verifica se e numero
            boolean eNumero = (x >= '0' && x <= '9');
            // verifica se e uma vogal
            boolean eVogal = (x == 'a' || x == 'e' || x == 'i' || x == 'o' || x == 'u' ||
                             x == 'A' || x == 'E' || x == 'I' || x == 'O' || x == 'U');
            // verifica se e um simbolo fora do alfabeto
            boolean eSimbolo = (x < 'A' || (x > 'Z' && x < 'a') || x > 'z');

            // se for qualquer uma dessas coisas, nao e consoante
            if (eNumero || eVogal || eSimbolo) {
                ok = false;
                i = s.length(); // sai do loop
            }
        }
        return ok;
    }

    // testa se a string e um numero inteiro
    public static boolean i(String s) {
        boolean ok = true;
        for (int j = 0; j < s.length(); j++) {
            char x = s.charAt(j);
            // se encontrar qualquer coisa que nao seja digito 0-9
            if (!(x >= '0' && x <= '9')) {
                ok = false;
                j = s.length(); // para a execucao
            }
        }
        return ok;
    }

    // testa se a string e um numero real (ponto flutuante)
    public static boolean r(String s) {
        boolean ok = true;
        int cont = 0; // conta quantos pontos ou virgulas existem
        for (int j = 0; j < s.length(); j++) {
            char x = s.charAt(j);
            // se for numero, ignora e segue
            if (x >= '0' && x <= '9') {
                // continua
            } else if (x == ',' || x == '.') {
                // se achar ponto ou virgula, incrementa contador
                cont++;
            } else {
                // se achar letra ou simbolo, invalida
                ok = false;
                j = s.length();
            }
        }
        // numero real so pode ter no maximo um separador
        if (cont > 1) {
            ok = false;
        }
        return ok;
    }

    // recebe o resultado e retorna o texto padrao
    public static String f(boolean b) {
        if (b) {
            return "SIM";
        } else {
            return "NAO";
        }
    }

    public static void main(String[] args) {
        // leitura inicial
        String s = MyIO.readLine();

        // repete ate ler a palavra FIM
        while (!s.equals("FIM")) {
            // executa os 4 testes e concatena os resultados com espaco
            String linhaSaida = f(v(s)) + " " + f(c(s)) + " " + f(i(s)) + " " + f(r(s));
            
            // imprime a linha final
            MyIO.println(linhaSaida);

            // le a proxima entrada do usuario
            s = MyIO.readLine();
        }
    }
}
