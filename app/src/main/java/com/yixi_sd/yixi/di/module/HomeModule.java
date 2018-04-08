package com.yixi_sd.yixi.di.module;

import android.support.v4.app.Fragment;

import com.jess.arms.di.scope.ActivityScope;
import com.yixi_sd.yixi.R;
import com.yixi_sd.yixi.mvp.contract.HomeContract;
import com.yixi_sd.yixi.mvp.model.HomeModel;
import com.yixi_sd.yixi.mvp.ui.activity.HomeActivity;
import com.yixi_sd.yixi.mvp.ui.adapter.PagerWithTabAdapter;

import java.util.ArrayList;
import java.util.List;

import dagger.Module;
import dagger.Provides;



@Module
public class HomeModule {
    private HomeContract.View view;

    /**
     * 构建HomeModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public HomeModule(HomeContract.View view) {
        this.view = view;
    }

    @ActivityScope
    @Provides
    HomeContract.View provideHomeView() {
        return this.view;
    }

    @ActivityScope
    @Provides
    HomeContract.Model provideHomeModel(HomeModel model) {
        return model;
    }

    @ActivityScope
    @Provides
    List<Fragment>  provideFragmentList(){
        List fragmentList = new ArrayList<>();
        return fragmentList;
    }
    @ActivityScope
    @Provides
    String[] provideTitles(){
        String[] titles = ((HomeActivity)view).getResources().getStringArray(R.array.m_titles);
        return titles;
    }

    @ActivityScope
    @Provides
    PagerWithTabAdapter provideAdapterViewPager(List<Fragment> list, String[] titles){
        return new PagerWithTabAdapter(((HomeActivity)view).getSupportFragmentManager(),list,titles);
    }
}