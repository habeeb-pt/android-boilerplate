package com.test.testandroid.data.remote;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.test.testandroid.data.constants.AppConstants;
import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import timber.log.Timber;

/**
 * Created by entangled on 14/12/16.
 */

@Singleton
public final class HeaderInterceptor implements Interceptor {
    Context mContext;
    SharedPreferences preferences;
    RxSharedPreferences rxPreferences;
    String accessToken;
    String userToken;

    @Inject
    public HeaderInterceptor(Context mContext) {
        this.mContext = mContext;
        this.preferences = PreferenceManager
                .getDefaultSharedPreferences(this.mContext);
        this.rxPreferences = RxSharedPreferences.create(preferences);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        accessToken = rxPreferences.getString(AppConstants.PreferenceConstants.ACCESS_TOKEN).get();

        Request original = chain.request();
        Timber.d("AccessToken::" + accessToken);
        // Request customization: add request headers

        Request.Builder requestBuilder = null;
        if ( accessToken != null&&accessToken.length()>0 ) {
            requestBuilder = original.newBuilder()
                    .addHeader("Authorization","jwt "+ accessToken)
                    .addHeader("Content-Type", "application/json"); // <-- this is the important line
        } else {
            requestBuilder = original.newBuilder()
                    .addHeader("Content-Type", "application/json"); // <-- this is the important line
        }

        Request request = requestBuilder.build();
        return chain.proceed(request);
    }
}

