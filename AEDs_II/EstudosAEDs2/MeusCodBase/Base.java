package AEDs_II.EstudosAEDs2.MeusCodBase;

// ==========================================
// Todos os BASE
// ==========================================
public static boolean/int metodoArvore(int x, No i) {
   // 1. Inicia a resposta com o cenário de falha/vazio
   boolean resp = false; // (ou int resp = 0; se for contagem)

   // 2. Só faz algo se o nó existir
   if (i != null) {
      if (i.elemento == x) { // ACHOU! (ou i.elemento % 2 == 0)
         resp = true;        // (ou resp = 1; se for contagem)
         
      } else if (x < i.elemento) {
         resp = metodoArvore(x, i.esq); // Desce pra esquerda
         
      } else {
         resp = metodoArvore(x, i.dir); // Desce pra direita
      }
   }
   // 3. Único return
   return resp;
}



// Lista Flex e Tabela hash
public boolean metodoLista(int x) {
   boolean resp = false;

   // Repare na condição do FOR: "!resp". 
   // Se resp virar true, o loop quebra na hora! Não precisa de 'break' ou múltiplos returns.
   for (Celula i = primeiro.prox; i != null && !resp; i = i.prox) {
      if (i.elemento == x) {
         resp = true;
      }
   }

   return resp;
}


// as hibridas aqui
public boolean metodoDoidona(int x) {
   boolean resp = false;
   int posT1 = hashT1(x);

   // 1. Testa a Tabela Principal
   if (t1[posT1] == x) {
      resp = true;

   // 2. Se não tá na principal, ROTEIA pela T2
   } else {
      int rota = hashT2(x);
      
      if (rota == 0) {
         resp = pesquisarT3(x); // Vai pra Hash c/ Rehash
         
      } else if (rota == 1) {
         resp = lista.pesquisar(x); // Vai pra Lista Flexível
         
      } else if (rota == 2) {
         resp = arvoreAVL.pesquisar(x); // Vai pra AVL
      }
   }
   
   return resp;
}




// ==========================================
// Mostrar gerais
// ==========================================

class No {
   public int elemento;
   public No esq, dir;
   public int nivel; // Controla a altura para o fator de balanceamento

   // Construtor: Todo nó novo nasce com nível 1
   public No(int elemento) {
      this.elemento = elemento;
      this.esq = null;
      this.dir = null;
      this.nivel = 1;
   }
}

// Métodos na classe AVL
public void mostrar() {
   mostrar(raiz);
}

private void mostrar(No i) {
   if (i != null) {
      mostrar(i.esq);
      System.out.print(i.elemento + " ");
      mostrar(i.dir);
   }
}


// ==========================================
// ==========================================
// ==========================================


class NoAN {
   public int elemento;
   public NoAN esq, dir;
   public boolean cor; // true = Vermelho, false = Preto

   // Construtor
   public NoAN(int elemento) {
      this.elemento = elemento;
      this.esq = null;
      this.dir = null;
      this.cor = true; // Nasce Vermelho (Alvo) por padrão
   }
}

// Métodos na classe Alvinegra
public void mostrar() {
   System.out.print("[ ");
   mostrar(raiz);
   System.out.println("]");
}

private void mostrar(NoAN i) {
   if (i != null) {
      mostrar(i.esq);
      System.out.print(i.elemento + ((i.cor) ? "(V) " : "(P) "));
      mostrar(i.dir);
   }
}

// ==========================================
// ==========================================
// ==========================================
// ==========================================



class No234 {
   public int[] elemento;
   public No234[] prox;
   public int n; // Quantidade de chaves preenchidas no nó (1, 2 ou 3)

   // Construtor
   public No234() {
      this.elemento = new int[3];
      this.prox = new No234[4];
      this.n = 0;
      for (int i = 0; i < 4; i++) {
         this.prox[i] = null;
      }
   }
}

// Métodos na classe Arvore234
public void mostrar() {
   mostrar(raiz);
}

private void mostrar(No234 i) {
   if (i != null) {
      int j = 0;
      // Intercala a chamada do filho com a exibição do elemento correspondente
      for (j = 0; j < i.n; j++) {
         mostrar(i.prox[j]);
         System.out.print(i.elemento[j] + " ");
      }
      mostrar(i.prox[j]); // Visita o último ponteiro válido do nó
   }
}

// ==========================================
// ==========================================
// ==========================================
// ==========================================

class Celula {
   public int elemento;
   public Celula prox;

   public Celula(int elemento) {
      this.elemento = elemento;
      this.prox = null;
   }
}

class Lista {
   public Celula primeiro, ultimo;

   public Lista() {
      this.primeiro = new Celula(-1); // Célula cabeça padrão PUC
      this.ultimo = this.primeiro;
   }
}

class HashIndireto {
   public Lista[] tabela;
   public int tamanho;

   // Construtor aloca o array de listas e instancia cada uma delas
   public HashIndireto(int tamanho) {
      this.tamanho = tamanho;
      this.tabela = new Lista[tamanho];
      for (int i = 0; i < tamanho; i++) {
         this.tabela[i] = new Lista();
      }
   }

   public void mostrar() {
      for (int i = 0; i < tamanho; i++) {
         System.out.print("Posição [" + i + "]: ");
         // Pula a célula cabeça começando do primeiro.prox
         for (Celula c = tabela[i].primeiro.prox; c != null; c = c.prox) {
            System.out.print(c.elemento + " -> ");
         }
         System.out.println("null");
      }
   }
}

// ==========================================
// ==========================================
// ==========================================
// ==========================================

class NoTrie {
   public char elemento;
   public NoTrie[] prox;
   public boolean folha;
   public final int TAMANHO_ASCII = 255;

   // Construtores
   public NoTrie() {
      this(' ');
   }

   public NoTrie(char elemento) {
      this.elemento = elemento;
      this.folha = false;
      this.prox = new NoTrie[TAMANHO_ASCII];
      for (int i = 0; i < TAMANHO_ASCII; i++) {
         this.prox[i] = null;
      }
   }
}

// Métodos na classe ArvoreTrie
public void mostrar() {
   mostrar("", raiz);
}

private void mostrar(String s, NoTrie no) {
   if (no != null) {
      if (no.folha == true) {
         System.out.println("Palavra: " + s + no.elemento);
      } else {
         for (int i = 0; i < no.prox.length; i++) {
            if (no.prox[i] != null) {
               mostrar(s + no.elemento, no.prox[i]);
            }
         }
      }
   }
}




// ==========================================
// TRIE A PARTE 
// ==========================================

class NoTrie {
   public char elemento;
   public NoTrie[] prox;
   public boolean folha;
   public final int TAMANHO_ASCII = 255;

   // Construtores
   public NoTrie() {
      this(' ');
   }

   public NoTrie(char elemento) {
      this.elemento = elemento;
      this.folha = false;
      this.prox = new NoTrie[TAMANHO_ASCII];
      for (int i = 0; i < TAMANHO_ASCII; i++) {
         this.prox[i] = null;
      }
   }
}

// Métodos na classe ArvoreTrie
public void mostrar() {
   mostrar("", raiz);
}

private void mostrar(String s, NoTrie no) {
   if (no != null) {
      if (no.folha == true) {
         System.out.println("Palavra: " + s + no.elemento);
      } else {
         for (int i = 0; i < no.prox.length; i++) {
            if (no.prox[i] != null) {
               mostrar(s + no.elemento, no.prox[i]);
            }
         }
      }
   }
}













public class Base {
    
}
