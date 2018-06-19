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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.LinkedList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author Uellington Damasceno
 */
public class HomeController implements Initializable {

    @FXML
    private AnchorPane baseHome;
    @FXML
    private Pane BusKeyHome;
    @FXML
    private Button btnPesquisar;
    @FXML
    private TextField txtCampoPesquisa;
    @FXML
    private Button btnCuriosidade;

    private AVLTree dicionario; //estrutura responsavel por armazenar palavras já pesquisada pelo usuario.

    private LinkedList paginasPesquisadas; // Utilzada para armazenar as paginas encontradas durante uma pesquisa;

    private File[] arquivos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dicionario = new AVLTree();
        arquivos = pegarArquivos("arquivos", ".txt");
        // TODO
    }

//    private void carregarDicionario() throws FileNotFoundException, IOException, ClassNotFoundException {
//        File dadosDicionario = new File("Dicionario.data");
//        if (!dadosDicionario.exists()) {
//            dadosDicionario.createNewFile();
//        }
//        if (dadosDicionario.length() > 0) {
//            ObjectInputStream entrada;
//            entrada = new ObjectInputStream(new FileInputStream(dadosDicionario));
//            dicionario = (AVLTree) entrada.readObject();
//            entrada.close();
//        }
//    }
//
//    private void atualizarDicionario() throws FileNotFoundException, IOException {
//        File dadosDicionario = new File("Dicionario.data");
//
//        ObjectOutputStream saida;
//        saida = new ObjectOutputStream(new FileOutputStream(dadosDicionario));
//        saida.writeObject(dicionario);
//        saida.close();
//    }
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
                //System.out.println(Arrays.toString(palavras));
                linhas.add(palavras);
            }
        } while (linha != null);
        inputReader.close();
        buffer.close();
        return new Pagina(arquivo.getName(), linhas);
    }

    /**
     * Método responsavel por verificar se uma determinada linha possue uma palavra chave.
     *
     * @param palavra
     * @return
     */
    /*private boolean possuePalavraChave(String[] palavra){
        return false;
    }*/
    /**
     * Método responsavel por pegar o nome de arquivos de um diretorio seguindo utilizando como filtro a extensão do arquivo.
     *
     * @param caminho Caminho do diretorio o qual se quer a lista de arquivos.
     * @param extensao Extensão utilizada como criterio de filtragem.
     * @return Uma Array que contém os arquivos encontrados.
     */
    private File[] pegarArquivos(String caminho, final String extensao) {
        File diretorio = new File(caminho);
        FileFilter filtro = null;
        if (extensao != null) {
            filtro = (File nomeDoCaminho) -> nomeDoCaminho.getAbsolutePath().endsWith(extensao);
        }
        return diretorio.listFiles(filtro);
    }

    private LinkedList verificaDicionario(Palavra[] palavrasChaves) {
        LinkedList paginasEncontradas = new LinkedList();

        for (Palavra chave : palavrasChaves) {
            System.out.println(chave);
            if (dicionario.contem(chave)) {
                paginasEncontradas.addAll(((Palavra) dicionario.buscarPalavra(chave)).getPaginas());
            }
        }
        return paginasEncontradas;
    }

    private LinkedList procurarNosArquivos(File[] arquivos, Palavra[] palavrasChaves) throws IOException {
        LinkedList paginasEncontradas = new LinkedList();
        Pagina pagina;
        for (File arquivo : arquivos) {
            pagina = carregarPagina(arquivo);
            pagina.descobrirMultRelevancia(Palavra.palavraToString(palavrasChaves)); //Calcula a relevancia da pagina atual com base na atual palavra chave pesquisada;
            if (pagina.temRelevancia()) {
                for (Palavra chave : palavrasChaves) {
                    chave.addNovaPagina(pagina.getNome());
                    dicionario.inserir(chave); //Adciona a palavra ao dicionario.
                    if (!paginasEncontradas.contains(pagina)) {
                        paginasEncontradas.add(pagina.getNome());
                    }
                }
            }
        }
        //System.out.println(dicionario);
        return paginasEncontradas;
    }

    @FXML
    private void pesquisar(MouseEvent event) throws IOException, FileNotFoundException, ClassNotFoundException {
        /*Antes de pesquisar deve-se verifiar se as palavras chaves que o usuario digitou
        já foram pesquisadas anteriomente. isto é, verificar no "Dicionario" (estutura de dados)
        se TODAS as palavras existe, caso uma não exista deve-se procurar somente aquelas que
        possue aquela nova palavra juntamente com as demais paginas.
         */
        String palavrasPesquisadas = txtCampoPesquisa.getText();
        if ("".equals(palavrasPesquisadas)) {
            System.out.println("Digite algo");
        } else {
            LinkedList paginasEncontradas;
            Palavra[] palavrasChaves;
            palavrasChaves = Palavra.stringToPalavra(palavrasPesquisadas.split(" "));
            paginasEncontradas = verificaDicionario(palavrasChaves);
            if (!paginasEncontradas.isEmpty()) {
                paginasPesquisadas = paginasEncontradas;
                System.out.println("Palavra Estava no dicionario");
            } else {
                paginasEncontradas = procurarNosArquivos(arquivos, palavrasChaves);
                if (paginasEncontradas.isEmpty()) {
                    System.out.println("Palavra não encontrada//Mensagem");
                } else {
                    paginasPesquisadas = paginasEncontradas;
                    System.out.println("Palavra Estava no arquivo");
                }
            }
            System.out.println(paginasEncontradas.size());
            System.out.println(paginasEncontradas);
        }
    }
    //Necessario trabalhar melhor a interação entre strig e as instancias da classe palavra.
    //Durante a pesquisa na arvore é necessario verificar se todas palavras foram encontradas;
    //pois pode acontecer de que em algum momento 2/3 palavras exista na arvore e por esse motiov 
    //é possivel que não apareca todas as paginas pesquisadas. 
    /*
     * - Adcionar
     * No método de adcionar nova pagina só será necessario verificar no "dicionario" se aquela pagina tem relevancia 
     * para aquela palavra (isso para todas as palavras do dicionario).
     * --Caso tenha é so inserir essa pagina. 
     * --Caso contrario é só inserir na lista de arquivos existentes. 
     * 
     * - Editar 
     * Verificar todas palavras do dicionario verificando a relevancia. 
     * -- Se se não tiver relevancia é so tirar. 
     * -- se tiver é so continuar do mesmo jeito. 
     * 
     * - Apagar
     * Verificar todas palavras do "dicionario" e ir retirando a pagina a ser apagada da lista de 
     * paginas que possue aquela palavra. 
     */
}
