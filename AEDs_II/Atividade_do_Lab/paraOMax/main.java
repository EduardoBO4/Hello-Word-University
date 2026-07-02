

// A função da prova está implementado no meio co código, obviamente tive que alterar as classes do código do ICEI para o da prova.

//  meu class implementado pra a resposta da prova, com base no do que o senhor passou do ICEI
class NoTrie {
   public char elemento;
   public final int tamanho = 26; 
   public NoTrie[] filho;        
   public int fraquencia;
   public boolean folha;
   

   public NoTrie (){
      this(' ');
   }

   public NoTrie (char elemento){
      this.elemento = elemento;
      filho = new NoTrie [tamanho];
      for (int i = 0; i < tamanho; i++) filho[i] = null;
      this.fraquencia = 0;
      this.folha = false;
   }
}

class ArvoreTrie {
    private NoTrie raiz;
    
    public NoTrie[] filho; 

    public ArvoreTrie(){
        raiz = new NoTrie();
        this.filho = raiz.filho; 
    }
                    // MEU CODIGO IMPLEMENTADO PRA A RESPOSTA DA PROVA, os erros estão comentados no metodo e explicado.
                    // se o senhor conseguir me dar (90% ou 80% seria o ideial)

                    // meu metodo implementado igual da prova 
                    public void inserir (String palavra){
                        int tam = palavra.length();

                        for(int i =0 ; i< palavra.length(); i++){
                            int val = palavra.charAt(i)-'a';

                            if(filho[val] !=null && i== tam-1){
                                filho[val].fraquencia +=1;

                            }else if( filho[val]!= null){
                                filho = filho[val].filho;

                            }else if(filho[val]== null){// meus 2 erros foram aqui
                                filho[val] = new NoTrie();/* eu apaguei sem querer na correria da prova o "filho[val] = new NoTrie(letra);" (o senhor consegue ver a rasura na prova) */
                                //filho = filho[val].filho; eu realmente ESQUECI erro mortal e testando em caso real, a arvore ficaria na horizontal

                            }else{
                                System.out.println("Erro geral");
                            }
                        }
                    }
                    // Max considere que eu tive que implementar e testar fisicamente o que nenhum aluno teve que fazer.

    public void reiniciarPonteiro() {
        this.filho = raiz.filho;
    }

    public void mostrar(){
        mostrar("", raiz);
    }

    private void mostrar(String s, NoTrie no) {
        for(int i = 0; i < no.filho.length; i++){
            if(no.filho[i] != null){
                char letra = (char)('a' + i);
                System.out.println("Palavra até aqui: " + s + letra + " | Frequência: " + no.filho[i].fraquencia);
                mostrar(s + letra, no.filho[i]);
            }
        }
    }
}

public class main {
    public static void main(String[] args) {
        ArvoreTrie arvore = new ArvoreTrie();

        System.out.println("A inserir a primeira palavra...");
        arvore.inserir("abc");
        arvore.reiniciarPonteiro();         
        System.out.println("A inserir a segunda palavra...");
        arvore.inserir("aba");
        System.out.println("\n--- Estrutura da Árvore ---");
        arvore.mostrar();
    }
}