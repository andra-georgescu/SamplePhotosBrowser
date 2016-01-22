package com.andra.samplephotosbrowser.network;

import android.content.Context;

import com.andra.samplephotosbrowser.R;
import com.andra.samplephotosbrowser.application.SamplePhotosBrowserApplication;

/**
 * Simple helper class for providing "know-nothing" requesters with their much needed urls and credentials
 */
public class ShutterstockHelper {
    private static final int RESULTS_PER_PAGE = 100;

    public static String getQueryUrl(Context context, String searchTerm) {
        String endpointUrl = context.getString(R.string.shutterstock_search_query_endpoint,
                searchTerm, String.valueOf(RESULTS_PER_PAGE));
        return getBaseUrl(context) + endpointUrl;
    }

    public static String getAllCategoriesUrl(Context context) {
        return getBaseUrl(context) + context.getString(R.string.shutterstock_list_categories_endpoint);
    }

    public static String getPhotosInCategoryUrl(Context context, String category) {
        String endpointUrl = context.getString(R.string.shutterstock_get_all_in_category_endpoint,
                category, String.valueOf(RESULTS_PER_PAGE));
        return getBaseUrl(context) + endpointUrl;
    }

    private static String getBaseUrl(Context context) {
        return context.getString(R.string.shutterstock_base_url);
    }

    public static String getCredentials() {
        return SamplePhotosBrowserApplication.getShutterstockClientId()
                + ":" + SamplePhotosBrowserApplication.getShutterstockApiKey();
    }
}
