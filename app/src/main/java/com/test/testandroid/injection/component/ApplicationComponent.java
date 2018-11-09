package com.test.testandroid.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import com.test.testandroid.data.DataManager;
import com.test.testandroid.data.SyncService;
import com.test.testandroid.data.local.DatabaseHelper;
import com.test.testandroid.data.local.PreferencesHelper;
import com.test.testandroid.data.remote.JamboService;
import com.test.testandroid.injection.ApplicationContext;
import com.test.testandroid.injection.module.ApplicationModule;
import com.test.testandroid.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    JamboService ribotsService();
    PreferencesHelper preferencesHelper();
    DatabaseHelper databaseHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
