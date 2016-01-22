package com.andra.samplephotosbrowser.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.andra.samplephotosbrowser.R;
import com.andra.samplephotosbrowser.adapter.CategoriesPagerAdapter;
import com.andra.samplephotosbrowser.adapter.PhotosStaggeredGridAdapter;
import com.andra.samplephotosbrowser.network.Callback;
import com.andra.samplephotosbrowser.network.ShutterstockHelper;
import com.andra.samplephotosbrowser.network.ShutterstockRequestTask;
import com.andra.samplephotosbrowser.parser.CategoriesParser;
import com.andra.samplephotosbrowser.parser.PhotosParser;
import com.andra.samplephotosbrowser.pojo.PhotoPOJO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PhotosCategoriesActivity extends AppCompatActivity {

    private static final String ARG_CATEGORIES = "categories";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private RecyclerView mSearchResultsRecyclerView;
    private PhotosStaggeredGridAdapter mResultsAdapter;
    private List<String> mCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_categories);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mSearchResultsRecyclerView = (RecyclerView) findViewById(R.id.searchResults);

        PagerTabStrip tabStrip = (PagerTabStrip) findViewById(R.id.tabs);
        tabStrip.setDrawFullUnderline(false);
        tabStrip.setTabIndicatorColorResource(R.color.colorPrimary);
        tabStrip.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        // If we have a list of categories that's already stored, we can use this one and skip a network call
        if (savedInstanceState != null) {
            mCategories = (ArrayList<String>) savedInstanceState.getSerializable(ARG_CATEGORIES);
        }

        if (mCategories != null) {
            updateViewPager();
        } else {
            getAllCategories();
        }

        // Initialize the recycler view used for search results
        mSearchResultsRecyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        mSearchResultsRecyclerView.setLayoutManager(layoutManager);

        mResultsAdapter = new PhotosStaggeredGridAdapter(new ArrayList<PhotoPOJO>());
        mSearchResultsRecyclerView.setAdapter(mResultsAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putSerializable(ARG_CATEGORIES, (Serializable) mCategories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_photos_categories, menu);

        // We only have one action item, which allows us to make search requests to the Shutterstock API
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // When entering/leaving the search mode, we need to alternate the visibility of the
        // categories view pager and of the search results recycler view
        MenuItemCompat.setOnActionExpandListener(searchItem,
                new MenuItemCompat.OnActionExpandListener() {

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem menuItem) {
                        mViewPager.setVisibility(View.GONE);
                        mSearchResultsRecyclerView.setVisibility(View.VISIBLE);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                        mSearchResultsRecyclerView.setVisibility(View.GONE);
                        mViewPager.setVisibility(View.VISIBLE);
                        return true;
                    }
                });

        // Just make a search query to the Shutterstock API when the user clicks on search
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                makeSearch(query);

                // Clear the focus for the search box so that the keyboard stays hidden
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    private void getAllCategories() {
        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Getting Data ...", "Waiting For Results...", true);
        ShutterstockRequestTask<List<String>> task = new ShutterstockRequestTask<>(new CategoriesParser(), new Callback<List<String>>() {
            @Override
            public void onSuccess(List<String> categories) {
                mCategories = categories;
                updateViewPager();
                // performing UI updates on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                    }
                });
            }

            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                    }
                });
            }
        });
        task.execute(ShutterstockHelper.getAllCategoriesUrl(this));
    }

    private void makeSearch(String query) {
        final ProgressDialog loadingDialog = ProgressDialog.show(this, "Getting Data ...", "Waiting For Results...", true);
        ShutterstockRequestTask<List<PhotoPOJO>> task = new ShutterstockRequestTask<>(new PhotosParser(), new Callback<List<PhotoPOJO>>() {
            @Override
            public void onSuccess(final List<PhotoPOJO> photos) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                    }
                });
                if (photos.size() > 0) {
                    mResultsAdapter.refreshImages(photos);
                } else {
                    Toast.makeText(PhotosCategoriesActivity.this, "No results found.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onError() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                    }
                });
            }
        });
        task.execute(ShutterstockHelper.getQueryUrl(this, query));
    }

    // Sets newly received list of categories on the view pager
    private void updateViewPager() {
        mViewPager.setAdapter(new CategoriesPagerAdapter(getSupportFragmentManager(), mCategories));
    }
}
