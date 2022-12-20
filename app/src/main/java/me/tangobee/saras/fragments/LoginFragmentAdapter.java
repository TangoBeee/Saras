package me.tangobee.saras.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class LoginFragmentAdapter extends FragmentStateAdapter {
    int totalTabs;

    public LoginFragmentAdapter(FragmentManager fm, Lifecycle lifecycle, int totalTabs) {
        super(fm, lifecycle);

        this.totalTabs = totalTabs;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignupFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return totalTabs;
    }
}
