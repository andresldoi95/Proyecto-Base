package com.techtraining.cosechasapp;

import android.app.AlarmManager;

public class Helper {
    public static final String URL_API = "http://38.123.149.8/api";
    public static final String URL_OAUTH2 = "http://38.123.149.8/oauth/token";
    public static final String URL_LOGIN_APP = "http://38.123.149.8/api/login_app";

    /*public static final String URL_API = "http://192.168.0.117:8000/api";
    public static final String URL_OAUTH2 = "http://192.168.0.117:8000/oauth/token";
    public static final String URL_LOGIN_APP = "http://192.168.0.117:8000/api/login_app";*/
    
    public static final String CLIENT_ID = "2";
    public static final String GRANT_TYPE = "password";
    public static final String CLIENT_SECRET = "XbEO8Ol48yp67UQrw0L4sbQSrmNKxFGaRwzplHKL";
    public static final String USER_TOKEN_NAME = "userToken";
    public static final String FIRST_TIME_NAME = "firstTime";
    public static final String CURRENT_COSECHA_ID_NAME = "currentCosechaId";
    public static final String CURRENT_COSECHA_ID_HISTORIAL = "currentCosechaIdHistorial";
    public static final String IS_HISTORIAL = "isHistorial";
    public static final int MAX_IMAGE_SIZE = 20;
    public static final String SHARED_PREFERENCES_NAME = "cosechas";
    public static final String AUTH_HEADER = "Authorization";
    public static final String AUTH_TYPE = "Bearer ";
    public static final int DB_VERSION = 42;
    public static final int MAX_PHOTOS_TROZA = 3;
    public static final String DB_NAME = "cosechas";
    public static final String CURRENT_ID = "currentId";
    public static final int DEFAULT_TIMEOUT = 15000;
    public static final String CURRENT_FILA_NAME = "currentRow";
    public static final String CURRENT_ITEM_FILA_NAME = "currentItemRow";
    public static final String CURRENT_LLENADO_NAME = "currentLlenado";
    public static final String INDICE_NAME = "indice";
    public static final String CURRENT_ITEM_SUELTO_NAME = "currentItemSueltoName";
    public static final int IMAGE_QUALITY = 90;
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    public static final int NOTIFICATION_ID = 1992;
    public static final long UNIX_DAYS_HISTORIAL = 1296000000L;
    public static final String PRIMARY_CHANNEL_ID = "primary_notification_channel";
    public static final Long NOTIFICATION_TIMER = AlarmManager.INTERVAL_HALF_HOUR;
    public static final String newPhotoPath = "newPhotoPath";
}
