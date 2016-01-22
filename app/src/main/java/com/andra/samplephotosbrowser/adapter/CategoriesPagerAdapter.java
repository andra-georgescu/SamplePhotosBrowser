package com.andra.samplephotosbrowser.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.andra.samplephotosbrowser.fragment.CategoryFragment;

import java.util.List;

/**
 * This is the adapter used by the ViewPager to create all the necessary fragments containing the image lists
 */
public class CategoriesPagerAdapter extends FragmentStatePagerAdapter {
    List<String> mCategories;

    public CategoriesPagerAdapter(FragmentManager fm, List<String> categories) {
        super(fm);

        mCategories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return CategoryFragment.newInstance(mCategories.get(position));
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mCategories.get(position);
    }
}
