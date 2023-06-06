package com.example.filescontrol;

import com.example.filescontrol.FilesControl.FileState;
import com.example.filescontrol.FilesControl.ListenedFile;
import com.example.filescontrol.FilesControl.ListenedFiles;
import com.example.filescontrol.FilesControl.User;
import com.example.filescontrol.Watcher.FileAdapter;
import com.example.filescontrol.Watcher.FileWatcher;
import javafx.beans.InvalidationListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Controller
 */
public class HelloController {
    public TextField fileName;
    public TextField dateEdit;
    public TextField userEdit;
    public TextArea causeEdit;
    public ListView<ListenedFile> filesList;
    public ListView<FileState> fileStates;
    FileWatcher watcher;
    ListenedFiles listenedFiles = new ListenedFiles();
    InvalidationListener listenerFile;
    final DirectoryChooser directoryChooser = new DirectoryChooser();

    User user;
    public void init(int id)
    {
        DatabaseAdapter.getDBConnection();
        user = DatabaseAdapter.getUser(id);
        InvalidationListener listenerState = observable -> {
            if(!fileStates.getSelectionModel().isEmpty()) {
                fileName.setText(fileStates.getSelectionModel().getSelectedItem().getTitle());
                userEdit.setText(fileStates.getSelectionModel().getSelectedItem().getAuthor());
                causeEdit.setText(fileStates.getSelectionModel().getSelectedItem().getCause());
                dateEdit.setText(fileStates.getSelectionModel().getSelectedItem().getEdited().format(DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss")));
            }
        };
        listenerFile = e->{
            fileStates.setItems((ObservableList<FileState>) filesList.getSelectionModel().getSelectedItem().getAll());
            fileStates.getSelectionModel().selectedItemProperty().removeListener(listenerState);
            fileStates.getSelectionModel().selectedItemProperty().addListener(listenerState);
        };
        configuringDirectoryChooser(directoryChooser);
        filesList.setItems((ObservableList<ListenedFile>) listenedFiles.getAll());
        filesList.getSelectionModel().selectedItemProperty().addListener(listenerFile);
    }


    /**
     * @param directoryChooser directoryChooser
     */
    private void configuringDirectoryChooser(DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Выбор папки для отслеживания");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
    }

    /**
     */
    @FXML
    private void selectPath() throws IOException {
        if(watcher != null)
            watcher.getListeners().clear();
        File dir = directoryChooser.showDialog(null);
        if (dir != null) {
            watcher = new FileWatcher(dir);
            filesList.getSelectionModel().selectedItemProperty().removeListener(listenerFile);
            listenedFiles.getAll().clear();
            for(File file: Objects.requireNonNull(dir.listFiles())) {
                listenedFiles.save(new ListenedFile(file));
            }
            filesList.getSelectionModel().selectedItemProperty().addListener(listenerFile);
            watcher.addListener(new FileAdapter(listenedFiles)).watch();
        } else {
            new Alert(Alert.AlertType.ERROR,"Не выбрана папка для отслеживания!", ButtonType.CLOSE).show();
        }
    }

    /**
     * info about programm
     */
    @FXML
    private void about()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Информация о программе");
        alert.setTitle("Справка");
        alert.setContentText("Данная программа предназначена для отслеживания изменений в файлах внутри выбранной пользователем папки.\nДля начала работы используйте меню 'Файл' -> 'Выбор папки для отслеживания'.\nПосле выбора директории в левом меню появится список файлов в папке, в среднем - список состояний файла, справа - параметры состояния");
        alert.show();
    }
}