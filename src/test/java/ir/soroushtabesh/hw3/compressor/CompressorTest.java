package ir.soroushtabesh.hw3.compressor;

import org.junit.jupiter.api.Test;

class CompressorTest {

    @Test
    public void compress_decompress() throws Exception {
        Compressor.compress("/Users/soroushtabesh/IdeaProjects/simple-compressor/src/main/resources/dirTest",
                "/Users/soroushtabesh/IdeaProjects/simple-compressor/src/main/resources/comp.dat"
                , new LZ77(16, 7), "");
        System.out.println("Success");
        Compressor.decompress("/Users/soroushtabesh/IdeaProjects/simple-compressor/src/main/resources/comp.dat",
                "/Users/soroushtabesh/IdeaProjects/simple-compressor/src/main/resources/dir/", "");
    }

}