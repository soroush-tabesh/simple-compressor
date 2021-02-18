package ir.soroushtabesh.hw3.compressor.utils;

public class WindowBuffer {
    private final int windowSize;
    private final int[] window;
    private final long[] partialHash;
    private final long[] inverseHash;
    private int ptr = 0;
    private long lastRadix = 1;

    private static final long mod = (long) (1e9 + 7);
    private static final long radix = 257;
    private static final long radInv = Utils.pow(radix, mod - 2, mod);

    public WindowBuffer(int windowSize) {
        this.windowSize = windowSize;
        window = new int[windowSize];
        partialHash = new long[windowSize];
        inverseHash = new long[windowSize];
        inverseHash[0] = 1;
    }

    public int getPointer() {
        return ptr;
    }

    public void add(int b) {
        window[ptr++ % windowSize] = b;
        partialHash[ptr % windowSize] = (partialHash[(ptr - 1) % windowSize] + lastRadix * b) % mod;
        inverseHash[ptr % windowSize] = (inverseHash[(ptr - 1) % windowSize] * radInv) % mod;
        lastRadix = (lastRadix * radix) % mod;
    }

    public int get(int index) {
        return window[index % windowSize];
    }

    public long getPartialHash(int index) {
        return partialHash[index % windowSize];
    }

    public long getInverseHash(int index) {
        return inverseHash[index % windowSize];
    }

    public long getMod() {
        return mod;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < ptr; i++) {
            sb.append("");
        }
        return sb.toString();
    }
}
