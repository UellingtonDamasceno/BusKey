/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.model.Pagina;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
/**
 *
 * @author Uellington Damasceno
 */
public class VerNoticia {
    Pagina pagina;
    ViewHelper helper;
    
    public VerNoticia(Pagina pagina, ViewHelper helper){
        this.pagina = pagina;
        this.helper = helper;
    }
    
    protected BorderPane gerar(boolean editavel) {
        pagina.addAcessos(1);
        System.out.println(pagina.getAcessos());
        HBox hBoxNome = new HBox();
        VBox opcoes = new VBox(20);
        Pane baseConteudo = new Pane();

        BorderPane verNoticia = new BorderPane();
        Label lblNome = new Label("Nome Da Pagina: ");
        TextField txtNome = new TextField(pagina.getNome());
        TextArea txtConteudo = new TextArea(pagina.getConteudo());

        hBoxNome.getChildren().addAll(lblNome, txtNome);
        hBoxNome.setSpacing(20);

        verNoticia.setPadding(new Insets(5, 5, 5, 5));
        verNoticia.setTop(hBoxNome);

        txtConteudo.setPrefSize(400, 400);
        txtConteudo.setEditable(editavel);
        txtNome.setEditable(false);
        txtNome.setMinWidth(250);

        Button editar = helper.createButton("Editar", "editar.png", 95, 35);
        Button voltar = helper.createButton("Deletar", "deletar.png", 95, 35);
        Button cancelar = helper.createButton("Cancelar", "cancelar.png", 100, 30);
        Button salvar = helper.createButton("Salvar", "salvar.png", 95, 35);

        txtNome.setTooltip(new Tooltip("Caminho absoluto do arquivo"));
        editar.setTooltip(new Tooltip("Editar conteudo"));
        voltar.setTooltip(new Tooltip("Voltar para resultado"));
        cancelar.setTooltip(new Tooltip("Cancelar edição"));
        salvar.setTooltip(new Tooltip("Salvar alterações"));

        salvar.setVisible(editavel);
        cancelar.setVisible(editavel);
       
        editar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                txtConteudo.setEditable(true);
                cancelar.setVisible(true);
                salvar.setVisible(true);
            }
        });
        
        voltar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
            
            }
        });
        
        cancelar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if(!pagina.getConteudo().equals(txtConteudo.getText())){
                    txtConteudo.setText(pagina.getConteudo());
                }
                txtConteudo.setEditable(false);
                cancelar.setVisible(false);
                salvar.setVisible(false);
            }
        });
        salvar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (txtConteudo.getText().equals("")) {
                    helper.alerta("Campo vazio!");
                    //CONTROLLER.editarPag(pagina, txtConteudo);
                } else if (pagina.getConteudo().equals(txtConteudo.getText())) {
                    helper.alerta("Não ouve alteração!");
                    event.consume();
                } else {
                    System.out.println("Salvou");
                }
            }
        });

        opcoes.setAlignment(Pos.TOP_CENTER);
        opcoes.getChildren().addAll(editar, voltar, cancelar, salvar);

        baseConteudo.setPadding(new Insets(10, 10, 10, 10));
        baseConteudo.getChildren().add(txtConteudo);
        verNoticia.setCenter(baseConteudo);
        verNoticia.setRight(opcoes);
        return verNoticia;
    }
}
