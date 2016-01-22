package com.andra.samplephotosbrowser.parser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the JSONParser interface that scans a JSON object containing multiple image
 * categories and compiles a list containing only the category names
 */
public class CategoriesParser implements JSONParser<List<String>> {
    private static final String JSON_DATA = "data";
    private static final String JSON_CATEGORY_NAME = "name";

    @Override
    public List<String> parseJSONResponse(JSONObject response) {
        List<String> categories = new ArrayList<>();
        JSONArray categoriesArray = response.optJSONArray(JSON_DATA);
        if (categoriesArray == null) {
            return categories;
        }

        for (int i = 0; i < categoriesArray.length(); i++) {
            JSONObject category = categoriesArray.optJSONObject(i);
            if (category != null && category.has(JSON_CATEGORY_NAME)) {
                categories.add(category.optString(JSON_CATEGORY_NAME));
            }
        }

        return categories;
    }
}
