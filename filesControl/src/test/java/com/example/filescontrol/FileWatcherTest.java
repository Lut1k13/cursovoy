package com.example.filescontrol;

import com.example.filescontrol.FilesControl.ListenedFiles;
import com.example.filescontrol.Watcher.FileAdapter;
import com.example.filescontrol.Watcher.FileEvent;
import com.example.filescontrol.Watcher.FileWatcher;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileWatcherTest {
    @Test
    public void test() throws IOException, InterruptedException {
        File folder = new File("src/test/resources");
        final Map<String, String> map = new HashMap<>();
        FileWatcher watcher = new FileWatcher(folder);
        watcher.addListener(new FileAdapter(new ListenedFiles()) {
            public void onCreated(FileEvent event) {
                map.put("file.created", event.getFile().getName());
            }
            public void onModified(FileEvent event) {
                map.put("file.modified", event.getFile().getName());
            }
            public void onDeleted(FileEvent event) {
                map.put("file.deleted", event.getFile().getName());
            }
        }).watch();
        assertEquals(1, watcher.getListeners().size());
        wait(2000);
        File file = new File(folder + "/test.txt");
        try(FileWriter writer = new FileWriter(file)) {
            writer.write("Some String");
        }
        wait(2000);
        file.delete();
        wait(2000);
        assertEquals(file.getName(), map.get("file.created"));
        assertEquals(file.getName(), map.get("file.modified"));
    }
    public void wait(int time) throws InterruptedException {
        Thread.sleep(time);
    }
}