/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Uellington Damasceno
 */
public class BusKeyFX extends Application {

    private Helper tabPaneBase;

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        tabPaneBase = new Helper();
        FacadeBuskeyfx facade = FacadeBuskeyfx.getInstance();
       
        try {
            facade.gerarLog();
            facade.carregarDicionario();
        } catch (IOException | ClassNotFoundException ex) {
            Helper.alerta("Erro ao carregar Dicionario!");
        }

        primaryStage.setResizable(false);
        primaryStage.setTitle("BusKey");
        primaryStage.getIcons().add(new Image("/br/uefs/ecomp/buskeyfx/imagens/B.png"));

        primaryStage.setScene(tabPaneBase.gerar());
        primaryStage.show();

        primaryStage.setOnCloseRequest(new EventHandler() {
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
