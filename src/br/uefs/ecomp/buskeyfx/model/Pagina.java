/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.model;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class Pagina implements Comparable, Serializable {

    private String nome;
    private LinkedList linhas;
    private int acessos;
    private int relevancia;
    private String previa;
    /*Pode-se tbm utilizar um identifiador da pagina para indicar seu valor de referencia 
     */
    public Pagina(String nome, LinkedList linhas, String previa) {
        this.nome = nome;
        this.linhas = linhas;
        this.relevancia = 0;
        this.acessos = 0;
        this.previa = previa;
    }

    public int getRelevancia() {
        return relevancia;
    }   
    
    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos += acessos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LinkedList getLinhas() {
        return linhas;
    }

    public void setLinhas(LinkedList linhas) {
        this.linhas = linhas;
    }

    /**
     * Método responsavel por caucular a relevância de uma pagina com base nas palavrs chaves.
     *
     * @param palavraChave Palavras utilizadas como criterio de pontuação de relevância.
     */
    public void descobrirRelevancia(String palavraChave) {
        int pEncontradas = 0;
        for (Iterator iLinhas = linhas.iterator(); iLinhas.hasNext();) {
            String[] palavras = (String[]) iLinhas.next();
            for (String palavraConteudo : palavras) {
                if (palavraConteudo.equalsIgnoreCase(palavraChave)) {
                    pEncontradas++;
                }
            }
        }
        relevancia = (pEncontradas == 0 || linhas.isEmpty()) ? 0 : pEncontradas;
    }
    
    public void descobrirMultRelevancia(String[] palavrasChaves){
        int relevanciaAtual = relevancia;
        for(String palavraChave : palavrasChaves){
            descobrirRelevancia(palavraChave);
            relevanciaAtual += relevancia;
        }
        relevancia = relevanciaAtual;
    }
    
    public boolean temRelevancia() {
        return relevancia > 0;
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Object o) {
        Pagina outraPagina = (Pagina) o;
        return this.getRelevancia() - outraPagina.getRelevancia();
    }
    
    @Override
    public boolean equals(Object o){
        if(o instanceof Pagina){
            Pagina outra = (Pagina) o;
            return outra.getNome().equals(this.getNome());
        }
        return false;
    }
}
