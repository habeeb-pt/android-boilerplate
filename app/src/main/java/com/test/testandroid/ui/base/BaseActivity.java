package com.test.testandroid.ui.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicLong;

import butterknife.Unbinder;
import timber.log.Timber;

import com.test.testandroid.JamboApplication;
import com.test.testandroid.R;
import com.test.testandroid.data.constants.AppConstants;
import com.test.testandroid.injection.component.ActivityComponent;
import com.test.testandroid.injection.component.ConfigPersistentComponent;
import com.test.testandroid.injection.component.DaggerConfigPersistentComponent;
import com.test.testandroid.injection.module.ActivityModule;
import com.test.testandroid.util.DialogFactory;
import com.test.testandroid.util.NetworkUtil;

/**
 * Abstract activity that every other Activity in this application must implement. It handles
 * creation of Dagger components and makes sure that instances of ConfigPersistentComponent survive
 * across configuration changes.
 */
public class BaseActivity extends AppCompatActivity  {

    private static final String KEY_ACTIVITY_ID = "KEY_ACTIVITY_ID";
    private static final AtomicLong NEXT_ID = new AtomicLong(0);
    private static final LongSparseArray<ConfigPersistentComponent>
            sComponentsMap = new LongSparseArray<>();

    private ActivityComponent mActivityComponent;
    private long mActivityId;
    private Dialog mDialog;

    public ActivityComponent activityComponent() {
        return mActivityComponent;
    }
    public Unbinder mUnbinder;
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(KEY_ACTIVITY_ID, mActivityId);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create the ActivityComponent and reuses cached ConfigPersistentComponent if this is
        // being called after a configuration change.
        mActivityId = savedInstanceState != null ?
                savedInstanceState.getLong(KEY_ACTIVITY_ID) : NEXT_ID.getAndIncrement();

        ConfigPersistentComponent configPersistentComponent = sComponentsMap.get(mActivityId, null);

        if (configPersistentComponent == null) {
            Timber.i("Creating new ConfigPersistentComponent id=%d", mActivityId);
            configPersistentComponent = DaggerConfigPersistentComponent.builder()
                    .applicationComponent(JamboApplication.get(this).getComponent())
                    .build();
            sComponentsMap.put(mActivityId, configPersistentComponent);
        }
        mActivityComponent = configPersistentComponent.activityComponent(new ActivityModule(this));

        if (checkNeedsPermission()) {
            requestStoragePermission();
        }


    }

    private boolean checkNeedsPermission() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION}, AppConstants.LOCATION_PERMISSION_REQUEST);
//        } else {
//            // Eh, prompt anyway
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION},AppConstants.LOCATION_PERMISSION_REQUEST);
//        }
    }

    public void KeybordDismis() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethod.SHOW_FORCED, 0);
        }
    }

    public void goToSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getApplication().getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!NetworkUtil.isNetworkConnected(getApplicationContext())) {
                            }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (!isChangingConfigurations()) {
            Timber.i("Clearing ConfigPersistentComponent id=%d", mActivityId);
            sComponentsMap.remove(mActivityId);
        }

        if(mUnbinder!=null)
            mUnbinder.unbind();
        try {
            if (mDialog.isShowing()) {
                mDialog.hide();
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (NullPointerException n) {

        }

        super.onDestroy();
    }

    public void makeLongToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void makeShortToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showBaseProgress() {
        if (mDialog != null) {
            if (!mDialog.isShowing()) {
                mDialog.show();
                mDialog.setCancelable(false);
            } else if (mDialog.isShowing()) {

                mDialog = DialogFactory.createTransparentProgress(this);
                mDialog.setContentView(this.getCurrentFocus());
                mDialog.show();
                mDialog.setCancelable(false);

            }
        } else {
            mDialog = DialogFactory.createTransparentProgress(this);
            mDialog.show();
            mDialog.setCancelable(false);
        }

        mDialog.setOnCancelListener(dialogInterface -> {

        });
    }

    public void hideBaseProgress() {

        try {
            if (mDialog.isShowing()) {
                mDialog.hide();
                mDialog.dismiss();
                mDialog = null;
            }
        } catch (NullPointerException n) {

        }
    }
    public static String getRealPathFromUri(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
