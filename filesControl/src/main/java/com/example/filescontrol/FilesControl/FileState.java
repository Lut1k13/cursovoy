package com.example.filescontrol.FilesControl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileState {
    String title;
    LocalDateTime edited;
    String author;
    String cause;
    int id;

    /**
     * @param fileStateBuilder builder
     */
    public FileState(FileStateBuilder fileStateBuilder)
    {
        title = fileStateBuilder.title;
        edited = fileStateBuilder.edited;
        author = fileStateBuilder.author;
        cause = fileStateBuilder.cause;
        id = fileStateBuilder.id;
    }

    /**
     * @return string
     */
    public String toString()
    {
        return title + ": " + edited.format(DateTimeFormatter.ofPattern("dd.MM.yyyy kk:mm:ss"));
    }

    /**
     * @return time of edit
     */
    public LocalDateTime getEdited() {
        return edited;
    }

    /**
     * @return author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return cause of edit
     */
    public String getCause() {
        return cause;
    }

    /**
     * builder for filestate
     */
    public static class FileStateBuilder
    {
        String title;
        LocalDateTime edited;
        String author;
        String cause = "";
        int id;
        public FileStateBuilder(String title, LocalDateTime edited, String author)
        {
            this.title = title;
            this.edited = edited;
            this.author = author;
        }

        public FileStateBuilder setCause(String cause) {
            this.cause = cause;
            return this;
        }

        public FileStateBuilder setId(int id) {
            this.id = id;
            return this;
        }

        public FileState build() {
            return new FileState(this);
        }
    }
}
