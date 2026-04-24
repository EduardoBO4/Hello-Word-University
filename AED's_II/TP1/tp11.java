import java.util.Scanner;

public class tp11 {

    // func de inversão que chama o recursivo com o índice
    public static String inverter(String s) {
        if (s == null || s.isEmpty()) return s;
        return inverterRecursivo(s, s.length() - 1);
    }

    // a rec que invert
    private static String inverterRecursivo(String s, int i) {
        // se o índice for menor que 0, a string acabou
        if (i < 0) {
            return "";
        }
        // pega o caractere atual (do fim) e junta com o próximo da recursão
        return s.charAt(i) + inverterRecursivo(s, i - 1);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        if (sc.hasNextLine()) {
            String entrada = sc.nextLine();

            // loop até ler "FIM"
            while (!(entrada.length() == 3 && entrada.equals("FIM"))) {
                
                System.out.println(inverter(entrada));

                if (sc.hasNextLine()) {
                    entrada = sc.nextLine();
                } else {
                    break;
                }
            }
        }
        
        sc.close();
    }
}
