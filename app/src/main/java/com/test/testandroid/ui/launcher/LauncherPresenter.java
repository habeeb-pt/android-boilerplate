package com.test.testandroid.ui.launcher;


import com.test.testandroid.data.DataManager;
import com.test.testandroid.ui.base.BasePresenter;
import com.test.testandroid.util.RxUtil;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;



public class LauncherPresenter extends BasePresenter<LauncherMvpView> {


    private final DataManager mDataManager;
    private Disposable mMartDisposable;

    @Inject
    public LauncherPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void attachView(LauncherMvpView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mMartDisposable != null) mMartDisposable.dispose();
    }

    @Override
    public LauncherMvpView getMvpView() {
        return super.getMvpView();
    }

    @Override
    public boolean isViewAttached() {
        return super.isViewAttached();
    }

    @Override
    public void checkViewAttached() {
        super.checkViewAttached();
    }

    public void getConfig() {
        checkViewAttached();
        RxUtil.dispose(mMartDisposable);
//        mDataManager.getConfig()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        mMartDisposable = d;
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
    }

}
