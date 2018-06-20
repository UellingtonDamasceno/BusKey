/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import br.uefs.ecomp.buskeyfx.controller.BuskeyHomeController;
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Uellington Damasceno
 */
public class BusKeyFX extends Application {

    private TabPane principal;
    private Stage palco;
    private double x, y;
    private final BuskeyHomeController CONTROLLER = new BuskeyHomeController();

    @Override
    public void start(Stage primaryStage) throws IOException, ClassNotFoundException {
        CONTROLLER.carregarDicionario();
        this.palco = primaryStage;
        palco.setResizable(false);
        this.palco.setTitle("BusKey"); //Responsavel por alterar/indicar o titulo do Palco.
        Pagination pagination = new Pagination(5, 0);
        pagination.setPageFactory((Integer pageIndex) -> {
            ScrollPane sp = new ScrollPane();
            sp.setPadding(new Insets(15, 20, 10, 50));
            for (int i = 0; i < pageIndex + 1; i++) {
                sp.setContent(home());
            }
            return sp;
        });
        palco.setScene(tabPane(pagination));
        palco.show();
    }
    private void addAba(Parent conteudo){
        Tab tab = new Tab();
        tab.setText("Buskey");
        tab.setContent(conteudo);
        principal.getTabs().add(tab);
    }
//    private void setCenarioPrincipal(String caminhoCenario) throws IOException {
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource(caminhoCenario));
//        principal = loader.load();
//        Scene cenario = new Scene(principal); // Cria um cenario com  
//        palco.setScene(cenario); // Responsavel por indicar qual cenário deverá ser exibido no Palco. 
//        controlePosicao(principal); // Método responsavel por permitir que o usario possa mover a tela apartir de qualquer ponto que selecionar.
//        palco.initStyle(StageStyle.TRANSPARENT); //Responsavel por retirar as opções padrões(minimizar, maximizar e fechar) do palco.
//        palco.show();
//    }
    private static BorderPane HomePesquisa() {
        BorderPane homePesquisa = new BorderPane();
        StackPane sp = new StackPane();

        return homePesquisa;
    }

    private Scene tabPane(Pagination pagination) {
        TabPane painelAbas = new TabPane();
        principal = painelAbas;
        painelAbas.setFocusTraversable(false);
        Tab aba2 = new Tab();
        Tab aba1 = new Tab();
        BorderPane bp = new BorderPane();
        bp.setCenter(pagination);
        bp.setTop(barraPesquisa(painelAbas));
        aba2.setContent(bp);
        aba2.setText("Pesquisa 1");
        aba1.setContent(verNoticia());
        aba1.setText("Ver Pesquisa");
        painelAbas.getTabs().addAll(aba2, aba1);
        return new Scene(painelAbas, 500, 450);
    }

    public Stage getPalco() {
        return palco;
    }

    private VBox home() {
        VBox noticias = new VBox();
        for (int i = 0; i < 10; i++) {
            noticias.getChildren().addAll(noticia(i), new Separator());
        }
        noticias.setAlignment(Pos.CENTER);
        noticias.setSpacing(20);
        return noticias;
    }

    private Pane noticia(int i) {
        VBox noticia = new VBox();
        HBox nomeNoticia = new HBox();
        nomeNoticia.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                nomeNoticia.setCursor(Cursor.HAND);
            }
        });
        nomeNoticia.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
                addAba(verNoticia());
                System.out.println("Abriu a noticia");
            }
        });
        /*nomeNoticia.setOnMouseEntered(new EventHandler() {
            @Override
            public void handle(Event event) {
                
            }
        });*/
        Label nome = new Label("Nome Da Noticia");
        nomeNoticia.getChildren().addAll(nome, extra());
        nomeNoticia.setSpacing(200);
        Label acessos = new Label();
        acessos.setText("Acessos: " + 10);
        Label relevancia = new Label("Relevância: " + Integer.toString(i));
        Label prev = new Label("Aqui será exibido o conteudo do arquivo em uma\npequena previa");
        noticia.getChildren().addAll(nomeNoticia, acessos, relevancia, prev);
        noticia.setSpacing(5);
        Pane paneNoticia = new Pane();
        paneNoticia.getChildren().add(noticia);
        paneNoticia.getOnMouseDragReleased();
        return paneNoticia;
    }

    private MenuBar extra() {
        MenuBar ops = new MenuBar();
        Menu menubar = new Menu();
        menubar.setGraphic(insereImagem("menu.png"));
        MenuItem editar = new MenuItem("Editar");
        MenuItem deletar = new MenuItem("Deletar");
        MenuItem outraAba = new MenuItem("Abrir em nova aba");

        editar.setGraphic(insereImagem("editar.png"));
        deletar.setGraphic(insereImagem("deletar.png"));
        outraAba.setGraphic(insereImagem("addAba.png"));

        ops.getMenus().addAll(menubar);
        menubar.getItems().addAll(editar, deletar, outraAba);
        return ops;
    }

    private ImageView insereImagem(String nome) {
        String url = "/br/uefs/ecomp/buskeyfx/imagens/";
        ImageView icon = new ImageView(new Image(url + nome));
        icon.setFitHeight(20);
        icon.setFitWidth(20);
        return icon;
    }

    private HBox barraPesquisa(TabPane painelAbas) {
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

            }
        });
        pesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    CONTROLLER.pesquisar(barraPesquisa.getText());
                } catch (IOException ex) {

                }
            }
        });
        home.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                try {
                    CONTROLLER.atualizarDicionario();
                } catch (IOException ex) {
                    System.out.println("Deu merda na escrita");
                }
                System.out.println("Atualizado com Sucesso!!!");
            }
        });
        adcionarAba.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                painelAbas.getTabs().add(new Tab("BuskeyFX - Home"));
            }
        });

        pesquisar.setGraphic(insereImagem("pesquisar.png"));
        frente.setGraphic(insereImagem("frente.png"));
        home.setGraphic(insereImagem("home.png"));
        voltar.setGraphic(insereImagem("voltar.png"));
        adcionarAba.setGraphic(insereImagem("add.png"));

        pesquisa.getChildren().addAll(logo, voltar, frente, home, barraPesquisa, pesquisar, adcionarAba);
        return pesquisa;
    }
    
    private BorderPane verNoticia() {
        BorderPane verNoticia = new BorderPane();
        TextArea txtConteudo = new TextArea();
        verNoticia.setPadding(new Insets(5, 5, 5, 5));
        verNoticia.setTop(nomePagina());

        txtConteudo.setPrefSize(420, 380);
        txtConteudo.setEditable(false);
        
        verNoticia.setCenter(txtConteudo);
        verNoticia.setRight(opcoesVerNoticia(txtConteudo));
        return verNoticia;
    }

    private HBox nomePagina() {
        HBox nomePagina = new HBox();
        Label lblNome = new Label("Nome Da Pagina: ");
        TextField txtNome = new TextField();
        txtNome.setMinWidth(300);
        nomePagina.getChildren().addAll(lblNome, txtNome);
        nomePagina.setSpacing(20);
        return nomePagina;
    }

    private VBox opcoesVerNoticia(TextArea txtConteudo) {
        VBox opcoes = new VBox();

        Button editar = new Button("Editar");
        Button deletar = new Button("Deletar");
        Button salvar = new Button("Salvar");
        salvar.setGraphic(insereImagem("salvar.png"));
        deletar.setGraphic(insereImagem("deletar.png"));
        editar.setGraphic(insereImagem("editar.png"));

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
        return opcoes;
    }

    public static void main(String[] args) {
        launch(args);
    }

//    private void controlePosicao(Parent principal) {
//        principal.setOnMousePressed((MouseEvent event) -> {
//            x = event.getSceneX();
//            y = event.getSceneY();
//        });
//        principal.setOnMouseDragged((MouseEvent event) -> {
//            palco.setX(event.getScreenX() - x);
//            palco.setY(event.getScreenY() - y);
//        });
}
