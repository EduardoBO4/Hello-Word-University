class NoFlex{// exercios 2
    public char elemento; 
    public NoFlex prox;
    public NoFlex entrar; 
    public boolean folha; 

    public NoFlex(){
        this.elemento = ' '; 
        this.prox = null;  
        this.entrar = null;
        this.folha = false; 
    }

    public NoFlex(char letra){
        this.elemento = letra; 
        this.prox = null;  
        this.entrar = null;
        this.folha = false; 
    }
    public boolean pesquisar(String palavra, NoFlex raiz) {
    // ⬇️ Começamos olhando para o primeiro filho da raiz (a primeira letra possível)
    NoFlex atual = raiz.entrar; 

    // 🏎️ Vamos letra por letra da palavra que queremos buscar
    for (int i = 0; i < palavra.length(); i++) {
        char letraQueQueremos = palavra.charAt(i);
        boolean achou = false; 
        while (atual != null && !achou) {
            if (letraQueQueremos == atual.elemento) {
                achou = true; 
            } else {
                atual = atual.prox; // Se não for a letra, pula para o próximo irmão
            }
        }
        if (!achou) {
            return false; 
        }
        if (i < palavra.length() - 1) {
            atual = atual.entrar; 
        }
    }
    
    return (atual != null && atual.folha); 
}


    public void inserir(String palavra, NoFlex raiz) {
        NoFlex pai = raiz;
        NoFlex atual = raiz.entrar;
        for (int i = 0; i < palavra.length(); i++) {
            char letraQueQueremos = palavra.charAt(i);
            NoFlex ant = null;
            boolean temNesseNivel = false;
            while (atual != null && !temNesseNivel) {
                if (atual.elemento == letraQueQueremos) {
                    temNesseNivel = true;
                } else {
                    ant = atual;        
                    atual = atual.prox; 
                }
            }
            if (!temNesseNivel) {
                NoFlex novo = new NoFlex(letraQueQueremos);
                
                if (ant == null) {
                    pai.entrar = novo;
                } else {
                    
                    ant.prox = novo;
                }
                atual = novo; // O atual agora passa a ser o nó que acabamos de criar
            }
            if (i == palavra.length() - 1) {
                atual.folha = true;
            }
            pai = atual;
            atual = atual.entrar;
        }
    }
}

public class Atividade {

    
}
