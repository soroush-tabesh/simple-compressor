package ir.soroushtabesh.hw3.compressor;

public class LZ77 {

    private final int windowSize, bufferSize;
    private final int windowSizeExp, bufferSizeExp;
    private final int lengthThreshold;

    public LZ77(int windowSizeExp, int bufferSizeExp) {
        if (windowSizeExp < bufferSizeExp)
            throw new RuntimeException("window size can't be smaller than buffer size");
        if (windowSizeExp >= 32)
            throw new RuntimeException("too large window");
        if (bufferSizeExp >= 8)
            throw new RuntimeException("too large buffer size");
        this.windowSize = 1 << windowSizeExp;
        this.bufferSize = 1 << bufferSizeExp;
        this.windowSizeExp = windowSizeExp;
        this.bufferSizeExp = bufferSizeExp;
        lengthThreshold = 1 + (bufferSizeExp + windowSizeExp + 7) / 8;
    }

    public int getLengthThreshold() {
        return lengthThreshold;
    }

    public int getWindowSizeExp() {
        return windowSizeExp;
    }

    public int getBufferSizeExp() {
        return bufferSizeExp;
    }

    public int getWindowSize() {
        return windowSize;
    }

    public int getBufferSize() {
        return bufferSize;
    }
}
