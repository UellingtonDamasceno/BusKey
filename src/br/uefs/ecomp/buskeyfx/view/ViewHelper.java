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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author Uellington Damasceno
 */
public class ViewHelper {

    private TabPane tabPane;
 
    public ViewHelper(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    void pesquisar(BuskeyPesquisaController c, String aPesquisar) {
        try {
            if (aPesquisar.equals("")) {
                alerta("Digite algo para ser pesquisado!");
            } else {
                LinkedList resultado = c.pesquisar(aPesquisar);
                if (!resultado.isEmpty()) {
                    if (resultado.size() < 10) {
//                        VBox resultadoBusca = new VBox(10);
//                        ScrollPane scrollPane = new ScrollPane();
//                        scrollPane.setPadding(new Insets(15, 20, 10, 50));
//                        for (int i = 0; i < resultado.size(); i++) {
//                            resultadoBusca.getChildren().addAll(noticia(i), new Separator());
//                        }
//                        resultadoBusca.setAlignment(Pos.CENTER);
//                        resultadoBusca.setSpacing(20);
//                        scrollPane.setContent(resultadoBusca);
//                        mudarCena(scrollPane, aPesquisar, true);
                    } else {
                        getTabAtual(aPesquisar).setContent(new ViewResultado(resultado, this).createPagination());
                    }
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
