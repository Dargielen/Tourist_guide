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
                    Attractions.COLUMN_NAME_IMAGE4 + " TEXT," +
                    Attractions.COLUMN_NAME_DISTANCE + " TEXT )";



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
        public static final String COLUMN_NAME_DISTANCE = "distance";
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
        createAttraction(new Attraction("Jaskinia niedźwiedzia", "Śnieżnicki Park Krajobrazowy, Kletno 18, 57-550 Kletno", "Jaskinia Niedźwiedzia – najdłuższa jaskinia całych Sudetów, położona w Masywie Śnieżnika, w dolinie Kleśnicy, w pobliżu wsi Kletno. Odkryta podczas eksploatacji kamieniołomu marmuru w 1966 roku, w obrębie wzniesienia Stroma.", "Jaskinię odkryto 14 października 1966 roku podczas eksploatacji wyrobiska „Kletno III” przez Bystrzyckie Zakłady Kamienia Budowlanego wydobywającego w tej okolicy marmur metodą odkrywkową. Około godziny 13.00 wykonano odstrzał, po którym odsłonił się dwumetrowy otwór o soczewkowatym kształcie. Wiertacz strzałowy Roman Kińczyk z Lądka-Zdroju wraz z jednym z pracujących tu więźniów zajrzeli pierwsi do otworu jaskini i znaleźli w nim kości. Duża ilość szczątków nasunęła odkrywcom przypuszczenie o zbiorowym grobie. Powiadomiono natychmiast kierownictwo zakładu i geologa z pobliskiego uzdrowiska. Trzy dni później dokonano pierwszych fachowych oględzin. Łączną długość korytarzy i sal na dwóch poziomach oszacowano na ok. 200 m. Liczne kości stwierdzone w namuliskach okazały się być szczątkami prehistorycznych ssaków.","jaskinia1", "jaskinia2", "jaskinia3", "jaskinia4", 50.2344142, 16.84281050000004, 0));
        createAttraction(new Attraction("Sanktuarium w Wambierzycach", "Pl. Najświętszej Marii Panny 11, 57-420 Wambierzyce", "Bazylika Nawiedzenia Najświętszej Marii Panny, Sanktuarium Wambierzyckiej Królowej Rodzin Patronki Ziemi Kłodzkiej – barokowa bazylika znajdująca się w Wambierzycach koło Radkowa w powiecie kłodzkim. Wybudowana została w latach 1715-1723.", "Obecna bazylika stoi na wzgórzu, gdzie w XII w. w niszy wysokiego drzewa umieszczono figurkę Matki Boskiej. Według kronik w 1218 r. ociemniały Jan z Raszewa odzyskał w tym miejscu wzrok. Po tym zdarzeniu do Wambierzyc zaczęło podróżować wielu pielgrzymów. Wkrótce pod drzewem z figurką postawiono ołtarz, a po bokach lichtarz i chrzcielnicę. W 1263 r. na wzgórzu powstał drewniany kościół. W 1512 r. Ludwik von Panwitz wzniósł większą świątynię wybudowaną z cegły. Został on jednak zrujnowany podczas wojny trzydziestoletniej. W latach 1695-1711 wybudowano nowy kościół, który jednak szybko zaczął się walić i został rozebrany w 1714 r. W latach 1715-1723 powstała kolejna świątynia wybudowana przez hrabiego Franciszka Antoniego von Goetzena, która przetrwała do dziś. W 1936 r. papież Pius XI nadał kościołowi tytuł bazyliki mniejszej. 17 sierpnia 1980 r. kardynał Stefan Wyszyński dokonał koronacji figurki Matki Bożej nadając jej tytuł Wambierzyckiej Królowej Rodzin", "sanktuarium1", "sanktuarium2", "sanktuarium3", "sanktuarium4", 50.490207, 16.454826000000025, 0));
        createAttraction(new Attraction("Błędne Skały", "Kudowa Zdrój", "Błędne Skały – zespół bloków skalnych na wysokości 853 m n.p.m., tworzący malowniczy labirynt, położony w południowo-zachodniej Polsce w Sudetach Środkowych w Górach Stołowych.", "Labirynt położony jest na obszarze Parku Narodowego Gór Stołowych i obejmuje północno-zachodnią część Stoliwa Skalniak. Znajduje się między Kudową-Zdrojem a Karłowem, w pobliżu granicy z Czechami. Najbliższa miejscowość to Bukowina Kłodzka. Obszar mający powierzchnię ok. 21 ha był rezerwatem przyrody (do czasu utworzenia Parku Narodowego Gór Stołowych). Na dnie górnokredowego morza osadziły się grube pokłady piaskowców. W trzeciorzędzie, w czasie orogenezy alpejskiej zostały one wydźwignięte wraz z całymi Sudetami. Następnie rozpoczął się długotrwały okres erozji i odprowadzania zwietrzeliny. Na skutek nierównej odporności poszczególnych warstw skalnych na wietrzenie i poszerzania szczelin, przecinających masyw w trzech kierunkach, powstały głębokie na kilka metrów korytarze o zmiennej szerokości. Przez kilka lat po drugiej wojnie światowej Błędne Skały najczęściej nazywane były Wilczymi Dołami. Takiego terminu używali m.in. żołnierze Wojsk Ochrony Pogranicza w meldunkach sytuacyjnych, dziennikarze w relacjach prasowych, czy niektórzy autorzy przewodników, jak np. Anna i Ignacy Potoccy. Niekiedy porównywano Błędne Skały do ruin zamczyska. Według legendy Błędne Skały stworzył Liczyrzepa.", "bskaly1", "bskaly2", "bskaly3", "bskaly4", 50.4802896, 16.28721619999999, 0));
        createAttraction(new Attraction("Szczeliniec Wielki","Góry Stołowe", "Szczeliniec Wielki – najwyższy szczyt w Górach Stołowych, na terenie Parku Narodowego Gór Stołowych.", "Szczeliniec Wielki (w XIX w. również Siennica, po 1945 Strzaskany i Spękany, Stołowiec, niem. Große Heuscheuer, czes. Velká Hejšovina) – najwyższy szczyt (919 m n.p.m.) w Górach Stołowych, na terenie Parku Narodowego Gór Stołowych. Należy on do Korony Gór Polski i jest jedną z największych atrakcji turystycznych Sudetów, z rezerwatem krajobrazowym i tarasami widokowymi z panoramą Sudetów. Najwyższym punktem jest Fotel Pradziada.", "szczeliniec1", "szczeliniec2", "szczeliniec3", "szczeliniec4",50.4841175, 16.34339699999998, 0));
        createAttraction(new Attraction("Kaplica Czaszek", "Stanisława Moniuszki 8, 57-350 Kudowa-Zdrój", "Kaplica Czaszek – zabytek sakralny znajdujący się w Kudowie-Zdroju, w dzielnicy Czermna. Kaplica położona jest w odległości ok. 1 km od centrum Kudowy w dolinie rzeki Czermnicy.", "Kaplicę zbudował w latach 1776–1804 ksiądz Wacław Tomaszek (czes. Václav Tomášek), z pochodzenia Czech, proboszcz parafii w Czermnej. Pewnego dnia w 1776 r. w skarpie koło dzwonnicy w pobliżu kościoła ksiądz Tomaszek zauważył ludzkie czaszki i kości. Wezwał grabarza J. Langera i kościelnego J. Schmidta. Razem zaczęli wygrzebywać znajdujące się płytko pod ziemią szczątki. Nie spodziewali się, że natrafią na tak dużą ilość ludzkich kości. Były to kości ofiar wojen na ziemi kłodzkiej, prawdopodobnie z czasów wojny trzydziestoletniej, prusko-austriackiej i szerzących się po niej epidemiach cholery w XVII i XVIII w. oraz z wojny siedmioletniej (1756–1763). Ksiądz Tomaszek postanowił wydobyć wszystkie szczątki. Polecił grabarzowi i kościelnemu oczyścić, wybielić kości wraz z czaszkami i zgromadzić w kaplicy. Ten fakt oraz doznany wstrząs podczas zwiedzania w 1775 r. katakumb w Rzymie zrodził pomysł wybudowania kaplicy ossuarium.", "kaplica1", "kaplica2", "kaplica3", "kaplica4", 50.45152, 16.24193200000002, 0));
        createAttraction(new Attraction("Muzeum Papiernictwa", "Kłodzka 42, 57-340 Duszniki-Zdrój", "Muzeum Papiernictwa w Dusznikach-Zdroju – placówka muzealna Samorządu Województwa Dolnośląskiego w miejscowości Duszniki-Zdrój, utworzona w 1968 r. w starym XVII-wiecznym młynie papierniczym nad Bystrzycą Dusznicką.", "Młyn papierniczy w Dusznikach to zabytek techniki. Elementami charakterystycznymi dla papierni są dach kryty gontem, zakończony od zachodu barokowym szczytem wolutowym oraz oryginalny pawilon wejściowy w kształcie wieży, a we wnętrzach budynku – polichromia z XVII–XIX stulecia. Obiekt posiada dużą wartość historyczną w skali Polski i jest jednocześnie ciekawą atrakcją turystyczną. 20 września 2011 młyn papierniczy w Dusznikach-Zdroju decyzją Prezydenta RP uzyskał status pomnika historii. Obecnie trwają starania o wpisanie zabytkowej papierni na listę UNESCO.", "muzeum1", "muzeum2", "muzeum3", "muzeum4", 50.4044749, 16.39549039999997, 0));
        createAttraction(new Attraction("Twierdza Kłodzka", "Grodzisko 1, 57-300 Kłodzko", "Twierdza Kłodzko – dobrze zachowana, duża twierdza w Kłodzku będąca systemem obronnym z okresu XVII i XVIII wieku.", "Pierwsza wzmianka o istnieniu grodu obronnego na Górze Fortecznej (Zamkowej) w Kłodzku pochodzi z relacji Kosmasa, czeskiego kronikarza z 981 roku. Należy sądzić, że był to zespół drewnianych budynków otoczonych palisadą – w takiej formie zdobył go i spalił czeski książę Sobiesław w roku 1114. Sobiesław odbudował gród w 1129 roku i osadził w nim kasztelana – Gronzatę.", "twierdza1", "twierdza2", "twierdza3", "twierdza4", 50.4405547, 16.65250400000002, 0));
        createAttraction(new Attraction("Kopalnia złota ZŁOTY STOK", "ul. Złota 7, 57-250 Złoty Stok", "Muzeum Górnictwa i Hutnictwa Złota w Złotym Stoku – placówka muzealna zlokalizowana w dawnej kopalni złota i arsenu w Złotym Stoku w województwie dolnośląskim, uruchomiona 28 maja 1996.", "W dawnej kopalni złota i arsenu 28 maja 1996 uruchomiono Muzeum Górnictwa i Hutnictwa Złota. Muzeum oferuje wystawę obrazującą historię górnictwa złota (stare mapy, narzędzia górnicze, kolekcja tablic ostrzegawczych i informacyjnych z czasów PRL) w sztolni transportowej \"Gertruda\", wyprawę do sztolni \"Czarnej\", w której można zobaczyć 10-metrowy podziemny wodospad, przejażdżkę kolejką kopalnianą, a także płukanie złota.", "kopalnia1", "kopalnia2", "kopalnia3", "kopalnia4", 50.4398727, 16.87473369999998, 0));

    }

    public void insertDistance(int id, double result) {
        ContentValues updateDistance = new ContentValues();
        updateDistance.put(Attractions.COLUMN_NAME_DISTANCE, result);
        mDb.update(Attractions.TABLE_NAME, updateDistance, Attractions._ID + "=" + id, null);

    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }



}
