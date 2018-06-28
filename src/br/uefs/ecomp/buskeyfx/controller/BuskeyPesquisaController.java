/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.model.*;
import br.uefs.ecomp.buskeyfx.util.*;
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
    private final QuickSort quick;

    public BuskeyPesquisaController() {
        dicionario = new AVLTree();
        arquivos = pegarArquivos("arquivos", ".txt");
        quick = new QuickSort();
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

    public String carregaConteudo(File arquivo) throws FileNotFoundException, IOException {
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
        return conteudo;
    }

    private LinkedList procurarNoDicionario(Palavra[] palavrasChaves) {
        LinkedList<Pagina> paginasEncontradas = new LinkedList();
        for (Palavra palavraChave : palavrasChaves) {
            Palavra palavraEncontrada = ((Palavra) dicionario.buscarPalavra(palavraChave));
            paginasEncontradas.addAll(palavraEncontrada.getListaPagina());
        }
        return paginasEncontradas;
    }

    private LinkedList procurarNosArquivos(File[] arquivos, Palavra[] palavrasChaves) throws IOException {
        LinkedList<Pagina> paginasEncontradas = new LinkedList();

        dicionario.insereMult(palavrasChaves);
        for (File arquivo : arquivos) {
            Pagina pagina;
            String conteudo = carregaConteudo(arquivo);
            for (Palavra palavraChave : palavrasChaves) {
                int relevancia = descobrirRelevancia(palavraChave, conteudo.split(" "));
                if (relevancia > 0) {//Verifica individualmente se a pagina atual tem relevancia para uma determinada palavra.
                    pagina = new Pagina(arquivo.getCanonicalPath(), arquivo.lastModified(), conteudo);
                    ((Palavra) dicionario.buscarPalavra(palavraChave)).addNovaPagina(pagina);
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina);
                    }
                    pagina.setRelevancia(descobrirRelevancia(palavrasChaves, pagina));
                }
            }
        }
        return paginasEncontradas;
    }

    //Método auxiliar que indica onde a busca pelas palavras pesquisadas devem acontecer.
    private int verificaPalavras(Palavra[] palavrasChaves, LinkedList dic, LinkedList arq) {
        boolean soArquivo = true, soDicionario = true;
        for (Palavra palavraAtual : palavrasChaves) {
            palavraAtual.addPesquisa();
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

    private boolean podeInserir(Pagina pagina) {
        for (File atual : arquivos) {
            if (atual.getName().equals(pagina.getNome())) {
                return false;
            }
        }
        return true;
    }

    public void imprimeArvore() {
        Iterator iPalavras = dicionario.iterator();
        while (iPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) iPalavras.next();
            System.out.println(palavraAtual);
            System.out.println(palavraAtual.getPesquisa());
        }
    }

    public void adcionarPagina(Pagina pagina) {
        if (podeInserir(pagina)) {
            verificaAlteracao(pagina);
        } else {
            //lancar exceção;
        }
    }

    public void verificaAlteracao(Pagina pagina) {
        Iterator lPalavras = dicionario.iterator();
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            int relevancia = descobrirRelevancia(palavraAtual, pagina.getConteudo().split(" "));
            if (relevancia > 0) {
                palavraAtual.addNovaPagina(pagina);
            }
        }
    }

    public void removerPagina(Pagina pagina) {
        Iterator lPalavras = dicionario.iterator();
        while (lPalavras.hasNext()) {
            Palavra palavraAtual = (Palavra) lPalavras.next();
            if (palavraAtual.contemPagina(pagina)) {
                palavraAtual.removerPagina(pagina);
            }
        }
        movePagina(pagina);
    }

    public LinkedList pesquisar(String palavrasPesquisadas, String ordem) throws IOException, FileNotFoundException {
        String[] palavrasChaves = validaConteudo(palavrasPesquisadas).split(" ");
        Palavra[] palavras = Palavra.stringToPalavra(palavrasChaves);
        LinkedList noDicionario = new LinkedList();
        LinkedList noArquivo = new LinkedList();
        LinkedList<Comparable> paginasEncontradas;
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
        atualizarDicionario();
        return ordena(paginasEncontradas, ordem);
    }

    private LinkedList ordena(LinkedList paginas, String ordem) {
        Comparable[] arrayPaginas = new Comparable[paginas.size()];
        paginas.toArray(arrayPaginas);
        quick.quickSort(arrayPaginas, 0, arrayPaginas.length - 1);
        LinkedList aux = new LinkedList();
        for (Comparable atual : arrayPaginas) {
            switch (ordem) {
                case "R+":
                case "Maior Relevancia": {
                    aux.addLast(atual);
                    break;
                }
                case "R-":
                case "Menor Relevancia": {
                    aux.addFirst(atual);
                    break;
                }
            }
        }
        return aux;
    }

    private String validaConteudo(String conteudo) {
        return conteudo.replaceAll("[^A-Za-z0-9áàâçaéèêíìîóòôúçñÁÀÂÉÈÊÍÌÓÔÚÇÑ\\s ]", "");
    }

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

    private File criaDiretorio(String nome) {
        File diretorio = new File(nome);
        if (!diretorio.exists()) {
            if (diretorio.mkdir()) {
                //Lançar exeção
            } else {
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

    private int descobrirRelevancia(Palavra palavraChave, String[] palavras) {
        int pEncontradas = 0;
        for (String palavraConteudo : palavras) {
            if (palavraConteudo.equalsIgnoreCase(palavraChave.getPalavra())) {
                pEncontradas++;
            }
        }
        return pEncontradas;
    }

    private int descobrirRelevancia(Palavra[] palavrasChaves, Pagina pagina) {
        int relevanciaAtual = 0;
        String[] palavras = pagina.getConteudo().split(" ");
        for (Palavra palavraChave : palavrasChaves) {
            relevanciaAtual += descobrirRelevancia(palavraChave, palavras);
        }
        return relevanciaAtual;
    }
}
