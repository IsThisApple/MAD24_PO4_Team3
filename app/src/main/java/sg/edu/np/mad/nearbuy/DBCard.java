package sg.edu.np.mad.nearbuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCard extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_CARDS = "cards";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CARD_TYPE = "card_type";
    private static final String COLUMN_CARD_NUMBER = "card_number";
    private static final String COLUMN_EXPIRATION_MONTH = "expiration_month";
    private static final String COLUMN_EXPIRATION_YEAR = "expiration_year";
    private static final String COLUMN_CVN = "cvn";
    private static final String COLUMN_LABEL = "label";

    public DBCard(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_CARDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CARD_TYPE + " TEXT, " +
                COLUMN_CARD_NUMBER + " TEXT, " +
                COLUMN_EXPIRATION_MONTH + " INTEGER, " +
                COLUMN_EXPIRATION_YEAR + " INTEGER, " +
                COLUMN_CVN + " TEXT, " +
                COLUMN_LABEL + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        onCreate(db);
    }

    public boolean addCard(String cardType, String cardNumber, int expMonth, int expYear, String cvn, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARD_TYPE, cardType);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_EXPIRATION_MONTH, expMonth);
        values.put(COLUMN_EXPIRATION_YEAR, expYear);
        values.put(COLUMN_CVN, cvn);
        values.put(COLUMN_LABEL, label);

        long result = db.insert(TABLE_CARDS, null, values);
        return result != -1;
    }

    public Cursor getCardsByType(String cardType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CARDS, null, COLUMN_CARD_TYPE + "=?", new String[]{cardType}, null, null, null);
    }
}
