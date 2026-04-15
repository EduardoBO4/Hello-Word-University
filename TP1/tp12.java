import java.util.Scanner;

public class tp12 {

    // metodo recursivo para somar digitos
    public static int somar(int n) {
        int resp;

        // caso base: se so sobrou um digito
        if (n < 10) {
            resp = n;
        } else {
            // soma o ultimo digito (n % 10) com o resto do numero (n / 10)
            resp = (n % 10) + somar(n / 10);
        }

        return resp;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Verifica se há algo para ler antes de começar
        if (sc.hasNextLine()) {
            String linha = sc.nextLine();

            // loop ate ler FIM
            while (!linha.equals("FIM")) {
                try {
                    // converte a string lida para inteiro
                    int num = Integer.parseInt(linha);

                    // chama o metodo e imprime o resultado usando System.out
                    System.out.println(somar(num));
                } catch (NumberFormatException e) {
                    // Caso a linha não seja um número e não seja FIM
                }

                // le a proxima linha
                if (sc.hasNextLine()) {
                    linha = sc.nextLine();
                } else {
                    break;
                }
            }
        }
        
        sc.close(); // Sempre bom fechar o recurso
    }
}
