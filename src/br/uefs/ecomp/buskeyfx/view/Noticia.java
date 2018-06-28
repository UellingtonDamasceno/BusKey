/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
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

    private ViewHelper helper;
    
    Noticia(ViewHelper helper) {
        this.helper = helper;
    }

    protected VBox gerarNoticia(Pagina pagina) {
        
        VBox baseNoticia = new VBox();
        HBox nomeNoticia = new HBox();
        VerNoticia novaNoticia = new VerNoticia(pagina, helper);
        Label nome = new Label(pagina.getNome());
        Label acessos = new Label("Acessos: " + pagina.getAcessos());
        Label relevancia = new Label("Relevância: " + pagina.getRelevancia());
        
        nome.setMaxWidth(150);
        nome.setId(pagina.getEndereco());
        
        nomeNoticia.setOnMouseEntered((Event event) -> {
            nomeNoticia.setCursor(Cursor.HAND);
        });
        nomeNoticia.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                helper.getTabAtual("Pesquisa: " + pagina.getNome()).setContent(novaNoticia.gerar(false));
            }
        });
        nomeNoticia.getChildren().addAll(nome, extra(pagina));
        nomeNoticia.setSpacing(200);
        Label prev = new Label(pagina.getPrevia());
        baseNoticia.getChildren().addAll(nomeNoticia, acessos, relevancia, prev);
        baseNoticia.setSpacing(5);
        return baseNoticia;
    }

    private MenuBar extra(Pagina pagina) {
        MenuBar ops = new MenuBar();
        Menu menubar = new Menu();
        menubar.setGraphic(helper.imagem("menu.png", 20, 20));
        MenuItem editar = new MenuItem("Editar");
        MenuItem deletar = new MenuItem("Deletar");
        MenuItem outraAba = new MenuItem("Abrir em nova aba");

        editar.setGraphic(helper.imagem("editar.png", 20, 20));
        deletar.setGraphic(helper.imagem("deletar.png", 20, 20));
        outraAba.setGraphic(helper.imagem("addAba.png", 20, 20));
        
        editar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.addAba(pagina.getNome()).setContent(new VerNoticia(pagina, helper).gerar(true));
            }
        });
        deletar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(helper.alertaConfirma("Deseja apagar: " + pagina.getNome()+ "?")){
                    //CONTROLLER.removePagina(pagina);
                }
            }
        });
        outraAba.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.addAba("Pesquisa").setContent(new VerNoticia(pagina, helper).gerar(false));
            }
        });

        ops.getMenus().addAll(menubar);
        menubar.getItems().addAll(editar, deletar, outraAba);
        return ops;
    }
}
