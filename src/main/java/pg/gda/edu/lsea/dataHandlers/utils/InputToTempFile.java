package pg.gda.edu.lsea.dataHandlers.utils;

import java.io.*;

public class InputToTempFile {
    public static File iSToF (InputStream is) throws IOException {
        File tempFile = File.createTempFile("temp", ".json");

        try(OutputStream os = new FileOutputStream(tempFile)) {
            is.transferTo(os);
        }
        return tempFile;
    }
}
