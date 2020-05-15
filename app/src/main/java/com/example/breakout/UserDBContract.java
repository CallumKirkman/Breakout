package com.example.breakout;

import android.provider.BaseColumns;

class UserDBContract {

    private UserDBContract(){}//exists so no instance can be created

    public static final class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "TBL_USERDATA";
        public static final String COLUMN_ID = "USER_ID";
        public static final String COLUMN_FORENAME = "USER_FORENAME";
        public static final String COLUMN_SURNAME = "USER_SURNAME";
        public static final String COLUMN_EMAIL_ADDRESS = "USER_EMAIL_ADDRESS";
        public static final String COLUMN_PASSWORD = "USER_PASSWORD";
        public static final String COLUMN_SALT = "USER_SALT";
    }
}
