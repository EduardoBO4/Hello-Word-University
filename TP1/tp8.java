import java.util.Scanner; // usa para ler do teclado

public class tp8 {

    // método que valida a senha
    public static boolean validar(String s) {
        if (s.length() < 8) {
            return false;
        }

        boolean maiuscula = false;
        boolean minuscula = false;
        boolean numero = false;
        boolean especial = false;

        for (int i = 0; i < s.length(); i++) {// confirma cada uma das questões pedidas
            char c = s.charAt(i);

            if (c >= 'A' && c <= 'Z') {
                maiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                minuscula = true;
            } else if (c >= '0' && c <= '9') {
                numero = true;
            } else if (c != ' ' && c != '\r' && c != '\n') {
                especial = true;
            }
        }

        return (maiuscula && minuscula && numero && especial);
    }

    public static void main(String[] args) {
        // leir do teclado 
        Scanner leitor = new Scanner(System.in);

        // verifica se ainda tem linha para ler 
        while (leitor.hasNextLine()) {
            String entrada = leitor.nextLine();

            // condição de parada do exercício
            if (entrada.equals("FIM")) {
                break;
            }

            if (validar(entrada)) {
                System.out.println("SIM"); // System.out.println ou MyIO.println
            } else {
                System.out.println("NAO");
            }
        }

        leitor.close(); // é legal fazer pois é boa pratica 
    }
}
