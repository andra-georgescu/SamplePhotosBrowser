package com.andra.samplephotosbrowser.adapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andra.samplephotosbrowser.network.DownloadImageTask;
import com.andra.samplephotosbrowser.pojo.PhotoPOJO;
import com.andra.samplephotosbrowser.R;

import java.util.List;

/**
 * Simple adapter that fills an ImageView with a network image provided by a PhotoPOJO
 */
public class PhotosStaggeredGridAdapter extends RecyclerView.Adapter<PhotosStaggeredGridAdapter.PhotoViewHolder> {

    private List<PhotoPOJO> mPhotos;

    /**
     * Provides a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mPhotoView;

        public PhotoViewHolder(View v) {
            super(v);

            mPhotoView = (ImageView) v.findViewById(R.id.photo);
        }

        public ImageView getPhotoView() {
            return mPhotoView;
        }
    }

    public PhotosStaggeredGridAdapter(List<PhotoPOJO> photos) {
        mPhotos = photos;
    }

    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item, null);
        PhotoViewHolder vh = new PhotoViewHolder(layoutView);
        return vh;
    }

    @Override
    public void onBindViewHolder(PhotoViewHolder holder, int position) {
        // This is useful because the views are being recycled, but there is a delay in loading the
        // new images from their network locations
        holder.getPhotoView().setImageDrawable(new ColorDrawable(Color.TRANSPARENT));
        new DownloadImageTask(holder.getPhotoView())
                .execute(mPhotos.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return mPhotos.size();
    }

    public void refreshImages(List<PhotoPOJO> photos) {
        mPhotos.clear();
        mPhotos.addAll(photos);
        notifyDataSetChanged();
    }
}