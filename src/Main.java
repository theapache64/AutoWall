import models.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.FileUtils;
import utils.GPix;
import utils.NetworkHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Main {

    private static final String APP_FOLDER = System.getProperty("user.home") + File.separator + "AutoWall";

    private static final String CONFIG_FILE = APP_FOLDER + File.separator + "config.json";
    private static final int LIMIT = 10;
    private static final Random random = new Random();
    //Default configuration
    private static final JSONObject joDefaultConfig = new JSONObject();

    static {
        try {
            joDefaultConfig.put("sleep", (1000 * 10)); // 5 minutes
            joDefaultConfig.put("keywords", new JSONArray().put("Spiderman"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException, JSONException, InterruptedException {


        //Creating app folder
        final File appFolder = new File(APP_FOLDER);
        if (!appFolder.exists()) {
            //noinspection ResultOfMethodCallIgnored
            appFolder.mkdirs();

            System.out.println("AutoWall folder created");
        }

        //Creating config file
        final File configFile = new File(CONFIG_FILE);
        if (!configFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configFile.createNewFile();

            System.out.println("Config file created");

            //Writing default config
            FileWriter fw = new FileWriter(configFile);
            fw.write(joDefaultConfig.toString());
            fw.flush();
            fw.close();
        }

        String configData = FileUtils.read(configFile);
        final JSONObject joConfig = new JSONObject(configData);
        final JSONArray jaKeywords = joConfig.getJSONArray("keywords");
        final List<String> keywords = prettify(jaKeywords);

        final int totalImages = keywords.size() * LIMIT;
        System.out.println("Total images going to load: " + totalImages);
        final List<Image> images = new ArrayList<>(totalImages);

        for (final String keyword : keywords) {
            final String apiUrl = GPix.getUrl(keyword, LIMIT);
            System.out.println("Connecting to : " + apiUrl);
            final String networkResp = NetworkHelper.downloadHtml(apiUrl);
            if (networkResp != null) {
                final JSONObject joResp = new JSONObject(networkResp);
                final String message = joResp.getString("message");
                if (!joResp.getBoolean("error")) {
                    final JSONArray jaImages = joResp.getJSONObject("data").getJSONArray("images");
                    images.addAll(GPix.parse(jaImages));
                } else {
                    System.out.println("Error: " + message);
                }
            } else {
                System.out.println("Couldn't connect: " + apiUrl);
            }
        }

        System.out.println("Total images downloaded: " + images.size());
        final long sleepTime = joConfig.getLong("sleep");
        System.out.println("Sleeping time : " + sleepTime + "ms");
        while (true) {
            System.out.println("------------------------------");
            final Image randomImage = images.get(random.nextInt(images.size()));
            System.out.println("Changing wallpaper: " + randomImage.getImageUrl());
            setWallpaper(randomImage);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------------------------------");
        }
    }

    private static List<String> prettify(JSONArray jaKeywords) throws JSONException {
        final List<String> arr = new ArrayList<>();
        for (int i = 0; i < jaKeywords.length(); i++) {
            final String s = jaKeywords.getString(i);
            if (s != null && !s.trim().isEmpty()) {
                arr.add(s + " HD Wallpaper 2017");
            }
        }

        return arr;
    }

    private static final String COMMAND_FORMAT_DOWNLOAD_AND_SET = "wget %s -O %s";
    private static final String COMMAND_FORMAT_SET = "gsettings set org.gnome.desktop.background picture-uri file://%s";

    public static void setWallpaper(Image wallpaper) throws IOException, InterruptedException {
        final String imagePath = APP_FOLDER + File.separator + wallpaper.getName();
        final File imageFile = new File(imagePath);
        String command;
        if (!imageFile.exists()) {
            //Download and set as wallpaper
            command = String.format(COMMAND_FORMAT_DOWNLOAD_AND_SET, wallpaper.getImageUrl(), imagePath);
            Runtime.getRuntime().exec(command).waitFor();
        }
        //just set as wallpaper
        command = String.format(COMMAND_FORMAT_SET, imagePath);

        Runtime.getRuntime().exec(command).waitFor();
    }
}
