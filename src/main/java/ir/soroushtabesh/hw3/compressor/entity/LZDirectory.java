package ir.soroushtabesh.hw3.compressor.entity;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LZDirectory implements Serializable {
    private final String name;
    private final List<LZDirectory> subDirs = new ArrayList<>();
    private final List<LZFile> files = new ArrayList<>();

    public LZDirectory(File file) {
        if (file.isDirectory()) {
            name = file.getName();
            for (File element : file.listFiles()) {
                if (element.isDirectory()) {
                    subDirs.add(new LZDirectory(element));
                } else {
                    files.add(new LZFile(element.getName(), element.hashCode()));
                }
            }
        } else {
            name = "root" + file.hashCode();
            files.add(new LZFile(file.getName(), file.hashCode()));
        }
    }

    public String getName() {
        return name;
    }

    public void addDir(LZDirectory directory) {
        subDirs.add(directory);
    }

    public void addFile(LZFile file) {
        files.add(file);
    }

    public List<LZDirectory> getSubDirs() {
        return Collections.unmodifiableList(subDirs);
    }

    public List<LZFile> getFiles() {
        return Collections.unmodifiableList(files);
    }
}
