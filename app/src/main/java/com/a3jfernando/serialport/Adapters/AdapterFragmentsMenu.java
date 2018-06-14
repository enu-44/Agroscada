package com.a3jfernando.serialport.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by EnuarMunoz on 17/08/2017.
 */

public class AdapterFragmentsMenu extends FragmentPagerAdapter {
    private String[] titles;
    private Fragment[] fragments;

    public AdapterFragmentsMenu(FragmentManager fm, String[] titles, Fragment[] fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments[position];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.titles[position];
    }



    @Override
    public int getCount() {
        return this.fragments.length;
    }
}