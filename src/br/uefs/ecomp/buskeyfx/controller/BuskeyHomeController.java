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
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        String linha;
        do {
            linha = buffer.readLine();
            if (linha != null) {
                palavras = linha.split(" ");
                linhas.add(palavras);
            }
        } while (linha != null);
        inputReader.close();
        buffer.close();
        return new Pagina(arquivo.getName(), linhas);
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
                pagina.descobrirRelevancia(palavraChave.getPalavra());
                if (pagina.temRelevancia()) {//Verifica individualmente se a pagina atual tem relevancia para uma determinada palavra.
                    palavraChave.addNovaPagina(pagina.getNome());
                    dicionario.inserir(palavraChave); //Adciona a palavra ao dicionario.
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina.getNome());
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

    public void pesquisar(String palavrasPesquisadas) throws IOException, FileNotFoundException {
        /*Antes de pesquisar deve-se verifiar se as palavras chaves que o usuario digitou
        já foram pesquisadas anteriomente. isto é, verificar no "Dicionario" (estutura de dados)
        se TODAS as palavras existe, caso uma não exista deve-se procurar somente aquelas que
        possue aquela nova palavra juntamente com as demais paginas.
         */
        if ("".equals(palavrasPesquisadas)) {
            //Pode retornar uma exceção. tipo: "campoVazioException".
            System.out.println("Digite algo");
        } else {
            Palavra[] palavras = Palavra.stringToPalavra(palavrasPesquisadas.split(" "));
            LinkedList paginasEncontradas;
            LinkedList noDicionario = new LinkedList();
            LinkedList noArquivo = new LinkedList();
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
            System.out.println(paginasEncontradas.size());
            System.out.println(paginasEncontradas);
        }
    }

    public void carregarDicionario() throws IOException, ClassNotFoundException {
        File dadosDicionario = new File("arquivos\\Dicionario.data");
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
        File dadosDicionario = new File("arquivos\\Dicionario.data");
        if (!dadosDicionario.exists()) {
            dadosDicionario.createNewFile();
        }
        ObjectOutputStream saida;
        saida = new ObjectOutputStream(new FileOutputStream(dadosDicionario));
        saida.writeObject(dicionario);
        saida.close();
    }
}
