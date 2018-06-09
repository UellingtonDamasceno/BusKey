/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.controller;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
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
    
    private LinkedList<Pagina> lista;
    
    public Pagina pagina;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.lista = new LinkedList<>();
        // TODO
    }

    private void carregarPaginas(File[] arquivos) throws FileNotFoundException, IOException {
        FileInputStream file;
        InputStreamReader inputReader;
        BufferedReader buffer;
        LinkedList linhas = new LinkedList();
        String[] palavras;
        for (File arquivo : arquivos) {
            file = new FileInputStream(arquivo);
            inputReader = new InputStreamReader(file);
            buffer = new BufferedReader(inputReader);
            String linha;
            do {
                linha = buffer.readLine();
                if (linha != null) {
                    palavras = linha.split(" ");
                    linhas.add(palavras);
                }
            } while (linha != null);
            pagina = new Pagina(arquivos[0].getName(), linhas);
            lista.add(pagina);
            file.close();
            inputReader.close();
            buffer.close();
        }
    }

    /*private void validaPalavra(String palavra){
        char c = palavra;
        for(int i = 0; c != null; i++){
            
        }
    }*/
    /**
     * Método responsavel por pegar o nome de arquivos de um diretorio seguindo utilizando como filtro a extensão do arquivo.
     *
     * @param caminho Caminho do diretorio o qual se quer a lista de arquivos.
     * @param extensao Extensão utilizada como criterio de filtragem.
     * @return Uma Array que contém os arquivos encontrados.
     */
    private static File[] pegarArquivos(String caminho, final String extensao) {
        File dir = new File(caminho);
        // filtro pela extensão procurada
        FileFilter filter = null;
        if (extensao != null) {
            filter = (File nomeDoCaminho) -> nomeDoCaminho.getAbsolutePath().endsWith(extensao);
        }
        // lista os arquivos que correspondem ao match
        return dir.listFiles(filter);
    }

    @FXML
    private void pesquisar(MouseEvent event) throws IOException {
        /*Antes de pesquisar deve-se verifiar se as palavras chaves que o usuario digitou
        já foram pesquisadas anteriomente. isto é, verificar no "Dicionario" (estutura de dados)
        se TODAS as palavras existe, caso uma não exista deve-se procurar somente aquelas que
        possue aquela nova palavra juntamente com as demais paginas.
         */
        String conteudo = txtCampoPesquisa.getText();
        if ("".equals(conteudo)) {
            // Pode ser exibido algum tipo de mensagem pedido para que o usuario digite alguma palavra ou
            // Pode-se deixar sem acontecer nada. 
            System.out.println("Digite algo");
        } else {
            String[] palavrasChaves = conteudo.split(" ");
            System.out.println(Arrays.toString(palavrasChaves));
            carregarPaginas(pegarArquivos("arquivos", ".txt"));
            System.out.println(pagina);
            pagina.descobrirRelevancia(palavrasChaves);
            if (pagina.temRelevancia()) {
                System.out.println(pagina.getRelevancia());
            } else {
                System.out.println("Nenhuma palavra foi encontrada");
            }
        }
    }

}
