package com.example.filescontrol.FilesControl;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class ListenedFiles implements Dao<ListenedFile>{
    ObservableList<ListenedFile> listenedFiles = FXCollections.observableArrayList();

    /**
     * @param file file
     * @return listenedFiles
     */
    public ListenedFile get(File file) throws IOException {
        if(file.exists()) {
            for (ListenedFile element : listenedFiles)
                if (element.getIncode().equals(getIncode(file)) && !Objects.equals(element.getIncode(), "") && !Objects.equals(getIncode(file), ""))
                    return element;
        }
        else {
            for (ListenedFile element1 : listenedFiles)
                if (element1.getFile().getAbsolutePath().equals(file.getAbsolutePath()))
                    return element1;
        }
        return null;
    }

    public String getIncode(File file) throws IOException {
        if(file.exists()) {
            BasicFileAttributes attr;
            Path path = Paths.get(file.getAbsolutePath());

            attr = Files.readAttributes(path, BasicFileAttributes.class);

            Object fileKey = attr.creationTime();
            return fileKey.toString();
        }
        return "";
    }
    /**
     * @return all files
     */
    @Override
    public List<ListenedFile> getAll() {
        return listenedFiles;
    }

    /**
     * @param t add new object
     */
    @Override
    public void save(ListenedFile t) {
        Platform.runLater(() ->{
            listenedFiles.add(t);
            try {
                createdState(t);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * @param listenedFile file
     * @throws IOException exception
     */
    private void createdState(ListenedFile listenedFile) throws IOException {
        File file = listenedFile.getFile();
        String cause = "файл создан";
        String own = "";
        if (listenedFile.getFile().exists()) {
            FileOwnerAttributeView ownerView = Files.getFileAttributeView(
                    Paths.get(file.getAbsolutePath()), FileOwnerAttributeView.class);
            UserPrincipal owner = ownerView.getOwner();
            own = owner.getName();
        }
        listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).setCause(cause).build());
    }

    /**
     * @param t      updateble object
     * @param params params for update
     */
    @Override
    public void update(ListenedFile t, String[] params) {

    }

    /**
     * @param t deletable object
     */
    @Override
    public void delete(ListenedFile t) {

    }
}
