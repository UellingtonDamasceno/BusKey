/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.model;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class Palavra implements Comparable, Serializable{
    
    private String palavra;
    private LinkedList listaDeNomes;
    
    public Palavra(String palavra, LinkedList paginas) {
        this(palavra);
        this.listaDeNomes = paginas;
        
    }

    public Palavra(String palavra) {
        this.palavra = palavra;
        this.listaDeNomes = new LinkedList();
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }

    public LinkedList getListaDeNomes() {
        return listaDeNomes;
    }

    public void setPaginas(LinkedList paginas) {
        this.listaDeNomes = paginas;
    }

    public void addNovaPagina(String nomePagina) {
        listaDeNomes.add(nomePagina);
    }

    public boolean contemPagina(Pagina pagina) {
        return listaDeNomes.contains(pagina);
    }

    public Object removerPagina(Pagina pagina) {
        return listaDeNomes.remove(pagina);
    }
    
    public static Palavra[] stringToPalavra(String[] string){
        Palavra[] suporte = new Palavra[string.length];
        for(int i = 0; i < string.length; i++){
            suporte[i] = new Palavra(string[i]);
        }
        return suporte;
    }
    
    public static String[] palavraToString(Palavra[] palavras){
        String[] suporte = new String[palavras.length];
        for(int i = 0; i < palavras.length; i++){
            suporte[i] = palavras[i].getPalavra();
        }
        return suporte;
    }
    
    @Override
    public int compareTo(Object o) {
        Palavra outra = (Palavra) o;
        return palavra.compareToIgnoreCase(outra.getPalavra());
    }

    @Override
    public String toString() {
        return palavra;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Palavra) {
            Palavra outra = (Palavra) o;
            if (this.getPalavra().equalsIgnoreCase(outra.getPalavra())) {
                return true;
            }
        }
        return false;
    }

}
