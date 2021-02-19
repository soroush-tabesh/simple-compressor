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

        byte[] data = inputStream.readNBytes(config.getLengthOfData());
        long len = config.decodeLength(data);
        long pos = windowBuffer.getPointer() - config.decodeIndex(data) + len;

//        System.out.println(len + " " + pos + " " + LZ77.toLong(data));
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

