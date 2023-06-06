package com.example.filescontrol;

import com.example.filescontrol.FilesControl.User;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AuthController implements Initializable {
    public TextField nameField;
    public PasswordField passField;

    public void userEnter(int id) throws IOException {
        nameField.getScene().getWindow().hide();
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((HelloController) fxmlLoader.getController()).init(id);
        stage.setTitle("Панель проектов");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DatabaseAdapter.getDBConnection();
    }

    public void login() throws IOException {
        int id;
        if ((id = userCheck()) != -1)
            userEnter(id);
        else new Alert(Alert.AlertType.ERROR, "Пользователь не найден!", ButtonType.OK).show();
    }

    public int userCheck() {
        return DatabaseAdapter.getUser(nameField.getText(), passField.getText());
    }

    public void register() throws IOException {
        int id;
        if ((id = DatabaseAdapter.addUser(new User(nameField.getText(), passField.getText()))) != -1)
            userEnter(id);
        else new Alert(Alert.AlertType.ERROR, "Пользователь не создан!", ButtonType.OK).show();
    }
}