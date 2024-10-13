/**
Artifact Enhancement
Author: Samuel Walters
Date: 9/26/24
Updated: 10/13/24 to include stylesheets and a database connection.
 */
package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.data.DBConnection;

import java.io.IOException;
import java.util.Objects;


public class MainApp extends Application {
    private static Stage stage;

    @Override
    public void start(Stage s) throws IOException {
        stage = s;
        setRoot("primary", "Contact Application");
        stage.getScene().getStylesheets().add(Objects.requireNonNull(MainApp.class.getResource("/styles/Styles.css")).toExternalForm());
        // 10/2/24 added the below line to create the database connection on program start.
        DBConnection db = new DBConnection();
        db.getDBConnection();
    }

    static void setRoot(String fxml, String title) throws IOException {
        Scene scene = new Scene(loadFXML(fxml));
        scene.getStylesheets().add(Objects.requireNonNull(MainApp.class.getResource("/styles/Styles.css")).toExternalForm());        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("/fxml/"+fxml + ".fxml"));
        return fxmlLoader.load();
    }

/**
    Main method is used in JavaFX applications to launch the application.
    DO NOT ADD ANYTHING TO THIS METHOD.
 */
    public static void main(String[] args) {
        launch(args);
    }
}
