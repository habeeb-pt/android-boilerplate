package com.test.testandroid.data.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

/**
 * Created by habeeb on 15/8/18.
 */
@AutoValue
public abstract class JamboSuccessResponse implements Parcelable {

    @Nullable
    public abstract String message();

    public static JamboSuccessResponse create(String message) {
        return builder()
                .message(message)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_JamboSuccessResponse.Builder();
    }

    public static TypeAdapter<JamboSuccessResponse> typeAdapter(Gson gson) {
        return new AutoValue_JamboSuccessResponse.GsonTypeAdapter(gson);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder message(String message);

        public abstract JamboSuccessResponse build();
    }
}
