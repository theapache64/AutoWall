
package utils;

import models.Image;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shifar on 14/10/16.
 */
public class GPix {

    private static final String API_URL_FORMAT = "http://theapache64.xyz:8080/gpix/v1/gpix?Authorization=WYAfuHwjCu&keyword=%s&limit=%d";
    private static final String X = GPix.class.getSimpleName();


    private static String getEncodedUrl(String data) throws UnsupportedEncodingException {
        return URLEncoder.encode(data, "UTF-8");
    }

    public static String getUrl(final String keyword, final int limit) throws UnsupportedEncodingException {
        return String.format(API_URL_FORMAT, getEncodedUrl(keyword), limit);
    }

    public static List<Image> parse(JSONArray jaImages) throws JSONException {
        List<Image> imageList = new ArrayList<Image>(jaImages.length());

        for (int i = 0; i < jaImages.length(); i++) {
            final JSONObject joImage = jaImages.getJSONObject(i);

            final String imageUrl = joImage.getString("image_url");
            final String thumbUrl = joImage.getString("thumb_url");
            final int width = joImage.getInt("width");
            final int height = joImage.getInt("height");

            imageList.add(new Image(thumbUrl, imageUrl, height, width));

        }
        return imageList;
    }




    public static class GPixException extends Exception {
        public GPixException(String message) {
            super(message);
        }
    }


}
