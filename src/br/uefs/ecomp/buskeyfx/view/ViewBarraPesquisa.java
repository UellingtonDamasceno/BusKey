/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyPesquisaController;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

/**
 *
 * @author Uellington Damasceno
 */
class ViewBarraPesquisa {

    private ViewHelper helper;
    private final BuskeyPesquisaController CONTROLLER = new BuskeyPesquisaController();

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

        TextField barraPesquisa = new TextField();
        Label logo = new Label("BusKey");
        logo.setFont(Font.font("Verdana", FontPosture.REGULAR, 20));

        pesquisa.setPadding(new Insets(5, 5, 5, 5));
        pesquisa.setSpacing(10);
        pesquisa.setAlignment(Pos.CENTER_LEFT);

        voltar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                CONTROLLER.imprimeArvore();
            }
        });
        pesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                helper.pesquisar(CONTROLLER, barraPesquisa.getText());
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

        pesquisa.getChildren().addAll(logo, voltar, frente, home, barraPesquisa, pesquisar, adcionarAba);
        return pesquisa;
    }
}
