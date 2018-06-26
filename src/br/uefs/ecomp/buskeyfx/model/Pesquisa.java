/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.model;

import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class Pesquisa {
    private LinkedList paginasEncontradas;
    
    public Pesquisa(LinkedList pesquisa){
        this.paginasEncontradas = pesquisa; 
    }
    
    public LinkedList getPaginasEncontradas(){
        return paginasEncontradas;
    }
    
    /*public Pagina getPagina(String outroNome) throws IOException {
        Iterator lPesquisadas = paginasEncontradas.iterator();
        while (lPesquisadas.hasNext()) {
            String paginaAtual = (String) lPesquisadas.next();
            if (paginaAtual.equals(outroNome)) {
                return carregarPagina(new File(paginaAtual).getAbsoluteFile());
            }
        }
        return null;
    }*/
    
}
