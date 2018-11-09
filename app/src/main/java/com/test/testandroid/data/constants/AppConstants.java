package com.test.testandroid.data.constants;


import com.test.testandroid.BuildConfig;

/**
 * Created by habeeb on 18/04/18.
 */

public class AppConstants {

    public static final String APP_VERSION = BuildConfig.VERSION_NAME;
    public static final String SOURCE_TYPE = "Android";


    public static final int LOCATION_PERMISSION_REQUEST = 1002;
    public static final int CAMERA_REQUEST = 1003;



    public class TilesType {
        public static final String CameraTile = "isCameraTile";
        public static final String PickerTile = "isPickerTile";
        public static final String ImageTile = "isImageTile";
    }

    public static class PreferenceConstants {
        public static final String PREF_FILE_NAME = "com_jambo_jamboandroid";


        public static final String ACCESS_TOKEN = "mAccessToken";
        public static final String USER_ID = "mUserId";
        public static final String USER_F_NAME = "mUserFname";
        public static final String USER_MOBILE_NUMBER = "mUserMobileNumber";
        public static final String USER_EMAIL = "mUserEmail";
        public static final String FIREBASE_TOKEN = "mFirebaseToken";


        public static final String SKIPPED_WELCOME = "mSkippedWelcome";


    }

    public static class IntentConstant {



        public static final String ADDRESS_ID = "ADDRESS_ID";

        public static final String USER_ID = "USER_INFO_ID";

        public static final String PRICE = "PRICE";
        public static final String EMAIL = "email";
        public static final String NAME = "name";
        public static final String PROFILE = "mProfile";
    }

    public static final class ImageProperties {

        public static final int MAX_WIDTH_IN_PX = 1920;
        public static final int MAX_HEIGHT_IN_PX = 2560;

        public static final int INITIALS_IMAGE_SIZE = 500;
        public static final float INITIALS_TEXT_SIZE = 150f;

    }


    public class PaymentConstants {

    }


    public class Weburls {
        public static final String ABOUT_URL = " ";
        public static final String PRIVACY_URL = " ";
        public static final String TERMS_URL= " ";
        public static final String FAQ_URL = " ";
    }


    public class StartCodes {
        public static final int LauncherActivity = 100;

    }
}
