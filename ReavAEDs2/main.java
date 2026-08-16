
inicio.diagUnificada();
public CelulaDupla diagUnificada(){
    CelulaDupla novo = null;
    CelulaMatriz aux = inicio;
    if (aux != null){
        do{
            aux = inicio.inicio;
            if(aux != null){
                do{
                    novo = new CelulaDupla(aux.elemento,  novo, null);// passa elemento, ponteiro para o anterior e o proximo
                    novo=novo.prox;
                    aux = aux.prox;
                }while(aux != null);
            }
        }while (aux != null);
        
    }
    return novo;
}
public class main {
    
}
