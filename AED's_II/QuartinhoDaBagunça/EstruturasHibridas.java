impot java.util.*;
public class EstruturasHibridas {
    public static boolean pesPala(No raiz, String nome) {
    		if (raiz == null) {
        		return false;
    		}
		// OUUU : int comp = nome.compareTo(raiz.palavra);
	
    		if (nome.compareTo(raiz.palavra) == 0) {
        		return true; 
    		} 

    		else if (nome.compareTo(raiz.palavra) > 0) { 
        		return pesPala(raiz.esq, nome); 
    		} 
    
    		else if (nome.compareTo(raiz.palavra) < 0) { 
        		return pesPala(raiz.dir, nome); 
    		}

    		return false;
	}

	public static boolean pesqLetra(No raiz1, String nome) {
    		if (raiz1 == null) {
        		return false;
    		}

    		char letraInicial = nome.charAt(0);

    		if (letraInicial == raiz.letra) {
      
       	 		return pesPala(raiz1.subArv, nome); 
    		} 
    		else if (letraInicial > raiz1.letra) {
        		return pesqLetra(raiz1.esq, nome); 
    		} 
    		else {
       			return pesqLetra(raiz1.dir, nome); 
    		}
	}    
    
    public static void main(String[] args) {
        // Aqui você pode criar instâncias das suas classes e testar os métodos
    }
}
