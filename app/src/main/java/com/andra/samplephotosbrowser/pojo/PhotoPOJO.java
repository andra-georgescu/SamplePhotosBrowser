package com.andra.samplephotosbrowser.pojo;

import java.io.Serializable;

/**
 * This class models the data Shutterstock api sends for a picture
 */
public class PhotoPOJO implements Serializable {
    private String mId;
    private String mDate;
    private String mDescription;
    private String mUrlLarge;
    private String mUrlMedium;
    private String mUrlSmall;

    public PhotoPOJO(String id, String date, String description, String urlLarge, String urlMedium, String urlSmall) {
        mId = id;
        mDate = date;
        mDescription = description;
        mUrlLarge = urlLarge;
        mUrlMedium = urlMedium;
        mUrlSmall = urlSmall;
    }

    public String getId() {
        return mId;
    }

    public String getDate() {
        return mDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl() {
        if (mUrlLarge != null && !mUrlLarge.isEmpty()) {
            return mUrlLarge;
        }

        if (mUrlMedium != null && !mUrlMedium.isEmpty()) {
            return mUrlMedium;
        }

        return mUrlSmall;
    }
}
