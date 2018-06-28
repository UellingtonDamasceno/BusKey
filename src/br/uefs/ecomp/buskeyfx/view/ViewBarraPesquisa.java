/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Uellington Damasceno
 */
class ViewBarraPesquisa {
 
    private ViewHelper helper;

    public ViewBarraPesquisa(ViewHelper helper) {
        this.helper = helper;
    }

    protected HBox gerar() {
        HBox pesquisa = new HBox();

        Button voltar = new Button();
        Button frente = new Button();
        Button home = new Button();
        Button pesquisar = new Button();
        Button adcionarAba = new Button();
        ComboBox ordem = new ComboBox();
        ordem.setMinHeight(25);
        ordem.setMinWidth(30);
        ordem.getItems().addAll("R+", "R-");
        ordem.setValue("R+");
        ordem.setTooltip(new Tooltip("Ordem de relevancia"));
        
        TextField barraPesquisa = new TextField();
        
        pesquisa.setPadding(new Insets(5, 5, 5, 5));
        pesquisa.setSpacing(10);
        pesquisa.setAlignment(Pos.CENTER_LEFT);
        
        barraPesquisa.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    helper.pesquisar(barraPesquisa.getText(), ordem.getValue().toString());
                }
            }
            
        });
        
        voltar.setOnAction((ActionEvent event) -> {
            helper.getController().imprimeArvore();
        });
        
        pesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.pesquisar(barraPesquisa.getText(), ordem.getValue().toString());
            }
        });
        
        home.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.getTabAtual("Home").setContent(new ViewHomePesquisa(helper).gerar());
            }
        });
        adcionarAba.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.addAba("Home").setContent(new ViewHomePesquisa(helper).gerar());
            }
        });

        pesquisar.setGraphic(helper.imagem("pesquisar.png", 20, 20));
        frente.setGraphic(helper.imagem("frente.png", 20, 20));
        home.setGraphic(helper.imagem("home.png", 20, 20));
        voltar.setGraphic(helper.imagem("voltar.png", 20, 20));
        adcionarAba.setGraphic(helper.imagem("add.png", 20, 20));

        pesquisar.setTooltip(new Tooltip("Pesquisar"));
        frente.setTooltip(new Tooltip("Em desenvolvimento"));
        home.setTooltip(new Tooltip("Página inicial"));
        voltar.setTooltip(new Tooltip("Em desenvolvimento"));
        adcionarAba.setTooltip(new Tooltip("Nova aba"));

        pesquisa.getChildren().addAll(helper.imagem("B.png", 20, 20), voltar, frente, home, barraPesquisa, pesquisar, ordem, adcionarAba);
        return pesquisa;
    }
}
