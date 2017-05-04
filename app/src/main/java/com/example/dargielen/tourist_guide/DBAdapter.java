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
    public static final String DATABASE_NAME = "Attractions.db";
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Attractions.TABLE_NAME + " (" +
                    Attractions._ID + " INTEGER PRIMARY KEY," +
                    Attractions.COLUMN_NAME_NAME + " TEXT," +
                    Attractions.COLUMN_NAME_ADDRESS + " TEXT," +
                    Attractions.COLUMN_NAME_DESCRIPTION_SHORT + " TEXT," +
                    Attractions.COLUMN_NAME_DESCRIPTION_LONG + " TEXT," +
                    Attractions.COLUMN_NAME_LONGITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_LATITUDE + " TEXT," +
                    Attractions.COLUMN_NAME_IMAGE1 + " TEXT," +
                    Attractions.COLUMN_NAME_IMAGE2 + " TEXT," +
                    Attractions.COLUMN_NAME_IMAGE3 + " TEXT," +
                    Attractions.COLUMN_NAME_IMAGE4 + " TEXT )";



    public static class Attractions implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ADDRESS = "address";
        public static final String COLUMN_NAME_DESCRIPTION_SHORT = "short_description";
        public static final String COLUMN_NAME_DESCRIPTION_LONG = "long_description";
        public static final String COLUMN_NAME_LONGITUDE = "longitude";
        public static final String COLUMN_NAME_LATITUDE = "latitude";
        public static final String COLUMN_NAME_IMAGE1 = "image1";
        public static final String COLUMN_NAME_IMAGE2 = "image2";
        public static final String COLUMN_NAME_IMAGE3 = "image3";
        public static final String COLUMN_NAME_IMAGE4 = "image4";
        public static final String KEY = "key";
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
        initialValues.put(Attractions.COLUMN_NAME_ADDRESS, attraction.getAddress());
        initialValues.put(Attractions.COLUMN_NAME_DESCRIPTION_SHORT, attraction.getShort_description());
        initialValues.put(Attractions.COLUMN_NAME_DESCRIPTION_LONG, attraction.getLong_description());
        initialValues.put(Attractions.COLUMN_NAME_IMAGE1, attraction.getImage1());
        initialValues.put(Attractions.COLUMN_NAME_IMAGE2, attraction.getImage2());
        initialValues.put(Attractions.COLUMN_NAME_IMAGE3, attraction.getImage3());
        initialValues.put(Attractions.COLUMN_NAME_IMAGE4, attraction.getImage4());
        initialValues.put(Attractions.COLUMN_NAME_LONGITUDE, attraction.getLongitude());
        initialValues.put(Attractions.COLUMN_NAME_LATITUDE, attraction.getLatitude());

        return mDb.insert(Attractions.TABLE_NAME, null, initialValues);
    }

    public boolean deleteAllAttractions() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Attractions.TABLE_NAME, null , null);

        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchAllAttractions() {
        Cursor mCursor = mDb.query(Attractions.TABLE_NAME, null, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor fetchAllAttractionsByNameAndDescription(String inputText) throws SQLException {

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Attractions.TABLE_NAME, null, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDb.query(Attractions.TABLE_NAME, null,
                    Attractions.COLUMN_NAME_NAME + " like '%" + inputText + "%' OR " + Attractions.COLUMN_NAME_DESCRIPTION_SHORT + " like '%" + inputText + "%'",
                    null, null, null, orderBy, null);
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

    }

    public void insertSomeAttractions() {
        createAttraction(new Attraction("Jaskinia niedźwiedzia", "Śnieżnicki Park Krajobrazowy, Kletno 18, 57-550 Kletno", "Jaskinia Niedźwiedzia – najdłuższa jaskinia całych Sudetów, położona w Masywie Śnieżnika, w dolinie Kleśnicy, w pobliżu wsi Kletno. Odkryta podczas eksploatacji kamieniołomu marmuru w 1966 roku, w obrębie wzniesienia Stroma.", "Jaskinię odkryto 14 października 1966 roku podczas eksploatacji wyrobiska „Kletno III” przez Bystrzyckie Zakłady Kamienia Budowlanego wydobywającego w tej okolicy marmur metodą odkrywkową. Około godziny 13.00 wykonano odstrzał, po którym odsłonił się dwumetrowy otwór o soczewkowatym kształcie. Wiertacz strzałowy Roman Kińczyk z Lądka-Zdroju wraz z jednym z pracujących tu więźniów zajrzeli pierwsi do otworu jaskini i znaleźli w nim kości. Duża ilość szczątków nasunęła odkrywcom przypuszczenie o zbiorowym grobie. Powiadomiono natychmiast kierownictwo zakładu i geologa z pobliskiego uzdrowiska. Trzy dni później dokonano pierwszych fachowych oględzin. Łączną długość korytarzy i sal na dwóch poziomach oszacowano na ok. 200 m. Liczne kości stwierdzone w namuliskach okazały się być szczątkami prehistorycznych ssaków.","jaskinia1", "jaskinia2", "jaskinia3", "jaskinia4",50.2344142, 16.84281050000004));
        createAttraction(new Attraction("Sanktuarium w Wambierzycach", "Pl. Najświętszej Marii Panny 11, 57-420 Wambierzyce", "Bazylika Nawiedzenia Najświętszej Marii Panny, Sanktuarium Wambierzyckiej Królowej Rodzin Patronki Ziemi Kłodzkiej – barokowa bazylika znajdująca się w Wambierzycach koło Radkowa w powiecie kłodzkim. Wybudowana została w latach 1715-1723.", "Obecna bazylika stoi na wzgórzu, gdzie w XII w. w niszy wysokiego drzewa umieszczono figurkę Matki Boskiej. Według kronik w 1218 r. ociemniały Jan z Raszewa odzyskał w tym miejscu wzrok. Po tym zdarzeniu do Wambierzyc zaczęło podróżować wielu pielgrzymów. Wkrótce pod drzewem z figurką postawiono ołtarz, a po bokach lichtarz i chrzcielnicę. W 1263 r. na wzgórzu powstał drewniany kościół.\n" +
                "\n" +
                "W 1512 r. Ludwik von Panwitz wzniósł większą świątynię wybudowaną z cegły. Został on jednak zrujnowany podczas wojny trzydziestoletniej. W latach 1695-1711 wybudowano nowy kościół, który jednak szybko zaczął się walić i został rozebrany w 1714 r. W latach 1715-1723 powstała kolejna świątynia wybudowana przez hrabiego Franciszka Antoniego von Goetzena, która przetrwała do dziś.\n" +
                "\n" +
                "W 1936 r. papież Pius XI nadał kościołowi tytuł bazyliki mniejszej. 17 sierpnia 1980 r. kardynał Stefan Wyszyński dokonał koronacji figurki Matki Bożej nadając jej tytuł Wambierzyckiej Królowej Rodzin", "sanktuarium1", "sanktuarium1", "sanktuarium1", "sanktuarium1", 50.490207, 16.454826000000025));
        createAttraction(new Attraction("Błędne Skały", "Kudowa Zdrój", "Błędne Skały – zespół bloków skalnych na wysokości 853 m n.p.m., tworzący malowniczy labirynt, położony w południowo-zachodniej Polsce w Sudetach Środkowych w Górach Stołowych.", "Labirynt położony jest na obszarze Parku Narodowego Gór Stołowych i obejmuje północno-zachodnią część Stoliwa Skalniak. Znajduje się między Kudową-Zdrojem a Karłowem, w pobliżu granicy z Czechami. Najbliższa miejscowość to Bukowina Kłodzka. Obszar mający powierzchnię ok. 21 ha był rezerwatem przyrody (do czasu utworzenia Parku Narodowego Gór Stołowych). Na dnie górnokredowego morza osadziły się grube pokłady piaskowców. W trzeciorzędzie, w czasie orogenezy alpejskiej zostały one wydźwignięte wraz z całymi Sudetami. Następnie rozpoczął się długotrwały okres erozji i odprowadzania zwietrzeliny. Na skutek nierównej odporności poszczególnych warstw skalnych na wietrzenie i poszerzania szczelin, przecinających masyw w trzech kierunkach, powstały głębokie na kilka metrów korytarze o zmiennej szerokości. Przez kilka lat po drugiej wojnie światowej Błędne Skały najczęściej nazywane były Wilczymi Dołami. Takiego terminu używali m.in. żołnierze Wojsk Ochrony Pogranicza w meldunkach sytuacyjnych, dziennikarze w relacjach prasowych, czy niektórzy autorzy przewodników, jak np. Anna i Ignacy Potoccy. Niekiedy porównywano Błędne Skały do ruin zamczyska. Według legendy Błędne Skały stworzył Liczyrzepa.", "bskaly1", "bskaly1", "bskaly1", "bskaly1",50.4802896, 16.28721619999999));
        createAttraction(new Attraction("Szczeliniec Wielki","Góry Stołowe", "Szczeliniec Wielki – najwyższy szczyt w Górach Stołowych, na terenie Parku Narodowego Gór Stołowych.", "Szczeliniec Wielki (w XIX w. również Siennica, po 1945 Strzaskany i Spękany, Stołowiec, niem. Große Heuscheuer, czes. Velká Hejšovina) – najwyższy szczyt (919 m n.p.m.) w Górach Stołowych, na terenie Parku Narodowego Gór Stołowych. Należy on do Korony Gór Polski i jest jedną z największych atrakcji turystycznych Sudetów, z rezerwatem krajobrazowym i tarasami widokowymi z panoramą Sudetów. Najwyższym punktem jest Fotel Pradziada.", "szczeliniec1", "szczeliniec1", "szczeliniec1", "szczeliniec1",50.4841175, 16.34339699999998));
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }



}
