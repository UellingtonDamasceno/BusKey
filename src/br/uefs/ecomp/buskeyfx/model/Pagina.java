/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.model;

import java.io.Serializable;

/**
 *
 * @author Uellington Damasceno
 */
public class Pagina implements Comparable, Serializable {

    private final String nome;
    private int acessos;
    private int ocorrencia;
    private transient String textoConteudo; //Marcador utilizado para indicar que não deve ser impresso.

    public Pagina(String nome, String conteudo) {
        this.nome = nome;
        this.textoConteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public int getAcessos() {
        return acessos;
    }

    public int getOcorrencia() {
        return ocorrencia;
    }

    public void setOcorrencia(int ocorrencia) {
        this.ocorrencia = ocorrencia;
    }

    public String getTextoConteudo() {
        return textoConteudo;
    }

    public void setTextoConteudo(String textoConteudo) {
        this.textoConteudo = textoConteudo;
    }

    public void addAcessos() {
        this.acessos++;
    }
    
    public String getPrevia(){
        return tiraPrevia();
    }

    private String tiraPrevia() {
        if (textoConteudo.isEmpty()) {
            return "!!!PAGINA SEM CONTEUDO!!!";
        } else {
            int tamanho = (textoConteudo.length() > 100) ? 100 : textoConteudo.length();
            String sup = "";
            for (int i = 0; i < tamanho; i++) {
                sup += !(i == 50) ? textoConteudo.charAt(i) : "\n";
            }
            return sup + "...";
        }
    }

    private String retiraNome(String aCortar) {
        return aCortar.substring((aCortar.lastIndexOf("\\") + 1));
    }

    @Override
    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Object o) {
        Pagina outraPagina = (Pagina) o;
        return this.getOcorrencia() - outraPagina.getOcorrencia();
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
