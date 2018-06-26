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
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewHomePesquisa {

    ViewHelper helper;
    BorderPane homePesquisa;
    private final BuskeyPesquisaController CONTROLLER = new BuskeyPesquisaController();

    public ViewHomePesquisa(ViewHelper helper) {
        homePesquisa = new BorderPane();
        this.helper = helper;
    }

    protected BorderPane gerar() {

        VBox centro = new VBox(20);
        HBox acoes = new HBox(5);

        centro.setAlignment(Pos.CENTER);
        acoes.setAlignment(Pos.CENTER);

        TextField txtPesquisa = new TextField();

        Button pesquisar = helper.createButton("", "pesquisar.png", 50, 20);

        homePesquisa.setTop(opcoes());

        txtPesquisa.setMaxWidth(300);
        txtPesquisa.setMinWidth(300);
//        curiosidade.setMinWidth(80);
//        pesquisar.setMinWidth(80);

        pesquisar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helper.pesquisar(CONTROLLER, txtPesquisa.getText());
            }
        });

        acoes.getChildren().addAll(txtPesquisa, pesquisar);
        centro.getChildren().addAll(helper.imagem("pesquisar.png", 100, 100), acoes);
        homePesquisa.setCenter(centro);
        //homePesquisa.setLeft(lateral());
        //homePesquisa.getLeft().setVisible(false);

        return homePesquisa;
    }

    private VBox lateral() {
        VBox base = new VBox(10);
        Label titulo = new Label("Top K!");
        titulo.setFont(Font.font("Verdana", 20));

        base.setAlignment(Pos.TOP_CENTER);

        base.getChildren().add(titulo);
        base.getChildren().add(opLateral("Palavras + B"));
        base.getChildren().add(opLateral("Palavras - B"));
        base.getChildren().add(opLateral("Paginas + A"));
        base.getChildren().add(opLateral("Paginas - A"));

        return base;
    }

    private VBox opLateral(String opcao) {
        VBox base = new VBox(5);
        HBox hbBase = new HBox(10);

        Label op = new Label(opcao);
        TextField qtd = new TextField();
        qtd.setTooltip(new Tooltip("Valor de 'K'"));
        qtd.setMaxWidth(50);

        Button listar = new Button();
        listar.setGraphic(helper.imagem("pesquisar.png", 15, 15));
        base.setPadding(new Insets(0, 5, 0, 10));
        hbBase.getChildren().addAll(qtd, listar);

        base.getChildren().addAll(op, hbBase, new Separator());
        return base;
    }

    private HBox opcoes() {
        
        HBox base = new HBox(160);
        HBox baseButton = new HBox(5);
        HBox hbOpPesquisa = new HBox(5);
        
        Label pesquisarPor = new Label("Pesquisar por: ");
        ComboBox opcoesPesquisas = new ComboBox();
        Button sliderVem = new Button();
        Button sliderVai = new Button();
        
        sliderVem.setVisible(false);
        sliderVem.setTooltip(new Tooltip("Top - K!"));
        sliderVem.setGraphic(helper.imagem("voltar.png", 20, 20));
        sliderVem.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                homePesquisa.setLeft(null);
                sliderVem.setVisible(false);
                sliderVai.setVisible(true);
            }
        });
        
        sliderVai.setTooltip(new Tooltip("Top - K!"));
        sliderVai.setGraphic(helper.imagem("frente.png", 20, 20));
        sliderVai.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                homePesquisa.setLeft(lateral());
                sliderVai.setVisible(false);
                sliderVem.setVisible(true);
            }
        });

        opcoesPesquisas.getItems().addAll("Maior Relevancia",
                "Menor Relevancia",
                "Mais Acessos",
                "Menos Acessos");
        opcoesPesquisas.setValue("Maior Relevancia");

        base.setPadding(new Insets(10, 10, 10, 10));
        pesquisarPor.setFont(Font.font("Verdanda", 14));

        hbOpPesquisa.getChildren().addAll(pesquisarPor, opcoesPesquisas);
        hbOpPesquisa.setAlignment(Pos.CENTER_RIGHT);
        baseButton.getChildren().addAll(sliderVai, sliderVem);
        base.getChildren().addAll(baseButton, hbOpPesquisa);
        return base;
    }
}
