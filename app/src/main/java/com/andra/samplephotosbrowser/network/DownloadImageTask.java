package com.andra.samplephotosbrowser.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

/**
 * You can use this class to load images from the network into a ImageView you must supply in the constructor.
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    ImageView mImageView;

    public DownloadImageTask(ImageView imageView) {
        mImageView = imageView;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap bitmap = null;
        try {
            InputStream in = new URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e(DownloadImageTask.class.getSimpleName(), "Error downloading image", e);
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap result) {
        // We have downloaded our image, now apply it on the view
        mImageView.setImageBitmap(result);
    }
}
