package com.example.filescontrol.Watcher;

import com.example.filescontrol.FilesControl.FileState;
import com.example.filescontrol.FilesControl.ListenedFiles;
import com.example.filescontrol.FilesControl.ListenedFile;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.nio.file.attribute.UserPrincipal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * file adapter
 */
public class FileAdapter implements FileListener {
    ListenedFiles listenedFiles;

    /**
     * @param listenedFiles list of files
     */
    public FileAdapter(ListenedFiles listenedFiles)
    {
        this.listenedFiles = listenedFiles;
    }

    /**
     * @param event type
     */
    @Override
    public void onCreated(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            if(listenedFiles.get(event.getFile())==null)
                listenedFiles.save(new ListenedFile(event.getFile()));
        }
    }

    /**
     * @param event type
     * @throws IOException exception
     */
    @Override
    public void onModified(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            ListenedFile file = listenedFiles.get(event.getFile());
            if (!file.getFile().getName().equals(event.getFile().getName()))
                file.setFile(event.getFile());
            saveState(file,false);
        }
    }

    /**
     * @param event type
     * @throws IOException exception
     */
    @Override
    public void onDeleted(FileEvent event) throws IOException {
        if(!event.getFile().getName().contains("~")) {
            ListenedFile file = listenedFiles.get(event.getFile());
            saveState(file,true);
        }
    }

    /**
     * @param listenedFile file
     * @param delete file is deleted
     * @throws IOException exception
     */
    public void saveState(ListenedFile listenedFile,boolean delete) throws IOException {
        File file = listenedFile.getFile();
        String own="";
        String cause = "файл удалён";
        if((file.getName().contains(".doc") || file.getName().contains(".docx")) && !delete) {
            InputStream input = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(input);
            POIXMLProperties prop = document.getProperties();
            int simbols = prop.getExtendedProperties().getCharacters();
            String author = prop.getCoreProperties().getLastModifiedByUser();
            document.close();
            input.close();
                cause = "Количество символов изменено на: "+simbols;
            listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                    LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                    author).setCause(cause).build());
        }
        else{
            if(!delete) {
                FileOwnerAttributeView ownerView = Files.getFileAttributeView(
                        Paths.get(file.getAbsolutePath()), FileOwnerAttributeView.class);
                UserPrincipal owner = ownerView.getOwner();
                own = owner.getName();
                listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).build());
            }
            else {
                if(!listenedFile.getAll().isEmpty())
                    own = listenedFile.getAll().get(0).getAuthor();
                listenedFile.save(new FileState.FileStateBuilder(file.getName(),
                        LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(), ZoneId.systemDefault()),
                        own).setCause(cause).build());
            }
        }
    }

}
