/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Uellington Damasceno
 */
public class BusKeyFX extends Application {

    private Parent principal;
    private Stage palco;
    private double x, y;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.palco = primaryStage;
        this.palco.setTitle("BusKey"); //Responsavel por alterar/indicar o titulo do Palco.
        setCenarioPrincipal("Home.fxml");
    }

    private void setCenarioPrincipal(String caminhoCenario) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(caminhoCenario));
        principal = loader.load();
        Scene cenario = new Scene(principal); // Cria um cenario com  
        palco.setScene(cenario); // Responsavel por indicar qual cenário deverá ser exibido no Palco. 
        controlePosicao(principal); // Método responsavel por permitir que o usario possa mover a tela apartir de qualquer ponto que selecionar.
        palco.initStyle(StageStyle.TRANSPARENT); //Responsavel por retirar as opções padrões(minimizar, maximizar e fechar) do palco.
        palco.show();
    }

    public Stage getPalco() {
        return palco;
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void controlePosicao(Parent principal) {
        principal.setOnMousePressed((MouseEvent event) -> {
            x = event.getSceneX();
            y = event.getSceneY();
        });
        principal.setOnMouseDragged((MouseEvent event) -> {
            palco.setX(event.getScreenX() - x);
            palco.setY(event.getScreenY() - y);
        });
    }

}
