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
    
    
}
