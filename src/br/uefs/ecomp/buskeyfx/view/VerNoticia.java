/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.facade.FacadeBuskeyfx;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.IOException;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

    private FacadeBuskeyfx facade;
    private Pagina pagina;
    private TextArea txtConteudo;
    private TextField txtNome;
    private Parent voltar;
            
    public VerNoticia(Parent voltar) {
        facade = FacadeBuskeyfx.getInstance();
        txtNome = new TextField();
        txtConteudo = new TextArea();
        this.voltar = voltar;
    }

    public VerNoticia(Pagina pagina, Parent voltar) {
        this(voltar);
        this.pagina = pagina;
    }

    protected BorderPane gerar(boolean editavel) {
        HBox hBoxNome = new HBox();
        VBox vbOpcoes = new VBox(20);
        BorderPane bpNoticia = new BorderPane();
        Pane pBase = new Pane();

        Label lblNome = new Label("Nome Da Pagina: ");
        if (pagina != null) {
            pagina.addAcessos();
            txtNome = new TextField(pagina.getNome());
            txtConteudo = new TextArea(pagina.getTextoConteudo());
            txtNome.setEditable(false);
        } else {
            txtNome.setEditable(true);
        }

        hBoxNome.getChildren().addAll(lblNome, txtNome);
        hBoxNome.setSpacing(20);

        bpNoticia.setPadding(new Insets(5, 5, 5, 5));
        bpNoticia.setTop(hBoxNome);

        txtConteudo.setPrefSize(400, 400);
        txtConteudo.setEditable(editavel);
        txtNome.setMinWidth(250);

        Button bntEditar = Helper.createButton("Editar", "editar.png", 95, 35);
        Button bntVoltar = Helper.createButton("Voltar", "voltar.png", 95, 35);
        Button bntCancelar = Helper.createButton("Cancelar", "cancelar.png", 100, 30);
        Button bntSalvar = Helper.createButton("Salvar", "salvar.png", 95, 35);

        txtNome.setTooltip(new Tooltip("Nome da noticia"));
        bntEditar.setTooltip(new Tooltip("Editar conteudo"));
        bntVoltar.setTooltip(new Tooltip("Voltar para resultado"));
        bntCancelar.setTooltip(new Tooltip("Cancelar edição"));
        bntSalvar.setTooltip(new Tooltip("Salvar alterações"));

        bntSalvar.setVisible(editavel);
        bntCancelar.setVisible(editavel);

        bntEditar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (pagina == null) {
                    txtNome.setEditable(true);
                }
                txtConteudo.setEditable(true);
                bntCancelar.setVisible(true);
                bntSalvar.setVisible(true);
            }
        });

        bntVoltar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                Helper.mudaConteudoTab("Home", voltar);
            }
        });

        bntCancelar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (pagina != null && !pagina.getTextoConteudo().equals(txtConteudo.getText())) {
                    txtConteudo.setText(pagina.getTextoConteudo());
                }
                txtNome.setEditable(false);
                txtConteudo.setEditable(false);
                bntCancelar.setVisible(false);
                bntSalvar.setVisible(false);
            }
        });

        bntSalvar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (txtConteudo.getText().equals("")) {
                    Helper.alerta("Campo vazio!");
                } else {
                    if (pagina != null) {
                        if (pagina.getTextoConteudo().equals(txtConteudo.getText())) {
                            Helper.alerta("Não ouve alteração!");
                            event.consume();
                        } else {
                            System.out.println("Salvou");
                        }
                    } else {
                        try {
                            boolean add = facade.adcionarPagina(txtNome.getText(), txtConteudo.getText());
                                if (add) {
                                    Helper.alerta("Pagina Adcionada com sucesso!");
                                    txtNome.setText("");
                                    txtConteudo.setText("");
                                } else {
                                    Helper.alerta("Erro ao adcionar pagina");
                                }
                                Helper.mudaConteudoTab("Home", voltar);
                            } catch (IOException ex) {
                                Helper.alerta("Erro ao abrir o arquivo!");
                            }
                    }
                }
            }
        });

        vbOpcoes.setAlignment(Pos.TOP_CENTER);

        vbOpcoes.getChildren().addAll(bntVoltar, bntEditar, bntCancelar, bntSalvar);

        pBase.setPadding(new Insets(10, 10, 10, 10));
        pBase.getChildren().add(txtConteudo);
        bpNoticia.setCenter(pBase);

        bpNoticia.setRight(vbOpcoes);
        return bpNoticia;
    }
}
