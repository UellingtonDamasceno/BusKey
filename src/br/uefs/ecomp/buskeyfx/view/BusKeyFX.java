/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyHomeController;
import br.uefs.ecomp.buskeyfx.model.Pagina;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Uellington Damasceno
 */
public class BusKeyFX extends Application {

    private final int NUM_ITENS = 10;
    private TabPane tabPane;
    private Stage palco;
    private final BuskeyHomeController CONTROLLER = new BuskeyHomeController();

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        CONTROLLER.carregarDicionario();
        this.palco = primaryStage;
        palco.setResizable(false);
        this.palco.setTitle("BusKey"); //Responsavel por alterar/indicar o titulo do Palco.
        palco.setScene(inicio());
        palco.show();
    }

    private Pagination createPagination(LinkedList resultados) {
        Pagination pagination = new Pagination(resultados.size() / NUM_ITENS, 0);
        pagination.setPageFactory((Integer pageIndex) -> {
            return resultadoPesquisa(pageIndex);
        });
        return pagination;
    }

    private BorderPane homePesquisa() {
        BorderPane homePesquisa = new BorderPane();
        VBox centro = new VBox(20);
        HBox acoes = new HBox(50);

        centro.setAlignment(Pos.CENTER);
        acoes.setAlignment(Pos.CENTER);

        TextField txtPesquisa = new TextField();
        Button curiosidade = new Button("Curiosidade");
        Button pesquisar = new Button("Pesquisar");

        txtPesquisa.setMaxWidth(300);
        curiosidade.setMinWidth(80);
        pesquisar.setMinWidth(80);

        pesquisar.setGraphic(imagem("pesquisar.png", 20, 20));
        pesquisar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                pesquisar(txtPesquisa);
            }
        });

        acoes.getChildren().addAll(curiosidade, pesquisar);
        centro.getChildren().addAll(imagem("pesquisar.png", 100, 100), txtPesquisa, acoes);
        homePesquisa.setCenter(centro);
        return homePesquisa;
    }

    private Scene inicio() {
        tabPane = new TabPane();
        Tab abaInicial = new Tab();
        abaInicial.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                if (tabPane.getTabs().size() == 1) {
                    event.consume();
                }
            }
        });
        abaInicial.setText("Buskey");
        abaInicial.setContent(homePesquisa());
        tabPane.getTabs().addAll(abaInicial);
        return new Scene(tabPane, 500, 450);
    }

    private ScrollPane resultadoPesquisa(int pageIndex) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPadding(new Insets(15, 20, 10, 50));
        VBox noticias = new VBox();
        int pagina = pageIndex * NUM_ITENS;
        for (int i = pagina; i < pagina + NUM_ITENS; i++) {
            noticias.getChildren().addAll(noticia(i), new Separator());
        }
        noticias.setAlignment(Pos.CENTER);
        noticias.setSpacing(20);
        scrollPane.setContent(noticias);
        return scrollPane;
    }

    private VBox noticia(int idNoticia) {
        String nomePagina = (String)CONTROLLER.paginasEncontradas().get(idNoticia);
        Pagina pagina;
        try {
            pagina = (Pagina) CONTROLLER.getPagina(nomePagina);
        } catch (IOException ex) {
            alerta("Erro ao carregar pagina");
            return null;
        }
        VBox noticia = new VBox();
        HBox nomeNoticia = new HBox();
        Label nome = new Label(pagina.getNome());
        nome.setMaxWidth(150);
        Label acessos = new Label("Acessos: " + pagina.getAcessos());
        Label relevancia = new Label("Relevância: " + pagina.getRelevancia());
        nome.setId(pagina.getNome());

        nomeNoticia.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                nomeNoticia.setCursor(Cursor.HAND);
            }
        });
        nomeNoticia.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                mudarCena(verNoticia(nome.getId()), retiraNome(nome.getId()), false);
                
            }
        });

        nomeNoticia.getChildren().addAll(nome, extra(nome.getId()));
        nomeNoticia.setSpacing(200);

        Label prev = new Label("Aqui será exibido o conteudo do arquivo em uma\npequena previa");
        noticia.getChildren().addAll(nomeNoticia, acessos, relevancia, prev);
        noticia.setSpacing(5);
        return noticia;
    }

    private MenuBar extra(String nomePagina) {
        MenuBar ops = new MenuBar();
        Menu menubar = new Menu();
        menubar.setGraphic(imagem("menu.png", 20, 20));
        MenuItem editar = new MenuItem("Editar");
        MenuItem deletar = new MenuItem("Deletar");
        MenuItem outraAba = new MenuItem("Abrir em nova aba");

        editar.setGraphic(imagem("editar.png", 20, 20));
        deletar.setGraphic(imagem("deletar.png", 20, 20));
        outraAba.setGraphic(imagem("addAba.png", 20, 20));

        outraAba.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                addAba(verNoticia(nomePagina), retiraNome(nomePagina), false);
            }
        });

        ops.getMenus().addAll(menubar);
        menubar.getItems().addAll(editar, deletar, outraAba);
        return ops;
    }

    private String retiraNome(String aCortar){       
        return aCortar.substring(aCortar.lastIndexOf("/"));
    }
    private HBox barraPesquisa() {
        HBox pesquisa = new HBox();

        Button voltar = new Button();
        Button frente = new Button();
        Button home = new Button();
        Button pesquisar = new Button();
        Button adcionarAba = new Button();

        TextField barraPesquisa = new TextField();
        Label logo = new Label("BusKeyFX");

        pesquisa.setPadding(new Insets(5, 5, 5, 5));
        pesquisa.setSpacing(10);
        pesquisa.setAlignment(Pos.CENTER_LEFT);

        voltar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    CONTROLLER.atualizarDicionario();
                    System.out.println("Atualizado com sucesso");
                } catch (IOException ex) {
                }
            }
        });
        pesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                pesquisar(barraPesquisa);
            }
        });
        home.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                mudarCena(homePesquisa(), "Buskey", false);
            }
        });
        adcionarAba.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                addAba(homePesquisa(), "Buskey", false);
            }
        });

        pesquisar.setGraphic(imagem("pesquisar.png", 20, 20));
        frente.setGraphic(imagem("frente.png", 20, 20));
        home.setGraphic(imagem("home.png", 20, 20));
        voltar.setGraphic(imagem("voltar.png", 20, 20));
        adcionarAba.setGraphic(imagem("add.png", 20, 20));

        pesquisa.getChildren().addAll(logo, voltar, frente, home, barraPesquisa, pesquisar, adcionarAba);
        return pesquisa;
    }

    private BorderPane verNoticia(String nomePagina) {
        Pagina pagina = null;
        try {
            pagina = CONTROLLER.getPagina(nomePagina);
        } catch (IOException ex) {
           // alerta("Erro ao CarregarPagina");
        }
        HBox hBoxNome = new HBox();
        VBox opcoes = new VBox();
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
        txtConteudo.setEditable(false);
        txtNome.setEditable(false);
        txtNome.setMinWidth(250);

        Button editar = new Button("Editar");
        Button deletar = new Button("Deletar");
        Button salvar = new Button("Salvar");
        salvar.setGraphic(imagem("salvar.png", 20, 20));
        deletar.setGraphic(imagem("deletar.png", 20, 20));
        editar.setGraphic(imagem("editar.png", 20, 20));

        editar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                txtConteudo.setEditable(true);
            }
        });
        salvar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                System.out.println(txtConteudo.getText());
            }
        });
        opcoes.getChildren().addAll(editar, deletar, salvar);
        
        baseConteudo.setPadding(new Insets(10, 10, 10, 10));
        baseConteudo.getChildren().add(txtConteudo);
        verNoticia.setCenter(baseConteudo);
        verNoticia.setRight(opcoes);
        return verNoticia;
    }


    public static void main(String[] args) {
        launch(args);
    }

    private void pesquisar(TextField aPesquisar) {
        try {
            if (aPesquisar.getText().equals("")) {
                alerta("Digite algo para ser pesquisado!");
            } else {
                LinkedList resultado = CONTROLLER.pesquisar(aPesquisar.getText());
                if (resultado.size() < 10) {
                    VBox resultadoBusca = new VBox(10);
                    ScrollPane scrollPane = new ScrollPane();
                    scrollPane.setPadding(new Insets(15, 20, 10, 50));
                    for (int i = 0; i < resultado.size(); i++) {
                        resultadoBusca.getChildren().addAll(noticia(i), new Separator());
                    }
                    resultadoBusca.setAlignment(Pos.CENTER);
                    resultadoBusca.setSpacing(20);
                    scrollPane.setContent(resultadoBusca);
                    mudarCena(scrollPane, aPesquisar.getText(), true);
                } else {
                    mudarCena(createPagination(resultado), aPesquisar.getText(), true);
                }
            }
        } catch (IOException ex) {
           alerta(ex.toString());
        }
    }

    private void addAba(Parent conteudo, String titulo, boolean barraPesquisa) {
        Tab tab = new Tab();
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        BorderPane borderPane = new BorderPane();
        tab.setText(titulo);
        if (barraPesquisa) {
            borderPane.setTop(barraPesquisa());
        }
        selectionModel.select(tab);
        borderPane.setCenter(conteudo);
        tab.setContent(borderPane);
        tabPane.getTabs().add(tab);
    }

    private void alerta(String mensagem) {
        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Alerta!");
        alerta.setHeaderText(null);
        alerta.setContentText(mensagem);
        alerta.showAndWait();
    }

    private boolean alertaConfirma(String mensagem) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confriamação");
        alert.setHeaderText("Tem certeza?");
        alert.setContentText(mensagem);
        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private void mudarCena(Parent conteudo, String titulo, boolean barraPesquisa) {
        for (Tab tabAtual : tabPane.getTabs()) {
            if (tabAtual.isSelected()) {
                if (barraPesquisa) {
                    BorderPane bp = new BorderPane();
                    bp.setTop(barraPesquisa());
                    bp.setCenter(conteudo);
                    tabAtual.setContent(bp);

                } else {
                    tabAtual.setContent(conteudo);
                }
                tabAtual.setText(titulo);
                break;
            }
        }
    }

    private ImageView imagem(String nome, double altura, double largura) {
        String url = "/br/uefs/ecomp/buskeyfx/imagens/";
        ImageView icon = new ImageView(new Image(url + nome));
        icon.setFitHeight(altura);
        icon.setFitWidth(largura);
        return icon;
    }
}
