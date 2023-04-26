package com.winway.onlinechat;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application
{
    public static void main(String[] args)
    {
        launch();
    }
    
    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
//    ListView<Session> sessionListView = new ListView<>();
//
//    ObservableList<Session> sessionList = FXCollections.observableArrayList(
//            new Session("Session 1", "/path/to/thumbnail1.png"),
//            new Session("Session 2", "/path/to/thumbnail2.png"),
//            new Session("Session 3", "/path/to/thumbnail3.png")
//    );
//
//sessionListView.setItems(sessionList);
//
//        sessionListView.setCellFactory(param -> new ListCell<Session>() {
//@Override
//protected void updateItem(Session session, boolean empty) {
//        super.updateItem(session, empty);
//        if (empty || session == null) {
//        setText(null);
//        setGraphic(null);
//        } else {
//        setText(session.getName());
//        setGraphic(new ImageView(session.getThumbnail()));
//        }
//        }
//        });
//
//        sessionListView.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
//        // Handle selection change
//        });
