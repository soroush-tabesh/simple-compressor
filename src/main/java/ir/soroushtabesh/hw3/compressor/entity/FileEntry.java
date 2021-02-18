package ir.soroushtabesh.hw3.compressor.entity;

import java.io.Serializable;
import java.util.Objects;

public class FileEntry implements Serializable {
    private final String relPath;
    private final long length;

    public FileEntry(String relPath, long length) {
        this.relPath = relPath;
        this.length = length;
    }

    public String getRelPath() {
        return relPath;
    }


    public long getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileEntry fileEntry = (FileEntry) o;
        return getLength() == fileEntry.getLength() && Objects.equals(getRelPath(), fileEntry.getRelPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRelPath(), getLength());
    }

    @Override
    public String toString() {
        return relPath;
    }
}
