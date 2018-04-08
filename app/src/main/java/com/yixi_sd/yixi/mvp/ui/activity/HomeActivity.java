package com.yixi_sd.yixi.mvp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.yixi_sd.yixi.R;
import com.yixi_sd.yixi.di.component.DaggerHomeComponent;
import com.yixi_sd.yixi.di.module.HomeModule;
import com.yixi_sd.yixi.mvp.contract.HomeContract;
import com.yixi_sd.yixi.mvp.presenter.HomePresenter;
import com.yixi_sd.yixi.mvp.ui.adapter.PagerWithTabAdapter;
import javax.inject.Inject;
import butterknife.OnClick;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {


    @Inject
    PagerWithTabAdapter adapterViewPager;


    @Override
    public void setupActivityComponent(AppComponent appComponent) {
        DaggerHomeComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(Bundle savedInstanceState) {
        return R.layout.activity_home; //如果你不需要框架帮你设置 setContentView(id) 需要自行设置,请返回 0
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }

    @OnClick(R.id.module_base_id_fail_retry)
    public void onViewClicked() {

    }

}
