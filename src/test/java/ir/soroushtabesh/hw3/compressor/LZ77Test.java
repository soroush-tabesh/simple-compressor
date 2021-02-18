package ir.soroushtabesh.hw3.compressor;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

class LZ77Test {

    @Test
    public void compressionTest() throws IOException {
        var content = new byte[]{1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3};
        var lz77 = new LZ77(4, 2);

        var fileComp = new File("comp.dat");
        fileComp.deleteOnExit();
        if (!fileComp.createNewFile()) {
            PrintWriter writer = new PrintWriter(fileComp);
            writer.close();
        }
        try (var fos = new FileOutputStream(fileComp);
             var los = new LZ77OutputStream(fos, lz77)) {
            for (int i : content) {
                los.write(i);
            }
            los.flush();
        }
        try (var fis = new FileInputStream(fileComp);
             var lis = new LZ77InputStream(fis, lz77)) {
            assertArrayEquals(lis.readAllBytes(), content);
        }
    }
}