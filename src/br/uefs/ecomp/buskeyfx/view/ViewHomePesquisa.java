/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.ControllerOrdenacao;
import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import java.util.LinkedList;
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

    private final Helper helper;
    private BorderPane homePesquisa;
    private final ComboBox opcoesPesquisas;
    
    private final VerNoticia abaEdicao;
    private final TopK topK;

    
    private final FacadeBuskeyfx facade;
    public ViewHomePesquisa() {
        helper = new Helper();
        facade = FacadeBuskeyfx.getInstance();
        homePesquisa = new BorderPane();
        abaEdicao = new VerNoticia(homePesquisa);
        topK = new TopK(homePesquisa);
        this.opcoesPesquisas = new ComboBox();
    }

    protected BorderPane gerar() {

        VBox centro = new VBox(20);
        HBox acoes = new HBox(5);

        centro.setAlignment(Pos.CENTER);
        acoes.setAlignment(Pos.CENTER);

        TextField txtPesquisa = new TextField();

        Button bntPesquisar = new Button();
        bntPesquisar.setGraphic(Helper.imagem("pesquisar.png", 20, 20));
        homePesquisa.setTop(opcoes());

        txtPesquisa.setMaxWidth(300);
        txtPesquisa.setMinWidth(300);

        bntPesquisar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                helper.pesquisar(txtPesquisa, bntPesquisar, opcoesPesquisas.getValue().toString());
            }
        });

        txtPesquisa.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    helper.pesquisar(txtPesquisa, bntPesquisar, opcoesPesquisas.getValue().toString());
                }
            }

        });
        acoes.getChildren().addAll(txtPesquisa, bntPesquisar);
        centro.getChildren().addAll(Helper.imagem("pesquisar.png", 100, 100), acoes);
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
        listar.setGraphic(Helper.imagem("pesquisar.png", 15, 15));

        listar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (qtd.getText().equals("")) {
                    Helper.alerta("Valor invalido!");
                } else {
                    int k = Integer.parseInt(qtd.getText());

                    LinkedList top = (listar.getId().contains("Pagina")) ? facade.listarTodasPaginas(): facade.listarTodasPalavras();
                    top = facade.ordenar(top, listar.getId());

                    String ordem = (listar.getId().contains("+")) ? "+" : "-";
                    Helper.mudaConteudoTab(listar.getId(), topK.gerarTopK(top, k, ordem));
                }
            }
        });

        base.setPadding(new Insets(0, 5, 0, 10));
        hbBase.getChildren().addAll(qtd, listar);

        base.getChildren().addAll(op, hbBase, new Separator());
        return base;
    }

    private HBox opcoes() {

        HBox base = new HBox(90);
        HBox baseButton = new HBox(20);
        HBox hbOpPesquisa = new HBox(5);

        Label pesquisarPor = new Label("Pesquisar por: ");
        Button sliderVem = new Button();
        Button sliderVai = new Button();
        Button addPagina = new Button();

        addPagina.setGraphic(Helper.imagem("addAba.png", 20, 20));
        addPagina.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Helper.mudaConteudoTab("Nova Pagina", abaEdicao.gerar(true));
            }
        });

        sliderVem.setVisible(false);
        sliderVem.setTooltip(new Tooltip("Fechar slider"));
        sliderVem.setGraphic(Helper.imagem("voltar.png", 20, 20));

        sliderVem.setOnMouseClicked((Event event) -> {
            homePesquisa.setLeft(null);
            sliderVem.setVisible(false);
            sliderVai.setVisible(true);
        });

        sliderVai.setTooltip(new Tooltip("Abrir slider"));
        sliderVai.setGraphic(Helper.imagem("frente.png", 20, 20));
        sliderVai.setOnMouseClicked((Event event) -> {
            homePesquisa.setLeft(lateral());
            sliderVai.setVisible(false);
            sliderVem.setVisible(true);
        });

        opcoesPesquisas.getItems().addAll("Maior Relevancia (R+)", "Menor Relevancia (R-)");
        opcoesPesquisas.setValue("Maior Relevancia (R+)");

        base.setPadding(new Insets(10, 10, 10, 10));
        pesquisarPor.setFont(Font.font("Verdanda", 14));

        hbOpPesquisa.getChildren().addAll(pesquisarPor, opcoesPesquisas, addPagina);
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
        linkGitU.setGraphic(Helper.imagem("github.png", 25, 25));
        linkGitI.setGraphic(Helper.imagem("github.png", 25, 25));
        linkGitU.setFont(Font.font("Verdana", 10));
        linkGitI.setFont(Font.font("Verdana", 10));
        rodape.getChildren().addAll(contexto, linkGitU, linkGitI);

        contexto.setVisible(false);
        linkGitU.setVisible(false);
        linkGitI.setVisible(false);

        linkGitU.setOnMouseEntered((Event event) -> {
            linkGitU.setCursor(Cursor.HAND);
        });
        linkGitI.setOnMouseEntered((Event event) -> {
            linkGitI.setCursor(Cursor.HAND);
        });
        rodape.setOnMouseEntered((Event event) -> {
            contexto.setVisible(true);
            linkGitU.setVisible(true);
            linkGitI.setVisible(true);
        });
        rodape.setOnMouseExited((Event event) -> {
            contexto.setVisible(false);
            linkGitU.setVisible(false);
            linkGitI.setVisible(false);
        });
        return rodape;
    }

}
