package com.procus.simpleuserregistration.adapters;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.procus.simpleuserregistration.fragments.RegistrationFragment;
import com.procus.simpleuserregistration.fragments.UserListFragment;

/**
 * Created by Peter on 2.7.17.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PageAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {

        switch (position) {
            case 0:
                RegistrationFragment tab1 = new RegistrationFragment();
                return tab1;
            case 1:
                UserListFragment tab2 = new UserListFragment();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}