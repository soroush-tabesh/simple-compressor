package ir.soroushtabesh.hw3.compressor;

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

    public int getLengthThreshold() {
        return 1 + (bufferSizeExp + windowSizeExp + 7) / 8;
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
}
