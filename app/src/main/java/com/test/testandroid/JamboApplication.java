package com.test.testandroid;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

import com.test.testandroid.injection.component.ApplicationComponent;
import com.test.testandroid.injection.component.DaggerApplicationComponent;
import com.test.testandroid.injection.module.ApplicationModule;
import com.facebook.stetho.Stetho;

public class JamboApplication extends MultiDexApplication {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            // Fabric.with(this, new Crashlytics());

            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
            Stetho.Initializer initializer = initializerBuilder.build();
            Stetho.initialize(initializer);
        }

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }

    public static JamboApplication get(Context context) {
        return (JamboApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
