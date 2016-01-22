package com.andra.samplephotosbrowser.network;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.andra.samplephotosbrowser.parser.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * You can use this class to make request to the Shutterstock API
 * @param <T> the type of the object your are waiting for in response
 */
public class ShutterstockRequestTask<T> extends AsyncTask<String, String, String> {

    private final Callback<T> mOnResultListener;
    private JSONParser<T> mParser;

    /**
     * Creates a new AsyncTask for GET requests to the Shutterstock API
     * @param parser provide a JSONParser that should be used to parse any successful response
     * @param onResponseCallback an optional callback to be used when this task finishes, either
     *                           with a success or with a failure (can be null if there is nothing to be done)
     */
    public ShutterstockRequestTask(JSONParser<T> parser, Callback<T> onResponseCallback) {
        mParser = parser;
        mOnResultListener = onResponseCallback;
    }

    @Override
    protected String doInBackground(String... uri) {
        String responseString = "";
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(uri[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            // This is used to authenticate this client to the Shutterstock API
            String userCredentials = ShutterstockHelper.getCredentials();
            String basicAuth = "Basic " + Base64.encodeToString(userCredentials.getBytes(), Base64.NO_WRAP);
            urlConnection.setRequestProperty("Authorization", basicAuth);

            InputStream in = urlConnection.getInputStream();
            responseString = readStream(in);
        } catch (Exception e) {
            Log.e(ShutterstockRequestTask.class.getSimpleName(),
                    "There was an error getting the content from the api", e);
            if (mOnResultListener != null) {
                mOnResultListener.onError();
            }
        } finally {
            // Don't forget to erase any traces left behind by our request
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return responseString;
    }

    // Takes the response coming from the server and puts it in a readable String
    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            // when the request completes, try to respond to the requesting thread with a parsed response
            if (mOnResultListener != null) {
                mOnResultListener.onSuccess(mParser.parseJSONResponse(new JSONObject(result)));
            }
        } catch (JSONException e) {
            // if the parsing fails, then the error listener takes its place
            Log.e(ShutterstockRequestTask.class.getSimpleName(),
                    "There was an error parsing the server response", e);
            if (mOnResultListener != null) {
                mOnResultListener.onError();
            }
        }
    }
}