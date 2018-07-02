/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.ControllerPesquisa;
import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author Uellington Damasceno
 */
public class Helper {

    private static TabPane tabPane;
    FacadeBuskeyfx facade;

    public Helper() {
        if (tabPane == null) {
            System.out.println("Criou tabPane");
            tabPane = new TabPane();
            addTabAdd();
        }
        facade = FacadeBuskeyfx.getInstance();
    }

    protected Scene gerar() {
        return new Scene(tabPane, 500, 450);
    }

    void pesquisar(TextField txtBPesquisa, Button bntPesquisar, String ordem) {
        if (txtBPesquisa.getText().equals("")) {
            alerta("Digite algo para ser pesquisado!");
        } else {
            Runnable pesquisar = () -> {
                Tab tabPesquisa = getTabAtual();
                Platform.runLater(() -> {
                    txtBPesquisa.setDisable(true);
                    bntPesquisar.setDisable(true);
                    tabPesquisa.setClosable(false);
                    tabPesquisa.setGraphic(imagem("gif-load.gif", 15, 15));
                });
                LinkedList resultado;
                try {
                    resultado = facade.pesquisar(txtBPesquisa.getText(), ordem);
                    if (!resultado.isEmpty()) {
                        Platform.runLater(() -> {
                            tabPesquisa.setText(txtBPesquisa.getText());
                            tabPesquisa.setGraphic(null);
                            tabPesquisa.setClosable(true);
                            txtBPesquisa.setDisable(false);
                            bntPesquisar.setDisable(false);
                            tabPesquisa.setContent(new ViewResultado(resultado, tabPesquisa).gerar());
                        });
                    } else {
                        Platform.runLater(() -> {
                            tabPesquisa.setGraphic(null);
                            tabPesquisa.setClosable(true);
                            alerta("Palavra não foi encontrada");
                            txtBPesquisa.setDisable(false);
                            bntPesquisar.setDisable(false);
                        });
                    }
                } catch (IOException ex) {
                    Platform.runLater(() -> {
                        alerta("Falha ao abrir arquivos");
                    });
                }

            };
            new Thread(pesquisar).start();
        }
    }

    protected static void addAba(String titulo, Parent conteudo) {
        Tab tab = new Tab();
        tab.setText(titulo);

        SingleSelectionModel<Tab> selecionaTab = tabPane.getSelectionModel();
        selecionaTab.select(tab);

        tab.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (selecionaTab.getSelectedItem().equals(tab) && tabPane.getTabs().size() > 2) {
                    selecionaTab.select(2);
                }
            }
        });
        tab.setContent(conteudo);
        tabPane.getTabs().add(tab);
    }

    protected static Tab getTabAtual() {
        return tabPane.getSelectionModel().getSelectedItem();
    }

    protected static void mudaConteudoTab(String titulo, Parent conteudo) {
        tabPane.getTabs().stream().filter((tabAtual) -> (tabAtual.isSelected())).map((tabAtual) -> {
            tabAtual.setText(titulo);
            return tabAtual;
        }).forEachOrdered((tabAtual) -> {
            tabAtual.setContent(conteudo);
        });
    }

    private void addTabAdd() {
        Tab tabAdd = new Tab();

        tabAdd.setClosable(false);

        tabAdd.setOnSelectionChanged((Event event) -> {
            addAba("Home", new ViewHomePesquisa().gerar());
        });

        tabAdd.setGraphic(imagem("add.png", 20, 20));
        tabAdd.setTooltip(new Tooltip("Adicionar nova Aba"));

        tabPane.getTabs().add(tabAdd);
    }

    protected static void alerta(String mensagem) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("Alerta!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    protected static boolean alertaConfirma(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confriamação");
        alert.setHeaderText("Tem certeza?");
        alert.setContentText(mensagem);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    protected static ImageView imagem(String nome, double altura, double largura) {
        String url = "/br/uefs/ecomp/buskeyfx/imagens/";
        ImageView icon = new ImageView(new Image(url + nome));
        icon.setFitHeight(altura);
        icon.setFitWidth(largura);
        return icon;
    }

    protected static Button createButton(String texto, String url, double x, double y) {
        Button bnt = new Button(texto);
        bnt.setMaxWidth(x);
        bnt.setMinHeight(y);
        bnt.setGraphic(imagem(url, x / 3, y));
        return bnt;
    }
}
