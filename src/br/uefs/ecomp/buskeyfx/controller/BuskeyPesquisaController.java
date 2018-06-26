/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import br.uefs.ecomp.buskeyfx.model.Palavra;
import br.uefs.ecomp.buskeyfx.util.AVLTree;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class BuskeyPesquisaController {

    private AVLTree dicionario; //estrutura responsavel por armazenar palavras já pesquisada pelo usuario.
    private File[] arquivos;
    private LinkedList log;

    public BuskeyPesquisaController() {
        dicionario = new AVLTree();
        arquivos = pegarArquivos("arquivos", ".txt");
        log = carregaLog();
    }

    private LinkedList carregaLog() {
        return null;
    }

    private File[] pegarArquivos(String caminho, final String extensao) {
        File diretorio = new File(caminho);
        FileFilter filtro = null;
        if (extensao != null) {
            filtro = (File nomeDoCaminho) -> nomeDoCaminho.getAbsolutePath().endsWith(extensao);
        }
        return diretorio.listFiles(filtro);
    }

    private Pagina carregarPagina(File arquivo) throws FileNotFoundException, IOException {
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(arquivo));
        BufferedReader buffer = new BufferedReader(inputReader);
        String linha, conteudo = "";
        do {
            linha = buffer.readLine();
            if (linha != null) {
                conteudo += validaConteudo(linha) + "\n";
            }
        } while (linha != null);
        inputReader.close();
        buffer.close();
        return new Pagina(arquivo.getCanonicalPath(), arquivo.lastModified(), conteudo);
    }
    
    private LinkedList procurarNoDicionario(Palavra[] palavrasChaves) {
        LinkedList paginasEncontradas = new LinkedList();
        for (Palavra palavraChave : palavrasChaves) {
            paginasEncontradas.addAll(((Palavra) dicionario.buscarPalavra(palavraChave)).getListaDeNomes());
        }
        return paginasEncontradas;
    }

    private LinkedList procurarNosArquivos(File[] arquivos, Palavra[] palavrasChaves) throws IOException {
        LinkedList paginasEncontradas = new LinkedList();
        Pagina pagina;
        dicionario.insereMult(palavrasChaves);
        for (File arquivo : arquivos) {
            pagina = BuskeyPesquisaController.this.carregarPagina(arquivo);
            for (Palavra palavraChave : palavrasChaves) {
                if (pagina.temRelevancia(palavraChave.getPalavra())) {//Verifica individualmente se a pagina atual tem relevancia para uma determinada palavra.
                    ((Palavra) dicionario.buscarPalavra(palavraChave)).addNovaPagina(pagina.getEndereco());
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina.getEndereco());
                    }
                }
            }
        }
        return paginasEncontradas;
    }

    //Método auxiliar que indica onde a busca pelas palavras pesquisadas devem acontecer.
    private int verificaPalavras(Palavra[] palavrasChaves, LinkedList dic, LinkedList arq) {
        boolean soArquivo = true, soDicionario = true;
        for (Palavra palavraAtual : palavrasChaves) {
            if (!dicionario.contem(palavraAtual)) {
                arq.add(palavraAtual);
                soDicionario = false;
            } else {
                dic.add(palavraAtual);
                soArquivo = false;
            }
        }
        if (soArquivo) {
            return -1;
        } else if (soDicionario) {
            return 1;
        } else {
            return 0;
        }
    }
    
    private boolean podeInserir(Pagina pagina){
        for(File atual : arquivos){
            if(atual.getName().equals(pagina.getNome())){
                return false;
            }
        }
        return true;
    }
    
    public void imprimeArvore(){
        Iterator iPalavras = dicionario.iterator();
        while(iPalavras.hasNext()){
            Palavra palavraAtual = (Palavra) iPalavras.next();
        }
    }
    
    public void adcionarPagina(Pagina pagina){
        if(podeInserir(pagina)){
            verificaAlteracao(pagina);
        }else{
            //lancar exceção;
        }
    }
    
    public void verificaAlteracao(Pagina pagina){
        Iterator lPalavras = dicionario.iterator();
        while(lPalavras.hasNext()){
            Palavra palavraAtual = (Palavra) lPalavras.next();
            if(pagina.temRelevancia(palavraAtual.getPalavra())){
                palavraAtual.addNovaPagina(pagina.getNome());
            }
        }
    }
    
    public void removerPagina(Pagina pagina){
        Iterator lPalavras = dicionario.iterator();
        while(lPalavras.hasNext()){
            Palavra palavraAtual = (Palavra) lPalavras.next();
            if(palavraAtual.contemPagina(pagina)){
                palavraAtual.removerPagina(pagina);
            }
        }
        movePagina(pagina);
    }
    
    public LinkedList pesquisar(String palavrasPesquisadas) throws IOException, FileNotFoundException {
        String[] palavrasChaves = validaConteudo(palavrasPesquisadas).split(" ");
        Palavra[] palavras = Palavra.stringToPalavra(palavrasChaves);
        LinkedList noDicionario = new LinkedList();
        LinkedList noArquivo = new LinkedList();
        LinkedList paginasEncontradas;
        int ondeProcurar = verificaPalavras(palavras, noDicionario, noArquivo);
        switch (ondeProcurar) {
            case -1: {
                paginasEncontradas = procurarNosArquivos(arquivos, palavras);
                break;
            }
            case 1: {
                paginasEncontradas = procurarNoDicionario(palavras);
                break;
            }
            default: {
                paginasEncontradas = procurarNosArquivos(arquivos, (Palavra[]) noArquivo.toArray(new Palavra[noArquivo.size()]));
                paginasEncontradas.addAll(procurarNoDicionario((Palavra[]) noDicionario.toArray(new Palavra[noDicionario.size()])));
                break;
            }
        }
        //descobreRelevancia(palavrasChaves, paginasPesquisadas);
        atualizarDicionario();
        System.out.println(paginasEncontradas.size());
        return paginasEncontradas;
    }

    private String validaConteudo(String conteudo) {
        return conteudo.replaceAll("[^A-Za-z0-9 ]", "");
    }

    public Pagina carregarPagina(int index, LinkedList resultado) throws IOException{
        return encontraPagina((String) resultado.get(index), resultado);
    }
    
    private Pagina encontraPagina(String outroNome, LinkedList resultado) throws IOException {
        Iterator lPesquisadas = resultado.iterator();
        while (lPesquisadas.hasNext()) {
            String paginaAtual = (String) lPesquisadas.next();
            if (paginaAtual.equals(outroNome)) {
                return this.carregarPagina(new File(paginaAtual).getAbsoluteFile());
            }
        }
        return null;
    }

   /*private void descobreRelevancia(String[] palavrasChaves, LinkedList paginasPesquisadas) throws IOException {
        Iterator lPagEncontradas = paginasPesquisadas.iterator();
        while (lPagEncontradas.hasNext()) {
            String caminho = (String) lPagEncontradas.next();
            Pagina paginaAtual = carregarPagina(caminho);
            paginaAtual.temRelevancia(palavrasChaves);
        }
    }*/

    public void carregarDicionario() throws IOException, ClassNotFoundException {
        criaDiretorio("resources");
        File dadosDicionario = new File("resources\\Dicionario.data");
        if (!dadosDicionario.exists()) {
            dadosDicionario.createNewFile();
        }
        if (dadosDicionario.length() > 0) {
            ObjectInputStream entrada;
            entrada = new ObjectInputStream(new FileInputStream(dadosDicionario));
            dicionario = (AVLTree) entrada.readObject();
            entrada.close();
        }
    }

    /*Pode da erro por que mudei o nome do endereço e tals.*/
    public void atualizarDicionario() throws FileNotFoundException, IOException {
        File dadosDicionario = new File(criaDiretorio("resources").getAbsolutePath() +"\\Dicionario.data");
        if (!dadosDicionario.exists()) {
            dadosDicionario.createNewFile();
        } else {
            limparDicionario(dadosDicionario);
        }
        ObjectOutputStream saida;
        saida = new ObjectOutputStream(new FileOutputStream(dadosDicionario));
        saida.writeObject(dicionario);
        saida.close();
    }
    
    private File criaDiretorio(String nome) {
        File diretorio = new File(nome);
        if (!diretorio.exists()) {
            if (diretorio.mkdir()) {
                //Lançar exeção
            }else{
                return diretorio;
            }
        }
        return diretorio;
    }

    private void limparDicionario(File registros) throws FileNotFoundException, IOException {
        Writer clean = new BufferedWriter(new FileWriter(registros));
        clean.close();
    }

    private void movePagina(Pagina pagina) {
        File arquivo = new File(pagina.getNome());
        arquivo.renameTo(new File(criaDiretorio("resources\\paginas removidas").getAbsolutePath(), arquivo.getName()));
    }
}
