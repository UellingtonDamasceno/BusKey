/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Uellington Damasceno
 */
public class TopK {

    private final Parent cenaAnterior;
    private String ordem;
    
    public TopK(Parent cenaAnterior) {
        this.cenaAnterior = cenaAnterior;
    }

    protected BorderPane gerarTopK(LinkedList listaGeral, int limite, String ordem) {
        BorderPane bpBase = new BorderPane();
        ScrollPane spBaseTable;
        this.ordem = ordem;
    
        Button bntVoltar = new Button();
        bntVoltar.setGraphic(Helper.imagem("voltar.png", 20, 20));
        bntVoltar.setOnAction((ActionEvent event) -> {
            Helper.mudaConteudoTab("Home", cenaAnterior);
        });

        spBaseTable = (listaGeral.get(0) instanceof Pagina) ? topKPaginas(tiraTop(listaGeral, limite)) : topKPalavras(tiraTop(listaGeral, limite));

        bpBase.setCenter(spBaseTable);
        bpBase.setLeft(bntVoltar);
        return bpBase;
    }

    private ScrollPane topKPaginas(LinkedList listaPaginas) {
        return gerarTabela(new TableView<>(), "Pagina", "Acessos", "nome", "acessos", listaPaginas);
    }

    private ScrollPane topKPalavras(LinkedList listaPalavras) {
        return gerarTabela(new TableView<>(), "Palavra", "Buscas", "palavra", "pesquisa", listaPalavras);
    }

    private ScrollPane gerarTabela(TableView tabela, String c1T, String c2T, String c1C, String c2C, LinkedList l) {
        ScrollPane spBaseTabela = new ScrollPane();

        TableColumn coluna1Titulo = new TableColumn(c1T);
        TableColumn coluna2Titulo = new TableColumn(c2T);
        
        coluna1Titulo.setCellValueFactory(new PropertyValueFactory<>(c1C));
        coluna2Titulo.setCellValueFactory(new PropertyValueFactory<>(c2C));
       
        SortType sort = (ordem.equals("-")) ? TableColumn.SortType.ASCENDING : TableColumn.SortType.DESCENDING;
        System.out.println(sort);
        coluna2Titulo.setSortType(sort);
        
        ObservableList conteudo = FXCollections.observableArrayList(l);
        
        tabela.setItems(conteudo);
        tabela.getColumns().addAll(coluna1Titulo, coluna2Titulo);
        spBaseTabela.setContent(tabela);
        return spBaseTabela;
    }

    private LinkedList tiraTop(LinkedList top, int limite) {
        limite = (limite <= 0) ? 10 : limite; //Impede que o usuario digite um valor negativo ou igual a zero. 
        limite = (top.size() > limite) ? limite : top.size(); // Caso o numero do Top-K seja maior do que o tamanho da lista.
        LinkedList aux = new LinkedList();
        for (int i = 0; i < limite; i++) {
            aux.add(top.get(i));
        }
        return aux;
    }

}
