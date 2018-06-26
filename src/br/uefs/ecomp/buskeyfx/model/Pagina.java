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

    private String nome;
    private String endereco;
    private double modificacao;
    private int acessos;
    private int relevancia;
    private transient String conteudo; //Marcador utilizado para indicar que não deve ser impresso.

    public Pagina(String endereco, double modificacao, String conteudo) {
        this.nome = retiraNome(endereco);
        this.endereco = endereco;
        this.relevancia = 0;
        this.acessos = 0;
        this.conteudo = conteudo;
    }
    
    public int getAcessos() {
        return acessos;
    }
    
    public void addAcessos(int acessos) {
        this.acessos += acessos;
    }

    public String getNome() {
        return nome;
    }
    
    public String getEndereco(){
        return endereco;
    }
    
    public String getConteudo() {
        return conteudo;
    }
    
    public int getRelevancia() {
        return relevancia;
    }
    
    public String getPrevia() {
        return tiraPrevia();
    }

    public boolean temRelevancia(String palavraChave) {
        descobrirRelevancia(palavraChave);
        return (relevancia > 0);
    }
    
    public boolean temRelevancia(String[] palavrasChaves){
        descobrirRelevancia(palavrasChaves);
        return (relevancia > 0);
    }
    
    /**
     * Método responsavel por caucular a relevância de uma pagina com base nas palavrs chaves.
     *
     * @param palavraChave Palavras utilizadas como criterio de pontuação de relevância.
     */
    private void descobrirRelevancia(String palavraChave) {
        int pEncontradas = 0;
        String[] palavras = quebraLinhas();
        for (String palavraConteudo : palavras) {
            if (palavraConteudo.equalsIgnoreCase(palavraChave)) {
                pEncontradas++;
            }
        }
        relevancia = pEncontradas;
    }
    
    private void descobrirRelevancia(String[] palavrasChaves) {
        int relevanciaAtual = 0;
        for (String palavraChave : palavrasChaves) {
            descobrirRelevancia(palavraChave);
            relevanciaAtual += relevancia;
        }
        relevancia = relevanciaAtual;
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

    private String[] quebraLinhas() {
        String[] palavras = conteudo.split(" ");
        return palavras;
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
