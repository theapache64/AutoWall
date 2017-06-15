package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkHelper {

    public static String downloadHtml(final String url) throws IOException {

        final URL theURL = new URL(url);
        final HttpURLConnection urlCon = (HttpURLConnection) theURL.openConnection();
        final BufferedReader br = new BufferedReader(new InputStreamReader(urlCon.getResponseCode() == 200 ? urlCon.getInputStream() : urlCon.getErrorStream()));
        final StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }

        br.close();

        final String data = sb.toString();
        return !data.isEmpty() ? data : null;
    }

    private static String getFileNameFromUrl(String downloadUrl) {
        final String[] parts = downloadUrl.split("/");
        return parts[parts.length - 1];
    }
}
