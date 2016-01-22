package com.andra.samplephotosbrowser.parser;

import com.andra.samplephotosbrowser.pojo.PhotoPOJO;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the JSONParser interface that scans a JSON object containing multiple images
 * and gathers all the necessary data into a list of photo objects
 */
public class PhotosParser implements JSONParser<List<PhotoPOJO>> {

    private static final String JSON_DATA = "data";
    private static final String JSON_ASSETS = "assets";
    private static final String JSON_ID = "id";
    private static final String JSON_DATE = "added_date";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_URL_LARGE = "preview";
    private static final String JSON_URL_MEDIUM = "large_thumb";
    private static final String JSON_URL_SMALL = "small_thumb";
    private static final String JSON_URL = "url";

    @Override
    public List<PhotoPOJO> parseJSONResponse(JSONObject response) {
        List<PhotoPOJO> photos = new ArrayList<>();
        JSONArray photosArray = response.optJSONArray(JSON_DATA);
        if (photosArray == null) {
            return photos;
        }

        for (int i = 0; i < photosArray.length(); i++) {
            JSONObject photo = photosArray.optJSONObject(i);
            if (photo != null && photo.has(JSON_ASSETS)) {
                String id = photo.optJSONObject(JSON_ASSETS).optString(JSON_ID);
                String date = photo.optJSONObject(JSON_ASSETS).optString(JSON_DATE);
                String description = photo.optJSONObject(JSON_ASSETS).optString(JSON_DESCRIPTION);
                String urlLarge = photo.optJSONObject(JSON_ASSETS).optJSONObject(JSON_URL_LARGE).optString(JSON_URL);
                String urlMedium = photo.optJSONObject(JSON_ASSETS).optJSONObject(JSON_URL_MEDIUM).optString(JSON_URL);
                String urlSmall = photo.optJSONObject(JSON_ASSETS).optJSONObject(JSON_URL_SMALL).optString(JSON_URL);
                photos.add(new PhotoPOJO(id, date, description, urlLarge, urlMedium, urlSmall));
            }
        }

        return photos;
    }
}
