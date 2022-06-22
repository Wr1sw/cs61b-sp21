package gitlet;

import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static gitlet.Utils.*;

public class Blob implements Serializable {
    private final File source;

    private final byte[] content;

    private final String id;

    private final File file;

    public Blob(File sourceFile) {
        source = sourceFile;
        String filePath = sourceFile.getPath();
        content = readContents(sourceFile);
        id = sha1(filePath, content);
        file = getObjectFile(id);
    }

    public String getId() {
        return id;
    }

    public File getFile() {
        return file;
    }
    public String getContentAsString() {
        // String.valueOf(content)   bad
        return new String(content, StandardCharsets.UTF_8);
    }

    public static Blob fromFile(String id) {
        return readObject(getObjectFile(id), Blob.class);
    }

    public void save() {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
        writeObject(file, this);
    }
}
