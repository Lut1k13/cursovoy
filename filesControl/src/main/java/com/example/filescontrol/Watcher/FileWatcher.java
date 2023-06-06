package com.example.filescontrol.Watcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.nio.file.StandardWatchEventKinds.*;

/**
 * file watcher
 */
public class FileWatcher implements Runnable {
    protected List<FileListener> listeners = new ArrayList<>();
    protected final File folder;
    protected static final List<WatchService> watchServices = new ArrayList<>();
    public FileWatcher(File folder) {
        this.folder = folder;
    }

    /**
     */
    public void watch() {
        if (folder.exists()) {
            Thread thread = new Thread(this);
            thread.setDaemon(true);
            thread.start();
        }
    }

    /**
     * run new watcher
     */
    @Override
    public void run() {
        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            Path path = Paths.get(folder.getAbsolutePath());
            path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
            watchServices.add(watchService);
            boolean poll = true;
            while (poll) {
                poll = pollEvents(watchService);
            }
        } catch (IOException | InterruptedException | ClosedWatchServiceException e) {
            Thread.currentThread().interrupt();

        }
    }

    /**
     * @param watchService listener
     * @return reset
     * @throws InterruptedException exception
     * @throws IOException exception
     */
    protected boolean pollEvents(WatchService watchService) throws InterruptedException, IOException {
        WatchKey key = watchService.take();
        Path path = (Path) key.watchable();
        for (WatchEvent<?> event : key.pollEvents()) {
            Thread.sleep(100);
            notifyListeners(event.kind(), path.resolve((Path) event.context()).toFile());
        }
        return key.reset();
    }

    /**
     * @param kind of event
     * @param file file
     * @throws IOException exception
     * @throws InterruptedException exception
     */
    protected void notifyListeners(WatchEvent.Kind<?> kind, File file) throws IOException, InterruptedException {
        FileEvent event = new FileEvent(file);
        if (kind == ENTRY_CREATE) {
            for (FileListener listener : listeners) {
                listener.onCreated(event);
            }
            if (file.isDirectory()) {
                new FileWatcher(file).setListeners(listeners).watch();
            }
        }
        else if (kind == ENTRY_MODIFY) {
            for (FileListener listener : listeners) {
                listener.onModified(event);
            }
        }
        else if (kind == ENTRY_DELETE) {

            for (FileListener listener : listeners) {
                listener.onDeleted(event);
            }
        }
    }

    /**
     * @param listener listener
     * @return watcher
     */
    public FileWatcher addListener(FileListener listener) {
        listeners.add(listener);
        return this;
    }

    /**
     * @param listener listener
     * @return watcher
     */
    public FileWatcher removeListener(FileListener listener) {
        listeners.remove(listener);
        return this;
    }

    /**
     * @return list of listeners
     */
    public List<FileListener> getListeners() {
        return listeners;
    }

    /**
     * @param listeners list of listeners
     * @return watcher
     */
    public FileWatcher setListeners(List<FileListener> listeners) {
        this.listeners = listeners;
        return this;
    }

    /**
     * @return list of watchServices
     */
    public static List<WatchService> getWatchServices() {
        return Collections.unmodifiableList(watchServices);
    }
}
