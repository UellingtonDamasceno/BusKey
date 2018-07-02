/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.ControllerEditar;
import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author Uellington Damasceno
 */
public class Noticia {
    private final FacadeBuskeyfx facade;
    private ViewResultado viewResultado;
    private final Parent cenaAnterior;
    
    Noticia(Parent cenaAnterior, ViewResultado viewResultado) {
        this.cenaAnterior = cenaAnterior;
        this.viewResultado = viewResultado;
        facade = FacadeBuskeyfx.getInstance();
    }

    protected VBox gerarNoticia(Pagina pagina) {
        VBox vbBaseNoticia = new VBox();
        HBox hbNomeNoticia = new HBox();
        VerNoticia novaNoticia = new VerNoticia(pagina, cenaAnterior);
        
        Label lblNome = new Label(pagina.getNome());
        Label lblAcessos = new Label("Acessos: " + pagina.getAcessos());
        Label lblOcorrência = new Label("Ocorrência: " + pagina.getOcorrencia());
        Label lblPrevia = new Label(pagina.getPrevia());
        
        lblNome.setMaxWidth(150);
        
        hbNomeNoticia.getChildren().addAll(lblNome, extra(pagina));
        hbNomeNoticia.setSpacing(200);
        
        hbNomeNoticia.setOnMouseEntered((Event event) -> {
            hbNomeNoticia.setCursor(Cursor.HAND);
        });
        hbNomeNoticia.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Helper.mudaConteudoTab("Pesquisa", novaNoticia.gerar(false));
            }
        });
        
        vbBaseNoticia.getChildren().addAll(hbNomeNoticia, lblAcessos, lblOcorrência, lblPrevia);
        vbBaseNoticia.setSpacing(5);
        
        return vbBaseNoticia;
    }

    private MenuBar extra(Pagina pagina) {
        MenuBar mbOpcoes = new MenuBar();
        Menu menuMais = new Menu();
        
        menuMais.setGraphic(Helper.imagem("menu.png", 20, 20));
        
        MenuItem menuItemEditar = new MenuItem("Editar");
        MenuItem menuItemDeletar = new MenuItem("Deletar");

        menuItemEditar.setGraphic(Helper.imagem("editar.png", 20, 20));
        menuItemDeletar.setGraphic(Helper.imagem("deletar.png", 20, 20));
        
        
        menuItemEditar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Helper.mudaConteudoTab("Editar", new VerNoticia(pagina, cenaAnterior).gerar(true));
            }
        });
        
        menuItemDeletar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(Helper.alertaConfirma("Deseja apagar: " + pagina.getNome()+ "?")){
                    try {
                        facade.deletaPagina(pagina);
                        viewResultado.getPaginasEncontradas().remove(pagina);
                        viewResultado.atualizarPaginacao();
                    } catch (IOException ex) {
                        Helper.alerta("Erro ao deletar pagina!");
                    }
                }
            }
        });
        
 
        mbOpcoes.getMenus().addAll(menuMais);
        menuMais.getItems().addAll(menuItemEditar, menuItemDeletar);
        return mbOpcoes;
    }
}
