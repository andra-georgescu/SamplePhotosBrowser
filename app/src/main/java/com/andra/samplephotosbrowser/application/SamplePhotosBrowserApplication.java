package com.andra.samplephotosbrowser.application;

import android.app.Application;
import android.content.Context;

public class SamplePhotosBrowserApplication extends Application {

    private static final String SHUTTERSTOCK_CLIENT_ID = "506796208c413c795cdf";
    private static final String SHUTTERSTOCK_API_KEY = "2b446aabf6348de9b6f59a570d48c55a85f3e6ac";

    private static SamplePhotosBrowserApplication mInstance;
    private static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        setAppContext(getApplicationContext());
    }

    public static SamplePhotosBrowserApplication getInstance() {
        return mInstance;
    }

    public static Context getAppContext() {
        return mAppContext;
    }

    public static String getShutterstockClientId() {
        return SHUTTERSTOCK_CLIENT_ID;
    }

    public static String getShutterstockApiKey() {
        return SHUTTERSTOCK_API_KEY;
    }

    public void setAppContext(Context appContext) {
        mAppContext = appContext;
    }
}