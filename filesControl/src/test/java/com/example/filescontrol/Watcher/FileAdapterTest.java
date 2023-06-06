package com.example.filescontrol.Watcher;

import com.example.filescontrol.FilesControl.ListenedFile;
import com.example.filescontrol.FilesControl.ListenedFiles;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.jupiter.api.Test;
import javafx.embed.swing.JFXPanel;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
@RunWith(JfxRunner.class)
class FileAdapterTest {

    @Test
    void saveState() throws IOException, InterruptedException {
        new JFXPanel();
        File folder = new File("src/test/resources");
        FileAdapter adapter = new FileAdapter(new ListenedFiles());
        File file = new File(folder + "/test.txt");
        ListenedFile listenedFile=new ListenedFile(file);
        adapter.saveState(listenedFile,true);
        Thread.sleep(2000);
        assertEquals(LocalDateTime.ofInstant(new Date(file.lastModified()).toInstant(),ZoneId.systemDefault()),listenedFile.getAll().get(0).getEdited());
        assertEquals("файл удален",listenedFile.getAll().get(0).getCause());
        assertEquals(file.getName(),listenedFile.getAll().get(0).getTitle());
    }
}