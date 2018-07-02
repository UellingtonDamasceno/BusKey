/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.facade;

import br.uefs.ecomp.buskeyfx.controller.*;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class FacadeBuskeyfx {
    
    private static FacadeBuskeyfx facade;
    private final ControllerArquivos controllerArquivo;
    private final ControllerPesquisa controllerPesquisa;
    private final ControllerArvore controllerArvore;
    private final ControllerOrdenacao controllerOrdenacao;
    
    private FacadeBuskeyfx(){
        controllerArquivo = new ControllerArquivos();
        controllerPesquisa = new ControllerPesquisa();
        controllerArvore = new ControllerArvore();
        controllerOrdenacao = new ControllerOrdenacao();
    }
    
    public static synchronized FacadeBuskeyfx getInstance(){
        return (facade == null) ? facade = new FacadeBuskeyfx() : facade;
    }
    
    public void gerarLog() throws IOException{
        controllerArquivo.gerarLog();
    }
    
    public void carregarDicionario() throws IOException, ClassNotFoundException{
        controllerArvore.setDicionario(controllerArquivo.carregarDicionario());
        System.out.println(controllerArvore.listarTodasPalavras());
    }
    
    public LinkedList ordenar(LinkedList aOrdenar, String ordem){
       return controllerOrdenacao.ordenar(aOrdenar, ordem);
    }
    
    public LinkedList listarTodasPalavras(){
        return controllerArvore.listarTodasPalavras();
    }
    
    public LinkedList listarTodasPaginas(){
        return controllerArvore.listarTodasPaginas();
    }
    
    public LinkedList pesquisar(String palavrasChaves, String ordem) throws IOException{
        LinkedList resultadoPesquisa = controllerPesquisa.pesquisar(palavrasChaves, controllerArvore, controllerArquivo);
        controllerArquivo.atualizarArquivoDicionario(controllerArvore.getDicionario());
        return controllerOrdenacao.ordenar(resultadoPesquisa, ordem);
    }
 
    public String carregaConteudo(File arquivo, boolean validaConteudo) throws IOException{
        return controllerArquivo.carregaConteudo(arquivo, validaConteudo);
    }
    
    public void deletaPagina(Pagina pagina) throws IOException{
        controllerArvore.removerPaginaArvore(pagina);
        controllerArquivo.movePagina(pagina.getNome());
    }
    
    public boolean adcionarPagina(String nome, String conteudo) throws IOException{
       controllerArquivo.escreveArquivo(new File("arquivos\\"+nome), conteudo);
       return controllerArvore.adcionarPaginaArvore(nome, conteudo, controllerArquivo, controllerPesquisa);
    }
}
