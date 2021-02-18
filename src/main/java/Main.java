import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("/a");
        System.out.println(path.relativize(Path.of("/a/b/s")));

//        GUI.main(args);
    }
}
