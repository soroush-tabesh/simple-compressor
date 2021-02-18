package ir.soroushtabesh.hw3.compressor;

import ir.soroushtabesh.hw3.compressor.utils.WindowBuffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.Queue;

// LZ77 decompressor
public class LZ77InputStream extends InputStream {
    private final InputStream inputStream;
    private final LZ77 config;
    private final WindowBuffer windowBuffer;
    private final Queue<Integer> outQueue = new ArrayDeque<>();

    public LZ77InputStream(InputStream inputStream, LZ77 config) {
        this.inputStream = inputStream;
        this.config = config;
        windowBuffer = new WindowBuffer(config.getWindowSize() + 2 * config.getBufferSize() + 1);
    }

    @Override
    public int read() throws IOException {
        var temp = outQueue.poll();
        if (temp != null) {
            windowBuffer.add(temp);
            return temp;
        }
        int b = inputStream.read();
        if (b != 0) {
            windowBuffer.add(b);
            return b;
        }
        byte[] conf = inputStream.readNBytes(config.getLengthThreshold() - 1);
        long res = 0;
        for (int i = config.getLengthThreshold() - 2; i >= 0; i--) {
            res |= (long) conf[i] << 8 * i;
        }
//        System.err.println(res);
        long len = (res & ((1L << config.getBufferSizeExp()) - 1)) + 1;
        long pos = res >> config.getBufferSizeExp();
//        System.err.println(len);
//        System.err.println(pos);
//        System.err.println(((1L << config.getBufferSizeExp()) - 1));
        for (int i = (int) pos; i < len + pos; i++) {
            outQueue.add(windowBuffer.get(i));
        }
        temp = outQueue.poll();
        if (temp == null)
            throw new IOException("corrupted file");
        windowBuffer.add(temp);
        return temp;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }
}

