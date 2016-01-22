package com.andra.samplephotosbrowser.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andra.samplephotosbrowser.R;
import com.andra.samplephotosbrowser.activity.PhotosCategoriesActivity;
import com.andra.samplephotosbrowser.adapter.PhotosStaggeredGridAdapter;
import com.andra.samplephotosbrowser.network.Callback;
import com.andra.samplephotosbrowser.network.ShutterstockHelper;
import com.andra.samplephotosbrowser.network.ShutterstockRequestTask;
import com.andra.samplephotosbrowser.parser.PhotosParser;
import com.andra.samplephotosbrowser.pojo.PhotoPOJO;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment containing a simple RecyclerView with a StaggeredGridLayoutManager,
 * for displaying images while preserving their aspect ratios
 */
public class CategoryFragment extends Fragment {

    // arguments keys
    private static final String ARG_CATEGORY_NAME = "categoryName";
    private static final String ARG_PHOTOS = "photos";

    private String mCategoryName;

    private TextView mNoContent;
    private ProgressBar mLoading;
    private RecyclerView mRecyclerView;
    private PhotosStaggeredGridAdapter mPhotosAdapter;
    private ArrayList<PhotoPOJO> mPhotos;

    private Activity mParentActivity;

    public CategoryFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given category name.
     */
    public static CategoryFragment newInstance(String categoryName) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY_NAME, categoryName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_photos_categories, container, false);

        mParentActivity = getActivity();

        // if we have some old state remembered, we can restore it
        Bundle arguments = getArguments() == null ? savedInstanceState : getArguments();
        if (arguments != null) {
            mCategoryName = arguments.getString(ARG_CATEGORY_NAME);
            mPhotos = (ArrayList<PhotoPOJO>) arguments.getSerializable(ARG_PHOTOS);
        }

        if (mPhotos == null) {
            mPhotos = new ArrayList<>();
        }

        mNoContent = (TextView) rootView.findViewById(R.id.noContent);
        mLoading = (ProgressBar) rootView.findViewById(R.id.loading);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.container);
        mRecyclerView.setHasFixedSize(true);

        // We will have a grid view with 2 columns and variable item height
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mRecyclerView.setLayoutManager(layoutManager);

        mPhotosAdapter = new PhotosStaggeredGridAdapter(mPhotos);
        mRecyclerView.setAdapter(mPhotosAdapter);

        // don't bother reloading images that were saved from a previous state
        if (mPhotos.size() == 0) {
            loadPhotos();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(ARG_CATEGORY_NAME, mCategoryName);
        outState.putSerializable(ARG_PHOTOS, mPhotos);
    }

    private void loadPhotos() {
        mLoading.setVisibility(View.VISIBLE);
        ShutterstockRequestTask<List<PhotoPOJO>> task = new ShutterstockRequestTask<>(new PhotosParser(), new Callback<List<PhotoPOJO>>() {
            @Override
            public void onSuccess(final List<PhotoPOJO> photos) {
                // we need to perform UI updates on the UI thread, since AsyncTask runs separately
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoading.setVisibility(View.GONE);
                        if (photos.size() == 0) {
                            mNoContent.setVisibility(View.VISIBLE);
                        }
                    }
                });

                mPhotosAdapter.refreshImages(photos);
            }

            @Override
            public void onError() {
                mParentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoading.setVisibility(View.GONE);
                        mNoContent.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        task.execute(ShutterstockHelper.getPhotosInCategoryUrl(getActivity(), mCategoryName));
    }
}
