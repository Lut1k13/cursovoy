package com.example.filescontrol.FilesControl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

/**
 * listened file class
 */
public class ListenedFile implements Dao<FileState>{
    private File file;
    String incode;
    ObservableList<FileState> fileStates = FXCollections.observableArrayList();

    /**
     * @param file file for listen
     */
    public ListenedFile(File file) throws IOException {
        this.file = file;
        BasicFileAttributes attr;
        Path path = Paths.get(file.getAbsolutePath());
        if(file.exists()) {
            attr = Files.readAttributes(path, BasicFileAttributes.class);

            Object fileKey = attr.creationTime();
            incode = fileKey.toString();
        }
        else incode="";
    }

    public String getIncode() {
        return incode;
    }
    public void setFile(File file)
    {
        this.file = file;
    }
    /**
     * @return get file
     */
    public File getFile() {
        return file;
    }

    /**
     * @return get all files
     */
    @Override
    public List<FileState> getAll() {
        return fileStates;
    }

    /**
     * @param t add new object
     */
    @Override
    public void save(FileState t) {
        Platform.runLater(() ->fileStates.add(t));
    }

    /**
     * @param t      updateble object
     * @param params params for update
     */
    @Override
    public void update(FileState t, String[] params) {

    }

    /**
     * @param t deletable object
     */
    @Override
    public void delete(FileState t) {

    }
    public String toString()
    {
        return file.getName();
    }
}
