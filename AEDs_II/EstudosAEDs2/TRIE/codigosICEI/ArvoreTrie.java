class ArvoreTrie {
    private No raiz; // Único atributo: o nó raiz (ponto de partida da árvore)

    public ArvoreTrie(){
        raiz = new No(); // Construtor: inicializa a raiz vazia
    }

    // =========================================================================
    // MÉTODOS DE PESQUISA (BUSCA)
    // =========================================================================

    public boolean pesquisar(String s) throws Exception {
        return pesquisar(s, raiz, 0); // Dispara a busca a partir da raiz e da letra 0
    }

    public boolean pesquisar(String s, No no, int i) throws Exception {
        boolean resp;
        
        if(no.prox[s.charAt(i)] == null){
            resp = false; // Se a gaveta da letra atual está vazia, a palavra não existe
            
        } else if(i == s.length() - 1){
            resp = (no.prox[s.charAt(i)].folha == true); // Última letra: verifica se é folha (fim de palavra)
            
        } else if(i < s.length() - 1 ){
            resp = pesquisar(s, no.prox[s.charAt(i)], i + 1); // Não é a última: entra na gaveta e vai para a próxima letra
            
        } else {
            throw new Exception("Erro ao pesquisar!"); // Salvaguarda contra erros de índice
        }
        return resp;
    }

    // =========================================================================
    // MÉTODOS DE INSERÇÃO
    // =========================================================================

    public void inserir(String s) throws Exception {
        inserir(s, raiz, 0); // Dispara a inserção a partir da raiz e da letra 0
    }

    private void inserir(String s, No no, int i) throws Exception {
        System.out.print("\nEM NO(" + no.elemento + ") (" + i + ")");
        
        if(no.prox[s.charAt(i)] == null){ // Cenário 1: A gaveta da letra atual está vazia (null)
            System.out.print("--> criando filho(" + s.charAt(i) + ")");
            no.prox[s.charAt(i)] = new No(s.charAt(i)); // Cria o nó correspondente à letra dentro da gaveta dela

            if(i == s.length() - 1){
                System.out.print("(folha)");
                no.prox[s.charAt(i)].folha = true; // Se for a última letra da palavra, ativa o carimbo de folha
            } else {
                inserir(s, no.prox[s.charAt(i)], i + 1); // Se não for a última, entra no nó criado e avança a letra
            }

        } else if (no.prox[s.charAt(i)].folha == false && i < s.length() - 1){ // Cenário 2: O nó já existe e a palavra continua
            inserir(s, no.prox[s.charAt(i)], i + 1); // Reaproveita o prefixo existente, entra nele e avança para a próxima letra
            
        } else {
            throw new Exception("Erro ao inserir!"); // Cenário 3: Regra de prefixos violada (ex: tentar inserir palavra que já existe)
        } 
    }

    // =========================================================================
    // MÉTODOS DE EXIBIÇÃO (MOSTRAR)
    // =========================================================================

    public void mostrar(){
        mostrar("", raiz); // Inicia a exibição com o prefixo acumulado vazio a partir da raiz
    }

    public void mostrar(String s, No no) {
        if(no.folha == true){
            System.out.println("Palavra: " + (s + no.elemento)); // Se o nó é folha, imprime tudo o que acumulou até aqui + a letra
        } else {
            for(int i = 0; i < no.prox.length; i++){ // Se não for folha, varre as 255 gavetas de ponteiros do nó atual
                if(no.prox[i] != null){ // Se a gaveta i não estiver vazia, há um caminho ativo de palavras por ali
                    System.out.println("ESTOU EM (" + no.elemento + ") E VOU PARA (" + no.prox[i].elemento + ")");
                    mostrar(s + no.elemento, no.prox[i]); // Desce de nível recursivamente, somando a letra atual ao prefixo 's'
                }
            }
        }
    }

    // =========================================================================
    // MÉTODOS DE CONTAGEM (MÉTODO EXTRA)
    // =========================================================================

    public int contarAs(){
        int resp = 0;
        if(raiz != null){
            resp = contarAs(raiz); // Se a árvore não estiver totalmente nula, dispara o contador recursivo
        }
        return resp;
    }

    public int contarAs(No no) {
        int resp = (no.elemento == 'A') ? 1 : 0; // Se o caractere do nó atual for 'A', começa a contagem com 1, senão com 0
        
        for(int i = 0; i < no.prox.length; i++){ // Percorre todas as 255 gavetas deste nó procurando filhos pendurados
            if(no.prox[i] != null){
                resp += contarAs(no.prox[i]); // Se houver um filho, faz a contagem nele e soma ao acumulador total
            }
        }
        return resp;
    }
}