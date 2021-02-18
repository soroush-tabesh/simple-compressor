package ir.soroushtabesh.hw3.compressor.entity;

import ir.soroushtabesh.hw3.compressor.LZ77;

import java.io.File;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Descriptor implements Serializable {
    private final List<FileEntry> files = new ArrayList<>();
    private final LZ77 config;
    private final boolean protect;

    public Descriptor(String path, LZ77 config, boolean protect) {
        this.config = config;
        this.protect = protect;
        discover(Path.of(path), new File(path));
    }

    private void discover(Path root, File file) {
        if (file.isDirectory()) {
            for (File element : file.listFiles()) {
                discover(root, element);
            }
        } else {
            files.add(new FileEntry(root.relativize(file.toPath()).toString(), file.hashCode(), file.length()));
        }
    }

    public List<FileEntry> getFiles() {
        return Collections.unmodifiableList(files);
    }

    public LZ77 getConfig() {
        return config;
    }
}
