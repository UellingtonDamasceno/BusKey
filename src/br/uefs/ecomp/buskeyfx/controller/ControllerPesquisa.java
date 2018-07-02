package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.model.*;
import br.uefs.ecomp.buskeyfx.util.AVLTree;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

/**
 *
 * @author Uellington Damasceno
 */
public class ControllerPesquisa {

    private static boolean estaPesquisando;
    LinkedList noDicionario = new LinkedList();
    LinkedList noArquivo = new LinkedList();

    public ControllerPesquisa() {
        noDicionario = new LinkedList();
        noArquivo = new LinkedList();
    }

    public LinkedList pesquisar(String palavrasPesquisadas, ControllerArvore ca, ControllerArquivos carq) throws IOException, FileNotFoundException {
        estaPesquisando = true;
        
        File[] arquivos = carq.getArquivos();
        AVLTree dicionario = ca.getDicionario();
        Palavra[] palavras = filtraPalavras(palavrasPesquisadas, carq);
        LinkedList<Pagina> paginasEncontradas;
        
        int ondeProcurar = direcionador(palavras, noDicionario, noArquivo, dicionario);
        switch (ondeProcurar) {
            case -1: {
                paginasEncontradas = procurarNosArquivos(arquivos, palavras, carq, ca);
                System.out.println("No arquivo");
                break;
            }
            case 1: {
                paginasEncontradas = procurarNoDicionario(dicionario, palavras);
                System.out.println("No diconario");
                break;
            }
            default: {
                Palavra[] palavrasNoArquivo = (Palavra[]) noArquivo.toArray(new Palavra[noArquivo.size()]);
                Palavra[] palavrasNoDicionario = (Palavra[]) noDicionario.toArray(new Palavra[noDicionario.size()]);

                paginasEncontradas = procurarNosArquivos(arquivos, palavrasNoArquivo, carq, ca);
                paginasEncontradas.addAll(procurarNoDicionario(dicionario, palavrasNoDicionario));
                break;
            }
        }
        estaPesquisando = false;
        return paginasEncontradas;
    }

    private LinkedList procurarNoDicionario(AVLTree dicionario, Palavra[] palavrasChaves) {
        LinkedList<Pagina> paginasEncontradas = new LinkedList();

        for (Palavra palavraChave : palavrasChaves) {
            Palavra palavraEncontrada = ((Palavra) dicionario.buscarPalavra(palavraChave));
            palavraEncontrada.addPesquisa();
            for (Pagina paginaAtual : palavraEncontrada.getListaPagina()) {
                if (!paginasEncontradas.contains(paginaAtual)) {
                    paginasEncontradas.add(paginaAtual);
                }
            }

        }
        return paginasEncontradas;
    }

    private Palavra[] filtraPalavras(String palavrasPesquisadas, ControllerArquivos ca) {
        palavrasPesquisadas = palavrasPesquisadas.toUpperCase();
        String[] palavrasChaves = ca.validaConteudo(palavrasPesquisadas).split(" ");
        LinkedList aux = new LinkedList();
        for (String aComparar : palavrasChaves) {
            if (!aux.contains(aComparar)) {
                aux.add(aComparar);
            }
        }
        return Palavra.stringToPalavra((String[]) aux.toArray(new String[aux.size()]));
    }

    private LinkedList procurarNosArquivos(File[] arquivos, Palavra[] palavrasChaves, ControllerArquivos ca, ControllerArvore car) throws IOException {
        LinkedList<Pagina> paginasEncontradas = new LinkedList();
        Pagina pagina;

        car.inserirMultPalavras(palavrasChaves);
        for (File arquivo : arquivos) {
            String conteudo = ca.carregaConteudo(arquivo, true);
            String[] palavras = conteudo.split(" ");
            for (Palavra palavraChave : palavrasChaves) {
                int relevancia = descobrirRelevancia(palavraChave, palavras); //Indica a relevancia da pagina para um das palavras pesquisadas.
                if (relevancia > 0) {
                    pagina = new Pagina(arquivo.toString(), conteudo);
                    Palavra palavraEncontrada = (Palavra) car.getDicionario().buscarPalavra(palavraChave);
                    palavraEncontrada.addNovaPagina(pagina);
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina);
                    }
                    pagina.setOcorrencia(descobrirRelevancia(palavrasChaves, pagina)); //Indica a relevancia da pagina para todas as palavras pesquisadas.
                }
            }
        }
        return paginasEncontradas;
    }

    //Método auxiliar que indica onde a busca pelas palavras pesquisadas devem acontecer.
    private int direcionador(Palavra[] palavrasChaves, LinkedList dic, LinkedList arq, AVLTree dicionario) {
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

    protected int descobrirRelevancia(Palavra palavraChave, String[] palavras) {
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
        String[] palavras = pagina.getTextoConteudo().split(" ");
        for (Palavra palavraChave : palavrasChaves) {
            relevanciaAtual += descobrirRelevancia(palavraChave, palavras);
        }
        return relevanciaAtual;
    }
}
