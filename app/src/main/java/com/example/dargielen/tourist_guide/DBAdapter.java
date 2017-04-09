package com.example.dargielen.tourist_guide;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;


/**
 * Created by dargielen on 03.04.2017.
 */

public class DBAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TG.db";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Attractions.TABLE_NAME + " (" +
                    Attractions._ID + " INTEGER PRIMARY KEY," +
                    Attractions.COLUMN_NAME_NAME + " TEXT," +
                    Attractions.COLUMN_NAME_ADDRESS + " TEXT," +
                    Attractions.COLUMN_NAME_DESCRIPTION_SHORT + " TEXT," +
                    Attractions.COLUMN_NAME_DESCRIPTION_LONG + " TEXT," +
                    Attractions.COLUMN_NAME_LONGITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_LATITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_IMAGES + " TEXT)";

    public static class Attractions implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "addres";
        public static final String COLUMN_NAME_DESCRIPTION_SHORT = "short description";
        public static final String COLUMN_NAME_DESCRIPTION_LONG = "long description";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_IMAGES = "images";
    }

    private static final String TAG = "DBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    private String orderBy = null;

    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DBAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Attractions.TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public long createAttraction(Attraction attraction) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Attractions.COLUMN_NAME_NAME, attraction.getName());
        initialValues.put(Attractions.COLUMN_NAME_ADDRESS, attraction.getAdress());
        initialValues.put(Attractions.COLUMN_NAME_DESCRIPTION_SHORT, attraction.getShort_description());
        initialValues.put(Attractions.COLUMN_NAME_DESCRIPTION_LONG, attraction.getLong_description());
        initialValues.put(Attractions.COLUMN_NAME_IMAGES, attraction.getImage());
        initialValues.put(Attractions.COLUMN_NAME_LONGITUDE, attraction.getLongitude());
        initialValues.put(Attractions.COLUMN_NAME_LATITUDE, attraction.getLatitude());

        return mDb.insert(Attractions.TABLE_NAME, null, initialValues);
    }

    public boolean deleteAllAttractionss() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Attractions.TABLE_NAME, null , null);

        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllAttractions() {
        Cursor mCursor = mDb.query(Attractions.TABLE_NAME, new String[]{Attractions._ID, Attractions.COLUMN_NAME_NAME,
                Attractions.COLUMN_NAME_ADDRESS, Attractions.COLUMN_NAME_DESCRIPTION_SHORT,}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void insertSomeAttractions() {
        createAttraction(new Attraction("Jaskinia niedźwiedzia", "Śnieżnicki Park Krajobrazowy, Kletno 18, 57-550 Kletno", "Jaskinia Niedźwiedzia – najdłuższa jaskinia całych Sudetów, położona w Masywie Śnieżnika, w dolinie Kleśnicy, w pobliżu wsi Kletno. Odkryta podczas eksploatacji kamieniołomu marmuru w 1966 roku, w obrębie wzniesienia Stroma.", "Jaskinię odkryto 14 października 1966 roku podczas eksploatacji wyrobiska „Kletno III” przez Bystrzyckie Zakłady Kamienia Budowlanego wydobywającego w tej okolicy marmur metodą odkrywkową. Około godziny 13.00 wykonano odstrzał, po którym odsłonił się dwumetrowy otwór o soczewkowatym kształcie. Wiertacz strzałowy Roman Kińczyk z Lądka-Zdroju wraz z jednym z pracujących tu więźniów zajrzeli pierwsi do otworu jaskini i znaleźli w nim kości. Duża ilość szczątków nasunęła odkrywcom przypuszczenie o zbiorowym grobie. Powiadomiono natychmiast kierownictwo zakładu i geologa z pobliskiego uzdrowiska. Trzy dni później dokonano pierwszych fachowych oględzin. Łączną długość korytarzy i sal na dwóch poziomach oszacowano na ok. 200 m. Liczne kości stwierdzone w namuliskach okazały się być szczątkami prehistorycznych ssaków.","jaskinia1", 50.2344142, 16.84281050000004));

    }



}
