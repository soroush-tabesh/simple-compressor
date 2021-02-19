package ir.soroushtabesh.hw3.compressor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;


class CompressorTest {

    private static final String root = "src/test/resources/";
    private static final String dir = "dirTest/";

    @Test
    @BeforeAll
    public static void createTestFiles() throws IOException {
        var contentA = new byte[]{1, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 6, 1, 2, 3};
        var contentB = new byte[]{2, 3, 4, 5, 6, 2, 3, 4, 5, 6, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6, 1};

        Path.of(root, dir).toFile().mkdirs();

        var fileA = Path.of(root, dir, "a.txt").toFile();
        var fileB = Path.of(root, dir, "b.txt").toFile();

        new PrintWriter(fileA).close();
        new PrintWriter(fileB).close();

        Files.write(fileA.toPath(), contentA);
        Files.write(fileB.toPath(), contentB);
    }

    @Test
    public void test() {
        long inl = 1157;
        int len = 3;
        byte[] data = new byte[len];

        for (int i = 0; i < len; i++) {
            data[len - 1 - i] = (byte) ((inl >> (8 * i)) & 255);
        }
//        System.out.println(Arrays.toString(data));

        long res = 0;
        for (int i = 0; i < data.length; i++) {
            res |= (255L & data[data.length - 1 - i]) << (8 * i);
        }
//        System.out.println(res);

        Assertions.assertEquals(res, inl);
    }

    @Test
    public void compress_decompress() throws Exception {
        Compressor.compress(Path.of(root, dir).toAbsolutePath().toString(),
                Path.of(root, "compressed.dat").toAbsolutePath().toString()
                , new LZ77(16, 7), "");
//        System.out.println("Success");
        Compressor.decompress(Path.of(root, "compressed.dat").toAbsolutePath().toString(),
                Path.of(root, "decompressed/").toAbsolutePath().toString(), "");
        Assertions.assertArrayEquals(
                Files.readAllBytes(Path.of(root, dir, "a.txt")),
                Files.readAllBytes(Path.of(root, "decompressed/", dir, "a.txt")));
        Assertions.assertArrayEquals(
                Files.readAllBytes(Path.of(root, dir, "b.txt")),
                Files.readAllBytes(Path.of(root, "decompressed/", dir, "b.txt")));
    }

}