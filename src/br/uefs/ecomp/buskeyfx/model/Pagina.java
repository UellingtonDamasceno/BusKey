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
    private int acessos;
    private int relevancia;
    private transient String conteudo;

    /*Pode-se tbm utilizar um identifiador da pagina para indicar seu valor de referencia 
     */
    public Pagina(String nome, String previa) {
        this.nome = nome;
        this.relevancia = 0;
        this.acessos = 0;
        this.conteudo = previa;
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

    public String getConteudo() {
        return conteudo;
    }

    /**
     * Método responsavel por caucular a relevância de uma pagina com base nas palavrs chaves.
     *
     * @param palavraChave Palavras utilizadas como criterio de pontuação de relevância.
     */
    public void descobrirRelevancia(String palavraChave) {
        int pEncontradas = 0;
        String[] palavras = quebraLinhas();
        for (String palavraConteudo : palavras) {
            if (palavraConteudo.equalsIgnoreCase(palavraChave)) {
                pEncontradas++;
            }
        }
        relevancia = pEncontradas;
    }

    public void descobrirMultRelevancia(String[] palavrasChaves) {
        int relevanciaAtual = 0;
        for (String palavraChave : palavrasChaves) {
            descobrirRelevancia(palavraChave);
            relevanciaAtual += relevancia;
        }
        relevancia = relevanciaAtual;
    }

    private String[] quebraLinhas() {
        String[] palavras = conteudo.split(" ");
        return palavras;
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
    public boolean equals(Object o) {
        if (o instanceof Pagina) {
            Pagina outra = (Pagina) o;
            return outra.getNome().equals(this.getNome());
        }
        return false;
    }
}
