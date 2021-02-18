package ir.soroushtabesh.hw3.compressor.utils;

public class SubArray {
    private final WindowBuffer buffer;
    private final int start, end;

    public SubArray(WindowBuffer buffer, int start, int end) {
        this.buffer = buffer;
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return end - start;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubArray subArray = (SubArray) o;
        if (start == subArray.start && end == subArray.end)
            return true;
        if (getLength() != subArray.getLength())
            return false;
        for (int i = 0; i < getLength(); i++) {
            if (buffer.get(i + start) != subArray.buffer.get(i + subArray.start))
                return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        var temp = (buffer.getPartialHash(end) - buffer.getPartialHash(start) + buffer.getMod()) % buffer.getMod();
        temp = (temp * buffer.getInverseHash(start)) % buffer.getMod();
        return (int) temp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = start; i < end - 1; i++) {
            sb.append(buffer.get(i));
            sb.append(" ,");
        }
        sb.append(buffer.get(end - 1));
        sb.append("]");
//        sb.append(" : ")
//        sb.append(hashCode());
        return sb.toString();
    }
}
