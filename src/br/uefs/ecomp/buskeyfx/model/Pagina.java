/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class Pagina implements Comparable{

    private String nome;
    private LinkedList linhas;
    private int acessos;
    private int relevancia;

    /*Pode-se tbm utilizar um identifiador da pagina para indicar seu valor de referencia 
     */
    public Pagina(String nome, LinkedList linhas) {
        this.nome = nome;
        this.linhas = linhas;
        this.relevancia = 0;
        this.acessos = 0;
    }
    
    public int getRelevancia() {
        return relevancia;
    }
    public int getAcessos(){
        return acessos;
    }
    /**
     * Método responsavel por caucular a relevância de uma pagina com base nas palavrs chaves. 
     * @param palavrasChaves Palavras utilizadas como criterio de pontuação de relevância.
     */
    public void descobrirRelevancia(String[] palavrasChaves){
         int pEncontradas = 0;
        for (Iterator iLinhas = linhas.iterator(); iLinhas.hasNext();) {
            String[] palavras = (String[]) iLinhas.next();
            for (String palavraConteudo : palavras) {
                for (String palavraChave : palavrasChaves) {
                    if (palavraConteudo.equalsIgnoreCase(palavraChave)) {
                        pEncontradas++;
                    }
                }
            }
        }
        relevancia = (pEncontradas == 0 || linhas.isEmpty()) ? 0 : pEncontradas;
    }
    
    public boolean temRelevancia(){
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
}
