package com.example.miwokapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MiwokFragmentPageAdpter extends FragmentPagerAdapter {

    private String tabTitles[] = new String[] { "Numbers", "Colors", "Family", "Phrases" };

    public MiwokFragmentPageAdpter(FragmentManager fm){
        super(fm);

    }

    @Override
    public Fragment getItem(int position) {

        Fragment screen = null;

        switch (position){
            case 0:
                screen = new NumberFragment();
                break;
            case 1:
                screen = new ColorFragment();
                break;
            case 2:
                screen = new FamilyFragment();
                break;
            case 3:
                screen = new PhraseFragment();
        }

        return screen;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
