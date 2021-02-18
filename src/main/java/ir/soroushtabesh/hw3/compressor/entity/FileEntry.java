package ir.soroushtabesh.hw3.compressor.entity;

import java.io.Serializable;

public class FileEntry implements Serializable {
    private final String relPath;
    private final int id;
    private final long size;

    public FileEntry(String relPath, int id, long size) {
        this.relPath = relPath;
        this.id = id;
        this.size = size;
    }

    public String getRelPath() {
        return relPath;
    }

    public int getId() {
        return id;
    }

    public long getSize() {
        return size;
    }
}
