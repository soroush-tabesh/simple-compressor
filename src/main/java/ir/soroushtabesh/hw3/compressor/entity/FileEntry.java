package ir.soroushtabesh.hw3.compressor.entity;

import java.io.Serializable;
import java.util.Objects;

public class FileEntry implements Serializable {
    private final String relPath;
    private final int id;
    private final long length;

    public FileEntry(String relPath, int id, long length) {
        this.relPath = relPath;
        this.id = id;
        this.length = length;
    }

    public String getRelPath() {
        return relPath;
    }

    public int getId() {
        return id;
    }

    public long getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntry fileEntry = (FileEntry) o;
        return getId() == fileEntry.getId() && getLength() == fileEntry.getLength() && Objects.equals(getRelPath(), fileEntry.getRelPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRelPath(), getId(), getLength());
    }
}
