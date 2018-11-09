package com.test.testandroid.injection.component;

import dagger.Subcomponent;
import com.test.testandroid.injection.PerActivity;
import com.test.testandroid.injection.module.ActivityModule;
import com.test.testandroid.ui.launcher.LauncherActivity;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {


    void inject(LauncherActivity launcherActivity);



}
