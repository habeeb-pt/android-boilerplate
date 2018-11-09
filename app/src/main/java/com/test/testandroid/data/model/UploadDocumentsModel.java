package com.test.testandroid.data.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

/**
 * Created by habeeb on 9/9/18.
 */
@AutoValue
public abstract class UploadDocumentsModel implements    Parcelable {


    public abstract String document_type();
    public abstract String names();
    public abstract String path();

    public static UploadDocumentsModel create(String document_type, String names, String path) {
        return builder()
                .document_type(document_type)
                .names(names)
                .path(path)
                .build();
    }

    public static Builder builder() {
        return new AutoValue_UploadDocumentsModel.Builder();
    }


    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder document_type(String document_type);

        public abstract Builder names(String names);

        public abstract Builder path(String path);

        public abstract UploadDocumentsModel build();
    }
}
