package ir.soroushtabesh.hw3.compressor;

import ir.soroushtabesh.hw3.compressor.utils.SubArray;
import ir.soroushtabesh.hw3.compressor.utils.WindowBuffer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

// LZ77 compressor
public class LZ77OutputStream extends OutputStream {
    private final OutputStream outputStream;
    private final LZ77 config;
    private final WindowBuffer windowBuffer;
    private int lastPtr = 0; // first unwritten byte in window buffer - last written exclusive pointer
    private final Map<SubArray, Deque<SubArray>> dict = new HashMap<>();

    public LZ77OutputStream(OutputStream outputStream, LZ77 config) {
        this.outputStream = outputStream;
        this.config = config;
        windowBuffer = new WindowBuffer(config.getWindowSize() + 2 * config.getBufferSize() + 1);
    }

    private void addToDict(int lastIndex, int offset) { // exclusive index
        if (offset <= 0)
            return;
        for (int len = 1; len <= Math.min(config.getBufferSize(), lastIndex - offset + 1); len++) {
            SubArray subArray;
            Deque<SubArray> queue;
            try {
                subArray = new SubArray(windowBuffer, lastIndex - offset + 1 - len, lastIndex - offset + 1);
//            System.out.println(subArray);
                queue = dict.getOrDefault(subArray, new ArrayDeque<>(2));
                dict.remove(subArray);
                queue.add(subArray);
                dict.put(subArray, queue);
            } catch (Exception e) {
                System.err.printf("%d,%d\n", lastIndex - offset + 1 - len, lastIndex - offset + 1);
                System.err.printf("%d,%d,%d\n", lastIndex, offset, len);
                throw e;
            }
        }
        if (lastIndex > config.getWindowSize())
            removeFromDict(lastIndex - config.getWindowSize() - offset);
        addToDict(lastIndex, offset - 1);
    }

    private void removeFromDict(int firstIndex) { //inclusive index
        for (int len = 1; len <= Math.min(windowBuffer.getPointer() - firstIndex, config.getBufferSize()); len++) {
            var subArray = new SubArray(windowBuffer, firstIndex, firstIndex + len);
            var queue = dict.get(subArray);
            queue.poll();
            if (queue.isEmpty())
                dict.remove(subArray);
        }
    }

    @Override
    public void write(int b) throws IOException {
        if (b == 0)
            throw new IOException("can't write zero in output.");

        windowBuffer.add((byte) b); //fixme: start id for write

        var subArray = new SubArray(windowBuffer, lastPtr, windowBuffer.getPointer());
        var match = dict.get(subArray);

        if (match == null || match.isEmpty()) {

            if (subArray.getLength() == 1) {
                addToDict(++lastPtr, 1);
                outputStream.write(b);
                return;
            }

            // create previous subarray of buffer
            subArray = new SubArray(windowBuffer, lastPtr, windowBuffer.getPointer() - 1);

            // search in window
            match = dict.get(subArray);

            if (subArray.getLength() > config.getLengthOfData() + 1) {
                // compress
                outputStream.write(0);
                byte[] data = config.encode(subArray.getLength(), match.peekLast().getStart());
                outputStream.write(data);
            } else {
                // no compress
                for (int i = subArray.getStart(); i < subArray.getEnd(); i++) {
                    outputStream.write(windowBuffer.get(i));
                }
            }
            lastPtr = subArray.getEnd();
            addToDict(lastPtr, subArray.getLength());

            // take care of last character
            subArray = new SubArray(windowBuffer, lastPtr, windowBuffer.getPointer());
            match = dict.get(subArray);

            if (match == null || match.isEmpty()) {
                addToDict(++lastPtr, 1);
                outputStream.write(b);
            }

        }
    }

    @Override
    public void flush() throws IOException {
        while (lastPtr < windowBuffer.getPointer()) {
            outputStream.write(windowBuffer.get(lastPtr++));
        }
        outputStream.flush();
    }

    @Override
    public void close() throws IOException {
//        var sub1 = new SubArray(windowBuffer,10,12);
//        var sub2 = new SubArray(windowBuffer,4,6);
//        System.out.println(sub1);
//        System.out.println(sub2);
//        System.out.println(sub1.equals(sub2) ? 1 : 0);
//        StringBuilder t = new StringBuilder("{");
//        for (var entry : dict.entrySet()) {
//            t.append(entry.getKey().hashCode()).append(" : ").append(entry.getValue()).append(", \n");
//        }
//        t.append("}");
//        System.out.println(t);
        outputStream.close();
    }
}
