package com.example.filescontrol.Watcher;

import java.io.File;
import java.util.EventObject;

/**
 * file event
 */
public class FileEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public FileEvent(Object source) {
        super(source);
    }

    /**
     * @return file
     */
    public File getFile() {
        return (File) getSource();
    }
}
