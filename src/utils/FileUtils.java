package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by theapache64 on 15/6/17.
 */
public class FileUtils {
    public static String read(File configFile) throws IOException {
        final BufferedReader br = new BufferedReader(new FileReader(configFile));
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return sb.toString();
    }
}
