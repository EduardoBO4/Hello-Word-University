// ⭐ CLASSE NO: Em vez de guardar as letras, guarda as "coordenadas" da palavra no vetor original.
class No {
   public int i; // Índice da palavra completa no array original de Strings.
   public int j; // Índice de onde começa o "pedaço" da palavra neste nó.
   public int k; // Índice de onde termina o "pedaço" da palavra neste nó.
   
   public int tamanho = 255; // Tabela Hash Perfeita (usando ASCII) para os filhos.
   public No[] prox;
   public boolean folha;
   
   public No (){
      this(-1, -1, -1);
   }

   public No (int i, int j, int k){
      this(i, j, k, false);
   }

   public No (int i, int j, int k, boolean folha){
      this.i = i;
      this.j = j;
      this.k = k;
      this.folha = folha;
      prox = new No [tamanho];
      for (int l = 0; l < tamanho; l++){
         prox[l] = null;
      }
   }

   public static int hash (char x){
      return (int)x;
   }
}

// ==============================================================================

class Patricia {
   No raiz;
   String[] array; // Vetor que guarda as palavras originais.

   public Patricia(){
      raiz = new No();
      array = null;
   }

   public void setArray(String[] array) throws Exception {
      this.array = array;
      for(int i = 0; i < array.length; i++){
         inserir(i); // Insere usando o índice da palavra no array.
      }
   }

   // ⭐ Métodos auxiliares para pegar a "substring" (o pedaço da palavra) baseando-se nos índices i, j, k.
   private String string(No no){
      return (no == raiz) ? " " : string(no.i, no.j, no.k);
   }
   
   private String string(int i, int j, int k){
      return array[i].substring(j, k + 1);
   }

   public void inserir(int i) throws Exception {
      inserir(raiz, i, 0);
   }

   // ⭐ O CORAÇÃO DA PATRÍCIA: Inserção e Quebra de Nós
   private void inserir(No no, int i, int j) throws Exception {

      // Se não existe nenhum nó começando com a letra atual, simplesmente cria um nó novo
      // que guarda todo o resto da palavra de uma vez só (compressão máxima!).
      if(no.prox[array[i].charAt(j)] == null){
         no.prox[array[i].charAt(j)] = new No(i, j, array[i].length() - 1, true);
         
      } else {
         // ⭐ ALERTA DE COLISÃO: Já existe um nó ali! Precisamos ver se vamos quebrar ele.
         String prox = string(no.prox[array[i].charAt(j)]); // Pedaço da palavra velha
         String inserindo = array[i].substring(j);          // Pedaço da palavra nova
         
         int k;
         // Conta quantas letras são IGUAIS entre a palavra velha e a nova
         for(k = 1; k < prox.length() && k < inserindo.length() && prox.charAt(k) == inserindo.charAt(k); k++);

         // Cenário 1: A palavra velha está inteira dentro da nova (ex: velha="CASA", nova="CASAMENTO")
         if(k == prox.length()){
            if(no.prox[array[i].charAt(j)].folha == true){
               throw new Exception("Erro: exite um prefixo de [" + array[i] + "] na arvore");
            } else {
               // Continua descendo na árvore para inserir o resto ("MENTO")
               inserir(no.prox[array[i].charAt(j)], i, j + k);
            }
            
         // Cenário 2: A palavra nova é prefixo da velha (ex: velha="CASAMENTO", nova="CASA")
         } else if (k == inserindo.length()){
            throw new Exception("Erro: [" + array[i] + "] é prefixo de outra palavra da árvore");
            
         // ⭐ Cenário 3: A GRANDE QUEBRA (BIFURCAÇÃO)! As palavras divergem no meio do caminho.
         } else {
            // Cria um nó novo só para a parte IGUAL (o prefixo em comum)
            No novo = new No(i, j, j + k - 1, false);
            
            // O nó velho vira filho do nó novo, mas ajustamos o índice 'j' dele 
            // para começar só depois da parte igual.
            novo.prox[prox.charAt(k)] = no.prox[array[i].charAt(j)];
            novo.prox[prox.charAt(k)].j = j + k;
            
            // A palavra nova também vira filha do nó novo, guardando o que sobrou dela.
            novo.prox[inserindo.charAt(k)] = new No(i, j + k, array[i].length() - 1, true);
            
            // Finalmente, liga esse novo nó "pai" (que quebrou os dois caminhos) na árvore original.
            no.prox[array[i].charAt(j)] = novo;
         }
      }
   }

   // ... (Restante dos métodos: pesquisar, mostrar, contarAs permanecem com a mesma lógica)
}