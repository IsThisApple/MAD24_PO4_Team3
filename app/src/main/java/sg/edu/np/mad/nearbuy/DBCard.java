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

    // Create the cards table
    @Override
    public void onCreate(SQLiteDatabase db) {
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

    // Update table schema if needed
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_CARDS + " ADD COLUMN " + COLUMN_USER_ID + " TEXT");
        }
    }

    // Add a new card
    public boolean addCard(String userId, String cardType, String cardNumber, int expMonth, int expYear, String cvn, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_CARD_TYPE, cardType);
        values.put(COLUMN_CARD_NUMBER, cardNumber);
        values.put(COLUMN_EXPIRATION_MONTH, expMonth);
        values.put(COLUMN_EXPIRATION_YEAR, expYear);
        values.put(COLUMN_CVN, cvn);
        values.put(COLUMN_LABEL, label);

        long result = db.insert(TABLE_CARDS, null, values);
        return result != -1;
    }

    // Check if the card number is unique for a user and card type
    public boolean isCardNumberUnique(String userId, String cardType, String cardNumber) {
        Cursor cursor = getCardsByType(userId, cardType);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String existingCardNumber = cursor.getString(cursor.getColumnIndex("cardNumber"));
                if (cardNumber.equals(existingCardNumber)) {
                    cursor.close();
                    return false;
                }
            }
            cursor.close();
        }
        return true;
    }

    // Retrieve cards based on user ID and card type
    public Cursor getCardsByType(String userId, String cardType) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_CARDS, null, COLUMN_USER_ID + "=? AND " + COLUMN_CARD_TYPE + "=?", new String[]{userId, cardType}, null, null, null);
    }

    // Delete a card by user ID and label
    public boolean deleteCard(String userId, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_CARDS, COLUMN_USER_ID + "=? AND " + COLUMN_LABEL + "=?", new String[]{userId, label});
        return rowsAffected > 0;
    }

}