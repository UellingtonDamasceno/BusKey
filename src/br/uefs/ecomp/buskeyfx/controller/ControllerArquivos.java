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
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;

/**
 *
 * @author Uellington Damasceno
 */
public class ControllerArquivos {

   
    private final File[] arquivos;
    private boolean alterou;
    private File arquivoLog;
    private final File arquivoDicionario;

    public ControllerArquivos() {
        arquivoDicionario = new File("resources\\Dicionario.data");
        arquivos = pegarArquivos("arquivos", "txt");
        alterou = false;
    }
    
    public File[] getArquivos() {
        return arquivos;
    }

    public boolean getAlterou() {
        return alterou;
    }

    private File[] pegarArquivos(String caminho, final String extensao) {
        File diretorio = new File(caminho);
        FileFilter filtro = null;
        if (extensao != null) {
            filtro = (File nomeDoCaminho) -> nomeDoCaminho.getAbsolutePath().endsWith(extensao);
        }
        return diretorio.listFiles(filtro);
    }

    public String carregaConteudo(File arquivo, boolean validaConteudo) throws FileNotFoundException, IOException {
        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(arquivo));
        BufferedReader buffer = new BufferedReader(inputReader);
        String linha, conteudo = "";
        do {
            linha = buffer.readLine();
            if (linha != null) {
                conteudo += (validaConteudo) ? validaConteudo(linha) + "\n" : linha+"\n";    
            }
        } while (linha != null);
        inputReader.close();
        buffer.close();
        return conteudo;
    }

    public void escreveArquivo(File arq, String conteudo) throws FileNotFoundException, IOException {
        BufferedWriter arquivo = new BufferedWriter(new FileWriter(arq));
        PrintWriter pr = new PrintWriter(arquivo);
        String[] linhas = conteudo.split("\n");
        for (String linha : linhas) {
            pr.println(linha);
        }
        pr.flush();
        arquivo.close();
        pr.close();
    }

    public void movePagina(String nome) throws IOException {
        File arquivo = new File(nome);
        String conteudo = carregaConteudo(arquivo, true);
        File novoArquivo = new File(criaDiretorio("resources\\paginas removidas") + "\\" + nome);
        escreveArquivo(novoArquivo, conteudo);
        arquivo.delete();
    }

    public void atualizarArquivoDicionario(AVLTree dicionario) throws FileNotFoundException, IOException {
        File dadosDicionario = new File(criaDiretorio("resources").getAbsolutePath() + "\\Dicionario.data");
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

    public AVLTree carregarDicionario() throws IOException, ClassNotFoundException {
        criaDiretorio("resources");
        if (!arquivoDicionario.exists()) {
            arquivoDicionario.createNewFile();
        }
        if (arquivoDicionario.length() > 0) {
            ObjectInputStream entrada;
            entrada = new ObjectInputStream(new FileInputStream(arquivoDicionario));
            AVLTree dicionario = (AVLTree) entrada.readObject();
            entrada.close();
            return dicionario;
        }
        return new AVLTree();
    }

    private void limparDicionario(File registros) throws FileNotFoundException, IOException {
        Writer clean = new BufferedWriter(new FileWriter(registros));
        clean.close();
    }

    private File criaDiretorio(String nome) {
        File diretorio = new File(nome);
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }
        return diretorio;
    }

    public void gerarLog() throws IOException {
        File log = new File("resources\\log.txt");
        String conteudo = arquivos.length+"\n";
        if (!log.exists()) {
            for (File arquivo : arquivos) {
                conteudo += arquivo.toString() + ":" + arquivo.lastModified()+"\n"; //+ ":" + arquivo.getName().hashCode() + "\n";
            }
            escreveArquivo(log, conteudo);
        }
        arquivoLog = log;
    }

    public void verificaIntegridade() {
        try {
            if (!arquivoLog.exists() && arquivoDicionario.exists()) { //Log foi apagado
                /*Para garantir a integridade do dicionario é necessario 
                  pesquisar por todas as palavras novamente;
                 */
                alterou = true;
            } else {
                String conteudo = carregaConteudo(arquivoLog, false);
                String[] linhas = conteudo.split("\n");
                int numeroDeArquivos = Integer.parseInt(linhas[0]); //Pega o numero de arquivos existentes no log;
                if (numeroDeArquivos > arquivos.length) { 
                    int qtdDeletados = numeroDeArquivos - arquivos.length;
                    //removeDeletados(verificaArquivoDeletado(linhas, qtdDeletados));
                } else if (numeroDeArquivos < arquivos.length) {
                    //Algum arquivo foi add;
                } else {
                    //Quantidade de arquivos iguais;
                    //Verifica se algum arquivo foi renomeado;
                }
            }
        } catch (IOException ex) {
            alterou = true; //Deletaram log durante verificação então é necessario
        }
    }
    
//    private void removeDeletados(String[] arquivosDeletados) {
//        for(String arquivoDeletado : arquivosDeletados){
//            controllerEditar.removerPaginaArvore(new Pagina(arquivoDeletado, ""));
//        }
//    }
    
    private String[] verificaArquivoDeletado(String[] log, int numDeletados) {
        String[] deletados = new String[numDeletados];
        File arquivo;
        String[] nomeModificacao;
        int j = 0;
        for(int i = 1; i < log.length; i++){
            nomeModificacao = log[i].split(":");
            String nomeArquivoLog = nomeModificacao[0];
            arquivo  = new File(nomeArquivoLog);
            if(!(arquivo.exists())){
                deletados[j] = nomeArquivoLog;
                j++;
            }
        }
        return deletados;
    }

    public String validaConteudo(String conteudo) {
        return conteudo.replaceAll("[^A-Za-z0-9áàâãçaéèêíìîóòôúçñÁÀÂÉÈÊÍÌÓÔÚÇÑ@\\s ]", "");
    }
    
        /**
     * Método responsável por pecorrer toda árvore/dicionario, removendo uma determinada
     * pagina em todas palavras;
     * @param lPalavras
     * @param pagina Pagina a ser removida.
     */
    public void removerPaginaArvore(Iterator lPalavras, Pagina pagina) {
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            if (palavraAtual.contemPagina(pagina)) {
                palavraAtual.removerPagina(pagina);
            }
        }
    }
    
}
