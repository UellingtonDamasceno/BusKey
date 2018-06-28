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
    private final String endereco;
    private double modificacao;
    private int acessos;
    private int relevancia;
    private transient String conteudo; //Marcador utilizado para indicar que não deve ser impresso.

    public Pagina(String endereco, double modificacao, String conteudo) {
        this.nome = retiraNome(endereco);
        this.endereco = endereco;
        this.conteudo = conteudo;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public double getModificacao() {
        return modificacao;
    }

    public void setModificacao(double modificacao) {
        this.modificacao = modificacao;
    }

    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos = acessos;
    }

    public int getRelevancia() {
        return relevancia;
    }

    public void setRelevancia(int relevancia) {
        this.relevancia = relevancia;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void addAcessos(int acessos) {
        this.acessos += acessos;
    }
    
    public String getPrevia(){
        return tiraPrevia();
    }
    
    private int descobrirRelevancia(String palavraChave) {
        int pEncontradas = 0;
        String[] palavras = conteudo.split(" ");
        for (String palavraConteudo : palavras) {
            if (palavraConteudo.equalsIgnoreCase(palavraChave)) {
                pEncontradas++;
            }
        }
        return pEncontradas;
    }

    private int descobrirRelevancia(String[] palavrasChaves) {
        int relevanciaAtual = 0;
        for (String palavraChave : palavrasChaves) {
            relevanciaAtual +=  descobrirRelevancia(palavraChave);
        }
        return relevanciaAtual;
    }

    private String tiraPrevia() {
        if (conteudo.isEmpty()) {
            return "!!!PAGINA SEM CONTEUDO!!!";
        } else {
            int tamanho = (conteudo.length() > 100) ? 100 : conteudo.length();
            String sup = "";
            for (int i = 0; i < tamanho; i++) {
                sup += !(i == 50) ? conteudo.charAt(i) : "\n";
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
