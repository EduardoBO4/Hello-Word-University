import java.util.*;
public class Pessoa{
    private String nome;
    private int dia;
    private int mes;
    public Pessoa(String nome, int dia, int mes){
        this.nome = nome;
        this.dia = dia;
        this.mes = mes;
    }
    void setNome(String nome){
        this.nome = nome;
    }
    void setDia(int dia){
        this.dia = dia;
    }
    void setMes(int mes){
        this.mes = mes;
    }
    String getNome(){
        return this.nome;
    }
    int getDia(){
        return this.dia;
    }
    int getMes(){
        return this.mes;
    }
}
public class hashMinha {
    private Pessoa[] tabela;
    public hashMinha(){
        tabela = new Pessoa[366];
    }
    public void inserir(Pessoa p){
        int mes [] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int cont= 0;
        for(int i = 0; i < p.getMes(); i++){
            cont += mes[i];
        }
        cont += p.getDia();
        cont--;
        tabela[cont] = p;
    }
    public Pessoa pesquisar(int dia, int mes, String nome){
         int meses [] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int cont= 0;
        for(int i = 0; i < mes; i++){
            cont += meses[i];
        }
        cont += dia;
        cont--;
        if(tabela[cont] != null && tabela[cont].getNome().equals(nome)){
            return tabela[cont];
        }else{
            System.out.println("Pessoa não encontrada");
            return null;
        }
    }
    public Pessoa remover(int dia, int mes, String nome){
         int meses [] = {0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int cont= 0;
        for(int i = 0; i < mes; i++){
            cont += meses[i];
        }
        cont += dia;
        cont--;
        if(tabela[cont] != null && tabela[cont].getNome().equals(nome)){
            System.out.println(tabela[cont].getNome()+ " removido com sucesso");
            Pessoa p = tabela[cont];
            tabela[cont] = null;
            return p;
        }else{
            System.out.println("Pessoa não encontrada");
            return null;
        }
    }

}

