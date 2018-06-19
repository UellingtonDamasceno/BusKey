/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uefs.ecomp.buskeyfx.view;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import javafx.scene.input.MouseEvent;
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

    private Parent principal;
    private Stage palco;
    private double x, y;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.palco = primaryStage;
        this.palco.setTitle("BusKey"); //Responsavel por alterar/indicar o titulo do Palco.
        Pagination pagination = new Pagination(5, 0);
        pagination.setPageFactory((Integer pageIndex) -> {
            ScrollPane sp = new ScrollPane();
            sp.setPadding(new Insets(15, 20, 10, 50));
            for (int i = 0; i < pageIndex+1; i++) {
                sp.setContent(home());
            }
            return sp;
        });
        //setCenarioPrincipal("Home.fxml");
        palco.setScene(tabPane(pagination));
        palco.show();
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
        //BorderPane home = new BorderPane();
        //home.setTop(barraPesquisa(painelAbas));
        VBox noticias = new VBox();
        for (int i = 0; i < 10; i++) {
            noticias.getChildren().addAll(noticia(i), new Separator());
        }
        noticias.setAlignment(Pos.CENTER);
        noticias.setSpacing(20);
        //home.setCenter(sp);
        //home.setRight(maisRelevante());
        return noticias;
    }

    private Pane noticia(int i) {
        VBox noticia = new VBox();
        HBox nomeNoticia = new HBox();
        nomeNoticia.setOnMouseClicked(new EventHandler() {
            @Override
            public void handle(Event event) {
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
        Menu op = new Menu("...");
        MenuItem editar = new MenuItem("Editar");
        MenuItem deletar = new MenuItem("Deletar");
        MenuItem outraAba = new MenuItem("Abrir em nova aba");
        ops.getMenus().addAll(op);
        op.getItems().addAll(editar, deletar, outraAba);
        return ops;
    }

    private HBox barraPesquisa(TabPane painelAbas) {
        HBox pesquisa = new HBox();
        pesquisa.setPadding(new Insets(5, 5, 5, 5));
        pesquisa.setSpacing(10);
        pesquisa.setAlignment(Pos.CENTER_LEFT);
        Label logo = new Label("BusKeyFX");
        Button voltar = new Button("<");
        voltar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
        Button frente = new Button(">");
        Button home = new Button("|^|");
        Button pesquisar = new Button("-O");
        pesquisar.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                //Chamar metodo que pesquisa pegando as palavras chaves.
            }
        });
        TextField barraPesquisa = new TextField();
        Button b = new Button("+");
        b.setOnAction(new EventHandler() {
            @Override
            public void handle(Event event) {
                painelAbas.getTabs().add(new Tab("BuskeyFX - Home"));
            }
        });
        pesquisa.getChildren().addAll(logo, voltar, frente, home, barraPesquisa, pesquisar, b);
        return pesquisa;
    }

    private BorderPane verNoticia() {
        BorderPane verNoticia = new BorderPane();
        verNoticia.setPadding(new Insets(5, 5, 5, 5));
        verNoticia.setTop(nomePagina());
        TextArea txtConteudo = new TextArea();
        //txtConteudo.setPadding(new Insets(5, 5, 5, 5));
        txtConteudo.setPrefSize(420, 380);
        ScrollPane scConteudo = new ScrollPane();
        scConteudo.setContent(txtConteudo);
        verNoticia.setCenter(scConteudo);
        verNoticia.setRight(opcoesVerNoticia());
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

    private VBox opcoesVerNoticia() {
        VBox opcoes = new VBox();
        Button editar = new Button("Editar");
        Button deletar = new Button("Deletar");
        Button salvar = new Button("Salvar");
        opcoes.getChildren().addAll(editar, deletar, salvar);
        return opcoes;
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
