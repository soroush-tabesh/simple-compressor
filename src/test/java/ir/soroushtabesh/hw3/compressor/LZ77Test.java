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
        var fos = new FileOutputStream(fileComp);
        var los = new LZ77OutputStream(fos, lz77);
        for (int i : content) {
            los.write(i);
        }
        los.flush();
        los.close();
//        System.out.println(Arrays.toString(Files.readAllBytes(Path.of(pathname + "comp.dat"))));

        var fis = new FileInputStream(fileComp);
        var lis = new LZ77InputStream(fis, lz77);
        assertArrayEquals(lis.readAllBytes(), content);
//        System.out.println(Arrays.toString(lis.readAllBytes()));
//        int t = 0;
//        while((t=lis.read()) != -1){
//            System.out.printf("%d ",t);
//        }
//        System.out.println();
        lis.close();

    }

}