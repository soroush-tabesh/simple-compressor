package ir.soroushtabesh.hw3.compressor;

import java.nio.ByteBuffer;

public class LZ77 {

    private final int windowSizeExp, bufferSizeExp;

    public LZ77(int windowSizeExp, int bufferSizeExp) {
        if (windowSizeExp < bufferSizeExp)
            throw new RuntimeException("window size can't be smaller than buffer size");
        if (windowSizeExp >= 32)
            throw new RuntimeException("too large window");
        if (bufferSizeExp >= 8)
            throw new RuntimeException("too large buffer size");
        this.windowSizeExp = windowSizeExp;
        this.bufferSizeExp = bufferSizeExp;
    }

    public int getLengthOfData() {
        return (bufferSizeExp + windowSizeExp + 7) / 8;
    }

    public int getWindowSizeExp() {
        return windowSizeExp;
    }

    public int getBufferSizeExp() {
        return bufferSizeExp;
    }

    public int getWindowSize() {
        return 1 << windowSizeExp;
    }

    public int getBufferSize() {
        return 1 << bufferSizeExp;
    }

    public byte[] encode(long length, long index) {
        if (length > getBufferSize() || index >= getWindowSize())
            throw new RuntimeException("invalid length or index");
        long res = (length - 1) | (index << getBufferSizeExp());
        byte[] data = new byte[getLengthOfData()];
        for (int i = 0; i < getLengthOfData(); i++) {
            data[getLengthOfData() - 1 - i] = (byte) ((res >> (8 * i)) & 255);
        }
//        System.err.println(length + " " + index + " " + res);
        return data;
    }

    public static long toLong(byte[] data) {
        long res = 0;
        for (int i = 0; i < data.length; i++) {
            res |= (255L & data[data.length - 1 - i]) << (8 * i);
        }
        return res;
//        return ByteBuffer.allocate(8).put(data).put(new byte[8-data.length]).getLong();
    }

    public long decodeLength(byte[] data) {
        long res = toLong(data);
        return (res & (getBufferSize() - 1)) + 1;
    }

    public long decodeIndex(byte[] data) {
        long res = toLong(data);
        return (res >> getBufferSizeExp()) & (getWindowSize() - 1);
    }
}
