package ir.soroushtabesh.hw3.compressor;

import com.google.gson.GsonBuilder;
import ir.soroushtabesh.hw3.compressor.entity.Descriptor;
import ir.soroushtabesh.hw3.compressor.entity.FileEntry;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Compressor {

    private Descriptor readDescriptor(InputStream fis) throws IOException {
        byte[] lenBytes = fis.readNBytes(4);
        int len = ByteBuffer.wrap(lenBytes).getInt();
        String json = new String(fis.readNBytes(len), StandardCharsets.UTF_8);
        var gson = new GsonBuilder().create();
        return gson.fromJson(json, Descriptor.class);
    }

    // get hierarchy, password protection, compression config of lz77 file
    public Descriptor readDescriptor(String path) {
        var file = new File(path);
        try (var fis = new FileInputStream(file)) {
            return readDescriptor(fis);
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
        boolean protect = password != null && !password.isEmpty();
        var descriptor = new Descriptor(inPathName, config, protect);
        var outFile = new File(outPathName);

        OutputStream fos = new FileOutputStream(outFile);
        fos = new BufferedOutputStream(fos);

        writeDescriptor(fos, descriptor);

        if (protect) {
            var key = new SecretKeySpec(password.getBytes(), "AES");
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            fos = new CipherOutputStream(fos, cipher);
        }

        fos = new LZ77OutputStream(fos, config);

        for (FileEntry fileEntry : descriptor.getFiles()) {
            var inPath = Path.of(inPathName, fileEntry.getRelPath());
            var inFile = inPath.toFile();
            try (var fis = new BufferedInputStream(new FileInputStream(inFile))) {
                fos.write(ByteBuffer.allocate(4).putInt(fileEntry.getId()).array());
                fis.transferTo(fos);
            }
        }

        fos.flush();
        fos.close();
    }

    private void createFile(File targetFile) throws IOException {
        var parent = targetFile.getParentFile();
        if (!parent.exists() && !parent.mkdirs()) {
            throw new IllegalStateException("Couldn't create dir: " + parent);
        }
        targetFile.createNewFile();
    }

    private void copyStream(InputStream input, OutputStream output, long length)
            throws IOException {
        byte[] buffer = new byte[1024]; // Adjust if you want
        int bytesRead;
        while ((bytesRead = input.read(buffer, 0, (int) Math.min(1024, length))) != -1) {
            output.write(buffer, 0, bytesRead);
            length -= bytesRead;
        }
    }

    // decompress lz77 file
    public void decompress(String inPathName, String outPathName, String password) throws Exception {
        var inFile = new File(inPathName);
        InputStream fis = new FileInputStream(inFile);

        fis = new BufferedInputStream(fis);
        var descriptor = readDescriptor(fis);

        if (descriptor.isProtect()) {
            var key = new SecretKeySpec(password.getBytes(), "AES");
            var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            fis = new CipherInputStream(fis, cipher);
        }
        fis = new LZ77InputStream(fis, descriptor.getConfig());

        Map<Integer, FileEntry> files = new HashMap<>();
        for (FileEntry file : descriptor.getFiles()) {
            files.put(file.getId(), file);
        }

        for (int i = 0; i < descriptor.getFiles().size(); i++) {
            int id = ByteBuffer.wrap(fis.readNBytes(4)).getInt();
            var fileEntry = files.get(id);
            var file = Path.of(outPathName, fileEntry.getRelPath()).toFile();
            createFile(file);
            long length = fileEntry.getLength();

            OutputStream fos = new FileOutputStream(file);
            fos = new BufferedOutputStream(fos);
            copyStream(fis, fos, length);
            fos.flush();
            fos.close();
        }

        fis.close();

    }

}
