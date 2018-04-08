package com.yixi_sd.yixi.mvp.ui.adapter;

import android.app.Activity;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.jess.arms.base.AdapterViewPager;

import java.util.List;

/**
 * Created by 张志远
 * on 2017/12/4.
 */

public class PagerWithTabAdapter extends AdapterViewPager{
    public PagerWithTabAdapter(FragmentManager fragmentManager, List<Fragment> list) {
        super(fragmentManager, list);
    }

    public PagerWithTabAdapter(FragmentManager fragmentManager, List<Fragment> list, CharSequence[] titles) {
        super(fragmentManager, list, titles);
    }

    /**
     * 连结viewPager，tabLayout和activity的title
     * @param activity
     * @param mTab
     * @param viewPager
     */
    public void setTabAndPager(Activity activity, TabLayout mTab, ViewPager viewPager){
        viewPager.setAdapter(this);

        for (int i = 0; i <getCount(); i++){
            mTab.addTab(mTab.newTab().setText(getPageTitle(i)));
        }

        mTab.setupWithViewPager(viewPager);

        mTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                activity.setTitle(tab.getText().toString());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
