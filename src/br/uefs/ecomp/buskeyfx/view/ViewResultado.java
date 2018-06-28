/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewResultado {

    private LinkedList resultados;
    private final ViewHelper helper;

    public ViewResultado(LinkedList resultados, ViewHelper helper) {
        this.resultados = resultados;
        this.helper = helper;
    }

    protected BorderPane createPagination() {
        int numPaginas = ((resultados.size() % 10) != 0) ? resultados.size() / 10 + 1 : resultados.size() / 10;

        BorderPane base = new BorderPane();

        base.setTop(new ViewBarraPesquisa(helper).gerar());

        Pagination pagination = new Pagination(numPaginas, 0);

        pagination.setPageFactory((Integer pageIndex) -> {
            System.out.println(pageIndex);
            return this.resultadoPesquisa(pageIndex, numPaginas);
        });

        base.setCenter(pagination);
        return base;
    }

    private ScrollPane resultadoPesquisa(int pageIndex, int numPaginas) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(15, 20, 10, 50));
        VBox vboxResultado = new VBox();

        int numPag = pageIndex * 10;
        int limite = (numPag / 10 == numPaginas - 1) ? (resultados.size() % 10) : 10;

        for (int i = numPag; i < numPag + limite; i++) {
            try {
                Pagina pagina = (Pagina) resultados.get(i);
                File arquivo = new File("arquivos\\" + pagina.getNome());
                pagina.setConteudo(helper.getController().carregaConteudo(arquivo));
                Noticia noticia = new Noticia(helper);
                vboxResultado.getChildren().addAll(noticia.gerarNoticia(pagina), new Separator());
            } catch (IOException ex) {
                helper.alerta("Erro ao carregar pagina: " + i);
            }
        }

        vboxResultado.setAlignment(Pos.CENTER);
        vboxResultado.setSpacing(20);
        scrollPane.setContent(vboxResultado);
        return scrollPane;
    }

}
