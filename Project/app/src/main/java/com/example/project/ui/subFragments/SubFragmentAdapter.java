package com.example.project.ui.subFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.project.entities.Book;
import com.example.project.ui.subFragments.FragmentAll;
import com.example.project.ui.subFragments.FragmentBorrowing;

public class SubFragmentAdapter extends FragmentPagerAdapter {

    public SubFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FragmentAll();
            case 1:
                return new FragmentBorrowing();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Set tab titles if needed
        return "Tab " + (position + 1);
    }

}
