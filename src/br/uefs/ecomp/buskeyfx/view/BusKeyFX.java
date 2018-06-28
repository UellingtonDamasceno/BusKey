/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyPesquisaController;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Uellington Damasceno
 */
public class BusKeyFX extends Application {

    private TabPane tabPane;
    private Stage palco;
    private final BuskeyPesquisaController CONTROLLER = new BuskeyPesquisaController();
    private ViewHelper helper;

    @Override
    public void start(Stage primaryStage) {
        tabPane = new TabPane();
        helper = new ViewHelper(tabPane, CONTROLLER);
        palco = primaryStage;
        
        try {
            CONTROLLER.carregarDicionario();
        } catch (IOException | ClassNotFoundException ex) {
            helper.alerta("Erro ao carregar dicionario!");
        }

        palco.setResizable(false);
        palco.setTitle("BusKey");
        palco.getIcons().add(new Image("/br/uefs/ecomp/buskeyfx/imagens/B.png"));
        
        Tab tabInicial = helper.addAba("Home");
        tabInicial.setContent(new ViewHomePesquisa(helper).gerar());
        
        palco.setScene(new Scene(tabPane, 500, 450));
        palco.show();
 
        palco.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println("Feshow");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

}
