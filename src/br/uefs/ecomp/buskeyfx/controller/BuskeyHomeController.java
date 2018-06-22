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
public class BuskeyHomeController {

    private AVLTree dicionario; //estrutura responsavel por armazenar palavras já pesquisada pelo usuario.
    private LinkedList paginasPesquisadas; // Utilzada para armazenar as paginas encontradas durante uma pesquisa;
    private File[] arquivos;
    private LinkedList log;

    public BuskeyHomeController() {
        dicionario = new AVLTree();
        paginasPesquisadas = new LinkedList();
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
        LinkedList linhas = new LinkedList();
        String[] palavras;
        String linha, previa = "";
        do {
            linha = buffer.readLine();
            if (linha != null) {
                palavras = linha.split(" ");
                linhas.add(palavras);
            }
        } while (linha != null);
        inputReader.close();
        buffer.close();
        return new Pagina(arquivo.getName(), linhas, previa);
    }

    private LinkedList procurarNoDicionario(Palavra[] palavrasChaves) {
        LinkedList paginasEncontradas = new LinkedList();
        for (Palavra palavraChave : palavrasChaves) {
            System.out.println(palavraChave);
            paginasEncontradas.addAll(((Palavra) dicionario.buscarPalavra(palavraChave)).getPaginas());
        }
        return paginasEncontradas;
    }

    private LinkedList procurarNosArquivos(File[] arquivos, Palavra[] palavrasChaves) throws IOException {
        LinkedList paginasEncontradas = new LinkedList();
        Pagina pagina;
        for (File arquivo : arquivos) {
            pagina = carregarPagina(arquivo);
            for (Palavra palavraChave : palavrasChaves) {
                System.out.println(palavraChave.getPalavra());
                pagina.descobrirRelevancia(palavraChave.getPalavra());
                if (pagina.temRelevancia()) {//Verifica individualmente se a pagina atual tem relevancia para uma determinada palavra.
                    palavraChave.addNovaPagina(pagina);
                    dicionario.inserir(palavraChave); //Adciona a palavra ao dicionario.
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina);
                    }
                }
                pagina = null;
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

    public LinkedList pesquisar(String palavrasPesquisadas) throws IOException, FileNotFoundException {
        /*Antes de pesquisar deve-se verifiar se as palavras chaves que o usuario digitou
        já foram pesquisadas anteriomente. isto é, verificar no "Dicionario" (estutura de dados)
        se TODAS as palavras existe, caso uma não exista deve-se procurar somente aquelas que
        possue aquela nova palavra juntamente com as demais paginas.
         */
        Palavra[] palavras = Palavra.stringToPalavra(palavrasPesquisadas.split(" "));
        LinkedList noDicionario = new LinkedList();
        LinkedList noArquivo = new LinkedList();
        int ondeProcurar = verificaPalavras(palavras, noDicionario, noArquivo);
        switch (ondeProcurar) {
            case -1: {
                paginasPesquisadas = procurarNosArquivos(arquivos, palavras);
                break;
            }
            case 1: {
                paginasPesquisadas = procurarNoDicionario(palavras);
                break;
            }
            default: {
                System.out.println(noArquivo);
                System.out.println(noDicionario);
                paginasPesquisadas = procurarNosArquivos(arquivos, (Palavra[]) noArquivo.toArray(new Palavra[noArquivo.size()]));
                paginasPesquisadas.addAll(procurarNoDicionario((Palavra[]) noDicionario.toArray(new Palavra[noDicionario.size()])));
                break;
            }
        }
        System.out.println(paginasPesquisadas.size());
        System.out.println(paginasPesquisadas);
        return paginasPesquisadas;
    }

    public void carregarDicionario() throws IOException, ClassNotFoundException {
        File dadosDicionario = new File("arquivos\\Dicionario.txt");
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

    public void atualizarDicionario() throws FileNotFoundException, IOException {
        
        File dadosDicionario = new File("arquivos\\Dicionario.txt");
        if (!dadosDicionario.exists()) {
            dadosDicionario.createNewFile();
        }else{
            limparDicionario(dadosDicionario);
        }
        ObjectOutputStream saida;
        saida = new ObjectOutputStream(new FileOutputStream(dadosDicionario));
        saida.writeObject(dicionario);
        saida.close();
    }

   private void limparDicionario(File registros) throws FileNotFoundException, IOException {
        Writer clean = new BufferedWriter(new FileWriter(registros));
        clean.close();
    }


    public LinkedList PaginasEncontradas() {
        return paginasPesquisadas;
    }

    public Pagina getPagina(String outroNome) {
        Iterator lPesquisadas = paginasPesquisadas.iterator();
        while (lPesquisadas.hasNext()) {
            Pagina paginaAtual = (Pagina) lPesquisadas.next();
            if (paginaAtual.getNome().equals(outroNome)) {
                return paginaAtual;
            }
        }
        return null;
    }
}
