package ir.soroushtabesh.hw3.compressor;

import com.google.gson.GsonBuilder;
import ir.soroushtabesh.hw3.compressor.entity.Descriptor;
import ir.soroushtabesh.hw3.compressor.entity.FileEntry;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class Compressor {

    // get hierarchy, password protection, compression config of lz77 file
    public Descriptor getFileInfo(String path) {
        var file = new File(path);
        try (var fis = new FileInputStream(file)) {
            byte[] lenBytes = fis.readNBytes(4);
            int len = ByteBuffer.wrap(lenBytes).getInt();
            String json = new String(fis.readNBytes(len), StandardCharsets.UTF_8);
            var gson = new GsonBuilder().create();
            return gson.fromJson(json, Descriptor.class);
        } catch (Exception ignored) {
            return null;
        }
    }

    // generate byte array of config for a file or directory
    private void writeDescriptor(OutputStream outputStream, Descriptor descriptor) throws IOException {
        var gson = new GsonBuilder().create();
        byte[] data = gson.toJson(descriptor).getBytes(StandardCharsets.UTF_8);
        var buffer = ByteBuffer.allocate(4 + data.length);
        buffer.putInt(data.length);
        buffer.put(data);
        outputStream.write(buffer.array());
    }

    // compress a file or directory to a lz77 file
    public void compress(String inPathName, String outPathName, LZ77 config, String password) throws Exception {
        var descriptor = new Descriptor(inPathName, config, password != null);
        var outFile = new File(outPathName);

        OutputStream fos = new FileOutputStream(outFile);
        fos = new BufferedOutputStream(fos);

        writeDescriptor(fos, descriptor);

        if (password == null || password.isEmpty()) {
            fos = new LZ77OutputStream(fos, config);
        } else {
            var key = new SecretKeySpec(password.getBytes(), "AES");
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            CipherOutputStream cos = new CipherOutputStream(fos, cipher);
            fos = new LZ77OutputStream(cos, config);
        }

        for (FileEntry fileEntry : descriptor.getFiles()) {
            var inPath = Path.of(inPathName, fileEntry.getRelPath());
            var inFile = inPath.toFile();
            try (var fis = new BufferedInputStream(new FileInputStream(inFile))) {
                fos.write(ByteBuffer.allocate(4).putInt(fileEntry.getId()).array());
                fos.write(ByteBuffer.allocate(8).putLong(fileEntry.getSize()).array());
                fis.transferTo(fos);
            }
        }

        fos.flush();
        fos.close();
    }

    // decompress lz77 file
    public void decompress(String inPath, String outPath, String password) {

    }

}
