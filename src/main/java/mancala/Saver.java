package mancala;

import java.io.IOException;
import java.io.Serializable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class Saver {

    private static final String ASSETS_FOLDER = "assets/";

    static {
        // Create the "assets" folder if it doesn't exist
        createAssetsFolder();
    }

    private static void createAssetsFolder() {
        final Path assetsPath = FileSystems.getDefault().getPath(ASSETS_FOLDER);
        if (!Files.exists(assetsPath)) {
            try {
                Files.createDirectories(assetsPath);
            } catch (IOException e) {
                e.getMessage(); 
            }
        }
    }

    public static void saveObject(final Serializable toSave,final String filename) throws IOException {
        try (ObjectOutputStream objectOut = new ObjectOutputStream(new FileOutputStream(ASSETS_FOLDER + filename))) {
            objectOut.writeObject(toSave);
        }
    }

    public static Serializable loadObject(final String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ASSETS_FOLDER + filename))) {
            return (Serializable) ois.readObject();
        }
    }
}
