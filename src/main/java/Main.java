import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/a");
        System.out.println(path.relativize(Path.of("/a/b/s")));
//        var targetFile = new File("foo/bar/phleem.css");
//        var parent = targetFile.getParentFile();
//        if (!parent.exists() && !parent.mkdirs()) {
//            throw new IllegalStateException("Couldn't create dir: " + parent);
//        }
//        targetFile.createNewFile();
//        GUI.main(args);
    }
}
