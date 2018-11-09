package com.test.testandroid.ui.launcher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.Window;

import com.test.testandroid.R;
import com.test.testandroid.data.constants.AppConstants;
import com.f2prateek.rx.preferences2.Preference;
import com.test.testandroid.injection.component.ActivityComponent;
import com.test.testandroid.ui.base.BaseActivity;
import com.test.testandroid.util.NetworkUtil;
import com.f2prateek.rx.preferences2.RxSharedPreferences;
import com.google.firebase.iid.FirebaseInstanceId;

import javax.inject.Inject;

import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.test.testandroid.data.constants.AppConstants.PreferenceConstants.ACCESS_TOKEN;


public class LauncherActivity extends BaseActivity implements LauncherMvpView {
    public static final int START_CODE = AppConstants.StartCodes.LauncherActivity;

    @Override
    protected void onResume() {
        super.onResume();
    }

    private static final int SPLASH_DISPLAY_LENGTH = 1000;
    public boolean gotmart = false;
    @Inject
    LauncherPresenter mLauncherPresenter;
    //response
    private Preference<String> mAccessToken;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        activityComponent().inject(this);
       setContentView(R.layout.activity_login);

        mLauncherPresenter.attachView(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String name = android.os.Build.MODEL;
        launch();

        if (NetworkUtil.isNetworkConnected(this)) {
            // mLauncherPresenter.getConfigurations(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case UserSelectionActivity.START_CODE: {
//                if (resultCode == Activity.RESULT_OK) {
//                    SharedPreferences preferences =
//                            PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//                    RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
//                    mAccessToken = rxPreferences.getString(ACCESS_TOKEN);
//                    mUserTypePref = rxPreferences.getString(USER_TYPE);
//                    Intent intent = getIntent();
//                    // finish();
//                    //startActivity(intent);
//                    startActivityForResult(DashboardActivity.getStartIntent(
//                            getApplicationContext(), true, mUserTypePref.get())
//                            , DashboardActivity.START_CODE);
//
////                    launch();
//                } else
//                    finish();
//            }
//            break;
//            case 999: {
//                finish();
//            }


        }
    }

    public void launch() {
        SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        RxSharedPreferences rxPreferences = RxSharedPreferences.create(preferences);
        mAccessToken = rxPreferences.getString(ACCESS_TOKEN);
        Preference<Boolean> mSkippedWelcome = rxPreferences.getBoolean(AppConstants.PreferenceConstants.SKIPPED_WELCOME);
        Timber.d("WELCOME SKIPPED" + mSkippedWelcome.get());

         new Handler().postDelayed(() -> {
            if (!mAccessToken.isSet()) {
                Timber.d("GOING TO DASHBOARD");

            } else if (!mSkippedWelcome.get()) {
                Timber.d("GOING TO WELCOME SCREEN");

                finish();
            }  else {
                Timber.d("GOING TO DASHBOARD");

                //TODO:Revert the above back
            }
        }, SPLASH_DISPLAY_LENGTH);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mLauncherPresenter.detachView();
        super.onDestroy();
    }

    @Override
    public ActivityComponent activityComponent() {
        return super.activityComponent();
    }
}
