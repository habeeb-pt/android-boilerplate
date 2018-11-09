package com.test.testandroid.util;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Response;

/**
 * Created by habeeb on 25/9/18.
 */

public class ErrorUtils {

    private static String errorbody;

    public static String parseError(Response<?> response) {

        try {
          errorbody=  response.errorBody().string();
          Log.e("ErrorUtils",errorbody);
            JSONObject jsonObject=new JSONObject(errorbody);

            return  jsonObject.getString("message");
        } catch (IOException e) {
            e.printStackTrace();
            return  e.getLocalizedMessage();
        } catch (JSONException e) {
            e.printStackTrace();
            return  e.getLocalizedMessage();
        }

    }
}
