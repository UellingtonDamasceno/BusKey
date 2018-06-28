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
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewHomePesquisa {

    ViewHelper helper;
    BorderPane homePesquisa;
    ComboBox opcoesPesquisas;

    public ViewHomePesquisa(ViewHelper helper) {
        homePesquisa = new BorderPane();
        this.opcoesPesquisas = new ComboBox();
        this.helper = helper;
    }

    protected BorderPane gerar() {

        VBox centro = new VBox(20);
        HBox acoes = new HBox(5);

        centro.setAlignment(Pos.CENTER);
        acoes.setAlignment(Pos.CENTER);

        TextField txtPesquisa = new TextField();

        Button pesquisar = new Button();
        pesquisar.setGraphic(helper.imagem("pesquisar.png", 20, 20));
        homePesquisa.setTop(opcoes());

        txtPesquisa.setMaxWidth(300);
        txtPesquisa.setMinWidth(300);

        pesquisar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helper.pesquisar(txtPesquisa.getText(), opcoesPesquisas.getValue().toString());
            }
        });

        txtPesquisa.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    helper.pesquisar(txtPesquisa.getText(), opcoesPesquisas.getValue().toString());
                }
            }

        });
        acoes.getChildren().addAll(txtPesquisa, pesquisar);
        centro.getChildren().addAll(helper.imagem("pesquisar.png", 100, 100), acoes);
        homePesquisa.setCenter(centro);
        homePesquisa.setBottom(rodape());
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
        listar.setId(opcao);
        listar.setGraphic(helper.imagem("pesquisar.png", 15, 15));

        listar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println(qtd.getText());
                System.out.println(listar.getId());
            }
        });

        base.setPadding(new Insets(0, 5, 0, 10));
        hbBase.getChildren().addAll(qtd, listar);

        base.getChildren().addAll(op, hbBase, new Separator());
        return base;
    }

    private HBox opcoes() {

        HBox base = new HBox(160);
        HBox baseButton = new HBox(20);
        HBox hbOpPesquisa = new HBox(5);

        Label pesquisarPor = new Label("Pesquisar por: ");
        Button sliderVem = new Button();
        Button sliderVai = new Button();

        sliderVem.setVisible(false);
        sliderVem.setTooltip(new Tooltip("Fechar slider"));
        sliderVem.setGraphic(helper.imagem("voltar.png", 20, 20));

        sliderVem.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                homePesquisa.setLeft(null);
                sliderVem.setVisible(false);
                sliderVai.setVisible(true);
            }
        });

        sliderVai.setTooltip(new Tooltip("Abrir slider"));
        sliderVai.setGraphic(helper.imagem("frente.png", 20, 20));
        sliderVai.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                homePesquisa.setLeft(lateral());
                sliderVai.setVisible(false);
                sliderVem.setVisible(true);
            }
        });

        opcoesPesquisas.getItems().addAll("Maior Relevancia", "Menor Relevancia");
        opcoesPesquisas.setValue("Maior Relevancia");

        base.setPadding(new Insets(10, 10, 10, 10));
        pesquisarPor.setFont(Font.font("Verdanda", 14));

        hbOpPesquisa.getChildren().addAll(pesquisarPor, opcoesPesquisas);
        hbOpPesquisa.setAlignment(Pos.CENTER_RIGHT);
        baseButton.getChildren().addAll(sliderVai, sliderVem);
        base.getChildren().addAll(baseButton, hbOpPesquisa);
        return base;
    }

    private VBox rodape() {

        VBox rodape = new VBox();
        rodape.setPadding(new Insets(0, 0, 20, 0));
        rodape.setAlignment(Pos.CENTER);

        Label contexto = new Label("Desenvolvido por: ");
        Label linkGitU = new Label("https://github.com/UellingtonDamasceno");
        Label linkGitI = new Label("https://github.com/invanildo99gomes");
        linkGitU.setGraphic(helper.imagem("github.png", 25, 25));
        linkGitI.setGraphic(helper.imagem("github.png", 25, 25));
        linkGitU.setFont(Font.font("Verdana", 10));
        linkGitI.setFont(Font.font("Verdana", 10));
        rodape.getChildren().addAll(contexto, linkGitU, linkGitI);

        contexto.setVisible(false);
        linkGitU.setVisible(false);
        linkGitI.setVisible(false);

        linkGitU.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                linkGitU.setCursor(Cursor.HAND);
            }
        });
        linkGitI.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                linkGitI.setCursor(Cursor.HAND);
            }
        });
        rodape.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                contexto.setVisible(true);
                linkGitU.setVisible(true);
                linkGitI.setVisible(true);
            }
        });
        rodape.setOnMouseExited(new EventHandler() {
            @Override
            public void handle(Event event) {
                contexto.setVisible(false);
                linkGitU.setVisible(false);
                linkGitI.setVisible(false);
            }
        });
        return rodape;
    }

}
