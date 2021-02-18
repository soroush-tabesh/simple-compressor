package ir.soroushtabesh.hw3.compressor.utils;

public class Utils {
    public static long pow(long base, long exp, long mod) {
        base %= mod;
        if (exp == 1)
            return base;
        if (exp == 0)
            return 1;
        long v = pow(base, exp / 2, mod);
        v = (v * v) % mod;
        v = (v * (exp % 2 == 1 ? base : 1)) % mod;
        return v;
    }
}
