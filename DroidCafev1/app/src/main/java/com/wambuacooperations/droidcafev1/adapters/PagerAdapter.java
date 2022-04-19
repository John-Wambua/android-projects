package com.wambuacooperations.droidcafev1.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.wambuacooperations.droidcafev1.fragments.DesertRecipeFragment;
import com.wambuacooperations.droidcafev1.fragments.PastriesRecipeFragment;
import com.wambuacooperations.droidcafev1.fragments.StoresFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int myNumberOfTabs;
    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, numOfTabs);
        this.myNumberOfTabs=numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new DesertRecipeFragment();
            case 1: return new PastriesRecipeFragment();
            case 2: return new StoresFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return myNumberOfTabs;
    }
}
