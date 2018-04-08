package com.yixi_sd.yixi.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.yixi_sd.yixi.di.module.HomeModule;
import com.yixi_sd.yixi.mvp.ui.activity.HomeActivity;

import dagger.Component;

@ActivityScope
@Component(modules = HomeModule.class, dependencies = AppComponent.class)
public interface HomeComponent {
    void inject(HomeActivity activity);
}