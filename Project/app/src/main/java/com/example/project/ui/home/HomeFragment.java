package com.example.project.ui.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.project.DataManager;
import com.example.project.R;
import com.example.project.databinding.FragmentHomeBinding;
import com.example.project.entities.Book;
import com.example.project.ui.CaptureAct;
import com.example.project.ui.DetailOrderActivity;
import com.example.project.ui.IntroActivity;
import com.example.project.ui.LoginActivity;
import com.example.project.ui.subFragments.SubFragmentAdapter;
import com.example.project.utils.Constants;
import com.google.android.material.tabs.TabLayout;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        ImageButton btnScan = binding.btnScan;
        btnScan.setOnClickListener(v -> {
            scanCode();
        });

        // Initialize TabLayout and ViewPager
        TabLayout tabLayout = binding.tabLayout;
        ViewPager viewPager = binding.viewPager;

        viewPager.setAdapter(new SubFragmentAdapter(getChildFragmentManager(), Constants.MODE_SUB_TABS.HOME));
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(
                getResources().getColorStateList(R.color.disable).getDefaultColor(), // Unselected color
                getResources().getColorStateList(R.color.main).getDefaultColor()
        );

        binding.myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireContext(), DetailOrderActivity.class));
            }
        });

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

    void scanCode(){
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLaucher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLaucher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null){
            Book book = DataManager.getInstance().getBookByID(result.getContents());
            DataManager.getInstance().addBookSelect(book);
            binding.myButton.setText("Check Out +" + DataManager.getInstance().getBooksSelect().size());
        }
    });
}
