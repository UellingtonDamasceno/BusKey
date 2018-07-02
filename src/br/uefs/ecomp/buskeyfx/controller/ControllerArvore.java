/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import br.uefs.ecomp.buskeyfx.model.Palavra;
import br.uefs.ecomp.buskeyfx.util.AVLTree;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class ControllerArvore {

    private AVLTree dicionario;

    public ControllerArvore() {
        dicionario = new AVLTree();
    }

    public void setDicionario(AVLTree dicionario) {
        this.dicionario = dicionario;
    }

    public AVLTree getDicionario() {
        return dicionario;
    }

    public Iterator listarDicionario() {
        return dicionario.iterator();
    }

    protected void inserirMultPalavras(Palavra[] palavras) {
        for (Palavra palavra : palavras) {
            palavra.addPesquisa();
            dicionario.inserir(palavra);
        }
    }
   
        /**
     * Método responsável por pecorrer toda árvore/dicionario, removendo uma determinada
     * pagina em todas palavras;
     * @param pagina Pagina a ser removida.
     */
    public void removerPaginaArvore(Pagina pagina) {
        Iterator lPalavras = listarDicionario();
        System.out.println("Entrou aki");
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            System.out.println(palavraAtual);
            if (palavraAtual.contemPagina(pagina)) {
                palavraAtual.removerPagina(pagina);
            }
        }

    }
    
    public boolean adcionarPaginaArvore(String nome, String conteudo, ControllerArquivos ca, ControllerPesquisa cp) {
        nome = ca.validaConteudo(nome);
        if (!nome.contains(".txt")) {
            nome += ".txt";
        }
        addPagSeRelevante(new Pagina(nome, conteudo), cp);
        
        return true;
    }

    public LinkedList listarTodasPaginas() {
        LinkedList aux = new LinkedList();
        Iterator lPalavras = dicionario.iterator();
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            if (palavraAtual.getListaPagina() != null) {
                aux.addAll(palavraAtual.getListaPagina());
            }
        }
        return aux;
    }

    public LinkedList listarTodasPalavras() {
        return dicionario.toList();
    }

    private void addPagSeRelevante(Pagina pagina, ControllerPesquisa cp) {
        Iterator lPalavras = listarDicionario();
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            int relevancia = cp.descobrirRelevancia(palavraAtual, pagina.getTextoConteudo().split(" "));
            if (relevancia > 0) {
                palavraAtual.addNovaPagina(pagina);
            }
        }
    }
}
