/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyPesquisaController;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewHelper {

    private TabPane tabPane;
    private final BuskeyPesquisaController CONTROLLER;

    public ViewHelper(TabPane tabPane, BuskeyPesquisaController CONTROLLER) {
        this.tabPane = tabPane;
        this.CONTROLLER = CONTROLLER;
    }

    public BuskeyPesquisaController getController() {
        return CONTROLLER;
    }

    void pesquisar(String aPesquisar, String ordem) {
        try {
            if (aPesquisar.equals("")) {
                alerta("Digite algo para ser pesquisado!");
            } else {
                LinkedList resultado = CONTROLLER.pesquisar(aPesquisar, ordem);
                if (!resultado.isEmpty()) {
                    getTabAtual(aPesquisar).setContent(new ViewResultado(resultado, this).createPagination());
                } else {
                    alerta("Palavra não foi encontrada");
                }
            }
        } catch (IOException ex) {
            alerta(ex.toString());
        }
    }

    protected Tab addAba(String titulo) {
        Tab tab = new Tab();
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        BorderPane borderPane = new BorderPane();
        tab.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (tabPane.getTabs().size() == 1) {
                    alerta("Impossivel Fechar!");
                    event.consume();
                }
            }
        });
        tab.setText(titulo);

        selectionModel.select(tab);
        tab.setContent(borderPane);
        tabPane.getTabs().add(tab);
        return tab;
    }

    protected Tab getTabAtual(String titulo) {
        for (Tab tabAtual : tabPane.getTabs()) {
            if (tabAtual.isSelected()) {
                tabAtual.setText(titulo);
                return tabAtual;
            }
        }
        return null;
    }

    protected void alerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Alerta!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    protected boolean alertaConfirma(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confriamação");
        alert.setHeaderText("Tem certeza?");
        alert.setContentText(mensagem);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    protected ImageView imagem(String nome, double altura, double largura) {
        String url = "/br/uefs/ecomp/buskeyfx/imagens/";
        ImageView icon = new ImageView(new Image(url + nome));
        icon.setFitHeight(altura);
        icon.setFitWidth(largura);
        return icon;
    }

    protected Button createButton(String texto, String url, double x, double y) {
        Button bnt = new Button(texto);
        bnt.setMaxWidth(x);
        bnt.setMinHeight(y);
        bnt.setGraphic(imagem(url, x / 3, y));
        return bnt;
    }
}
