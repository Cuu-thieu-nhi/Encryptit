package com.example.encryptit.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.encryptit.view.fragment.FileFragment;
import com.example.encryptit.view.fragment.ImageFragment;

public class ViewPageAdapter extends FragmentStatePagerAdapter {
    public ViewPageAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    public ViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ImageFragment();
            case 1:
                return new FileFragment();
            default:
                return new ImageFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
