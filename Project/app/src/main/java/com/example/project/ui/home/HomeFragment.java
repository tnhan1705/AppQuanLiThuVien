package com.example.project.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.project.R;
import com.example.project.databinding.FragmentHomeBinding;
import com.example.project.ui.subFragments.SubFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        // Initialize TabLayout and ViewPager
        TabLayout tabLayout = binding.tabLayout;
        ViewPager viewPager = binding.viewPager;

        // Set up the ViewPager with the sections adapter.
        viewPager.setAdapter(new SubFragmentAdapter(getChildFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(
                getResources().getColorStateList(R.color.disable).getDefaultColor(), // Unselected color
                getResources().getColorStateList(R.color.main).getDefaultColor()
        );

        // Optional: Set a listener to handle tab selection events
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Handle tab selection
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Handle tab unselection
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Handle tab reselection
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
