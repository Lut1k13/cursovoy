package com.example.filescontrol.Watcher;

import java.io.IOException;
import java.util.EventListener;

/**
 * file listener
 */
public interface FileListener extends EventListener {
    /**
     * @param event type
     */
    void onCreated(FileEvent event) throws IOException;

    /**
     * @param event type
     * @throws IOException exception
     */
    void onModified(FileEvent event) throws IOException;

    /**
     * @param event type
     * @throws IOException exception
     */
    void onDeleted(FileEvent event) throws IOException;
}
