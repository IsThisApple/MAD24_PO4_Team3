package sg.edu.np.mad.nearbuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBCard extends SQLiteOpenHelper {

    // Database name and version
    private static final String DATABASE_NAME = "cards.db";
    private static final int DATABASE_VERSION = 2;

    // Table and column names
    private static final String TABLE_CARDS = "cards";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_USER_ID = "user_id";
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
        // Create the cards table
        String createTable = "CREATE TABLE " + TABLE_CARDS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " TEXT, " +
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
        // Drop the old table if it exists
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        // Create the table again
        onCreate(db);
    }

    // Add a card to the database
    public boolean addCard(String userId, String cardType, String cardNumber, int expirationMonth,
                           int expirationYear, String cvn, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USER_ID, userId);
        contentValues.put(COLUMN_CARD_TYPE, cardType);
        contentValues.put(COLUMN_CARD_NUMBER, cardNumber);
        contentValues.put(COLUMN_EXPIRATION_MONTH, expirationMonth);
        contentValues.put(COLUMN_EXPIRATION_YEAR, expirationYear);
        contentValues.put(COLUMN_CVN, cvn);
        contentValues.put(COLUMN_LABEL, label);

        long result = db.insert(TABLE_CARDS, null, contentValues);
        return result != -1; // Return true if insert was successful
    }

    // Check if the card number is unique for the user and card type
    public boolean isCardNumberUnique(String userId, String cardType, String cardNumber) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CARDS + " WHERE " +
                COLUMN_USER_ID + " = ? AND " +
                COLUMN_CARD_TYPE + " = ? AND " +
                COLUMN_CARD_NUMBER + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, cardType, cardNumber});
        boolean isUnique = !cursor.moveToFirst(); // Return true if no matching record found
        cursor.close();
        return isUnique;
    }

    // Check if the label is unique for the user and card type
    public boolean isLabelUnique(String userId, String cardType, String label) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CARDS + " WHERE " +
                COLUMN_USER_ID + " = ? AND " +
                COLUMN_CARD_TYPE + " = ? AND " +
                COLUMN_LABEL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId, cardType, label});
        boolean isUnique = !cursor.moveToFirst(); // Return true if no matching record found
        cursor.close();
        return isUnique;
    }

    // Get cards by type and user
    public Cursor getCardsByType(String userId, String cardType) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_CARDS + " WHERE " +
                COLUMN_USER_ID + " = ? AND " +
                COLUMN_CARD_TYPE + " = ?";
        return db.rawQuery(query, new String[]{userId, cardType});
    }

    public boolean deleteCard(String userId, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_CARDS, COLUMN_USER_ID + " = ? AND " + COLUMN_LABEL + " = ?", new String[]{userId, label}) > 0;
    }

}
