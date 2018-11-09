package com.test.testandroid.data.remote;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import com.test.testandroid.data.model.Ribot;
import com.test.testandroid.util.MyGsonTypeAdapterFactory;

public interface JamboService {

    String ENDPOINT = "http://staging.api.com/api/";

    @GET("ribots")
    Observable<List<Ribot>> getRibots();





    /******** Helper class that sets up a new services *******/
    class Creator {

        public static JamboService newCabbbieService(Context mContext) {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapterFactory(MyGsonTypeAdapterFactory.create())
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            OkHttpClient client = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new com.facebook.stetho.okhttp3.StethoInterceptor())
                    .addInterceptor(new HeaderInterceptor(mContext))
                    .retryOnConnectionFailure(true)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(JamboService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(client)
                    .build();


            return retrofit.create(JamboService.class);
        }
    }
}
