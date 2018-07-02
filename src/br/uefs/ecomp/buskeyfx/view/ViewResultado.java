/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.ControllerArquivos;
import br.uefs.ecomp.buskeyfx.controller.ControllerEditar;
import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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
    
    private FacadeBuskeyfx facade;
    private LinkedList paginasEncontradas;
   
    private Parent paginacao;
    private final Tab tabBase;
    
    public ViewResultado(LinkedList resultados, Tab tabBase) {
        facade = FacadeBuskeyfx.getInstance();
        paginasEncontradas = resultados;
        this.tabBase = tabBase;
    }

    public LinkedList getPaginasEncontradas(){
        return paginasEncontradas;
    }
    
    protected BorderPane gerar() {
        int numPaginas = ((paginasEncontradas.size() % 10) != 0) ? paginasEncontradas.size() / 10 + 1 : paginasEncontradas.size() / 10;
        
        BorderPane bpBase = new BorderPane();
        ViewBarraPesquisa bp = new ViewBarraPesquisa();
        bpBase.setTop(bp.gerar());

        Pagination pagination = new Pagination(numPaginas, 0);

        pagination.setPageFactory((Integer pageIndex) -> {
            return this.resultadoPesquisa(pageIndex, numPaginas);
        });

        bpBase.setCenter(pagination);
        paginacao = bpBase;
        return bpBase;
    }
    
    public void atualizarPaginacao(){
       tabBase.setContent(this.gerar());
    }
    private ScrollPane resultadoPesquisa(int pageIndex, int numPaginas) {
        ScrollPane spBase = new ScrollPane();
        spBase.setPadding(new Insets(15, 20, 10, 50));
        VBox vbResultado = new VBox();

        int numPag = pageIndex * 10;
        int limite = (numPag / 10 == numPaginas - 1) ? (paginasEncontradas.size() % 10) : 10;

        for (int i = numPag; i < numPag + limite; i++) {
            try {
                Pagina pagina = (Pagina) paginasEncontradas.get(i);
                pagina.setTextoConteudo(facade.carregaConteudo(new File(pagina.getNome()), true));
                Noticia noticia = new Noticia(paginacao, this);
                vbResultado.getChildren().addAll(noticia.gerarNoticia(pagina), new Separator());
            } catch (IOException ex) {
                Helper.alerta("Erro ao carregar pagina: " + i);
            }
        }

        vbResultado.setAlignment(Pos.CENTER);
        vbResultado.setSpacing(20);
        spBase.setContent(vbResultado);
        return spBase;
    }

}
