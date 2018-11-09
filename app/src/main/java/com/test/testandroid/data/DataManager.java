package com.test.testandroid.data;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

import com.test.testandroid.data.local.DatabaseHelper;
import com.test.testandroid.data.local.PreferencesHelper;
import com.test.testandroid.data.model.Ribot;
import com.test.testandroid.data.remote.JamboService;

@Singleton
public class DataManager {

    private final JamboService mJamboService;
    private final DatabaseHelper mDatabaseHelper;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(JamboService jamboService, PreferencesHelper preferencesHelper,
                       DatabaseHelper databaseHelper) {
        mJamboService = jamboService;
        mPreferencesHelper = preferencesHelper;
        mDatabaseHelper = databaseHelper;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<Ribot> syncRibots() {
        return mJamboService.getRibots()
                .concatMap(new Function<List<Ribot>, ObservableSource<? extends Ribot>>() {
                    @Override
                    public ObservableSource<? extends Ribot> apply(@NonNull List<Ribot> ribots)
                            throws Exception {
                        return mDatabaseHelper.setRibots(ribots);
                    }
                });
    }

    public Observable<List<Ribot>> getRibots() {
        return mDatabaseHelper.getRibots().distinct();
    }


}
