/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;

/**
 *
 * @author Uellington Damasceno
 */
class ViewBarraPesquisa {
 
    private final Helper tabPaneBase;    
   
    public ViewBarraPesquisa() {
        tabPaneBase = new Helper();
    }

    protected HBox gerar() {
        HBox pesquisa = new HBox();
       
        Button bntHome = new Button();
        Button bntPesquisar = new Button();
        ComboBox ordem = new ComboBox();
        
        ordem.setMinHeight(25);
        ordem.setMinWidth(30);
        ordem.getItems().addAll("R+", "R-");
        ordem.setValue("R+");
        ordem.setTooltip(new Tooltip("Ordem de relevancia"));

        TextField barraPesquisa = new TextField();
        barraPesquisa.setMinWidth(250);
        
        pesquisa.setPadding(new Insets(5, 5, 5, 5));
        pesquisa.setSpacing(10);
        pesquisa.setAlignment(Pos.CENTER_LEFT);
        
        barraPesquisa.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode().equals(KeyCode.ENTER)){
                    tabPaneBase.pesquisar(barraPesquisa, bntPesquisar, ordem.getValue().toString());
                }
            }
            
        });
        
        bntPesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                tabPaneBase.pesquisar(barraPesquisa, bntPesquisar, ordem.getValue().toString());
            }
        });
        
        bntHome.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                tabPaneBase.mudaConteudoTab("Home", new ViewHomePesquisa().gerar());
            }
        });

        bntPesquisar.setGraphic(tabPaneBase.imagem("pesquisar.png", 20, 20));
        bntHome.setGraphic(tabPaneBase.imagem("home.png", 20, 20));

        bntPesquisar.setTooltip(new Tooltip("Pesquisar"));
        bntHome.setTooltip(new Tooltip("Página inicial"));

        pesquisa.getChildren().addAll(tabPaneBase.imagem("B.png", 20, 20), bntHome, barraPesquisa, bntPesquisar, ordem);
        return pesquisa;
    }
}
