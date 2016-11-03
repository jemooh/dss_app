package com.example.leticia.dss.Adapters;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.leticia.dss.src.PendingFragment;
import com.example.leticia.dss.src.ResolvedFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    private Bundle negotiations;

    //gettting Bundled negs_Json
    public TabsPagerAdapter(FragmentManager fm, Bundle negotiations) {
        super(fm);
        this.negotiations = negotiations;
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                // Top Rated fragment activity
                PendingFragment pending = new PendingFragment();
                pending.setArguments(negotiations);
                return  pending;

            case 1:
                ResolvedFragment closed = new ResolvedFragment();
                closed.setArguments(negotiations);
                return  closed;

        }

        return null;
    }
    //check count
    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 2;
    }

}

