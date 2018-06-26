/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyPesquisaController;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.IOException;
import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewResultado {

    private LinkedList resultados;
    private final BuskeyPesquisaController CONTROLLER = new BuskeyPesquisaController();
    private final ViewHelper helper;
    
    public ViewResultado(LinkedList resultados, ViewHelper helper) {
        this.resultados = resultados;
        this.helper = helper;
    }

    protected BorderPane createPagination() {
        BorderPane base = new BorderPane();
        
        base.setTop(new ViewBarraPesquisa(helper).gerar());
        
        Pagination pagination = new Pagination(resultados.size() / 10, 0);
        
        pagination.setPageFactory((Integer pageIndex) -> {
            return this.resultadoPesquisa(pageIndex);
        });
        
        base.setCenter(pagination);
        return base;
    }

    private ScrollPane resultadoPesquisa(int pageIndex) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(15, 20, 10, 50));
        VBox vboxResultado = new VBox();
        
        int numPag = pageIndex * 10;
        for (int i = numPag; i < numPag + 10; i++) {
            try {
                Pagina pagina = CONTROLLER.carregarPagina(i, resultados);
                Noticia noticia = new Noticia(pagina, helper);
                vboxResultado.getChildren().addAll(noticia.gerarNoticia(), new Separator());
            } catch (IOException e) {
                helper.alerta("Erro ao carregar Pagina: "+ i);
            }
        }
        
        vboxResultado.setAlignment(Pos.CENTER);
        vboxResultado.setSpacing(20);
        scrollPane.setContent(vboxResultado);
        return scrollPane;
    }

}
