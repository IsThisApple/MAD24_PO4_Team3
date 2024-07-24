package sg.edu.np.mad.nearbuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAddress extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "AddressDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "addresses";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_STREET = "street";
    private static final String COLUMN_POSTAL_CODE = "postal_code";
    private static final String COLUMN_UNIT_NUMBER = "unit_number";
    private static final String COLUMN_LABEL = "label";

    public DBAddress(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_STREET + " TEXT, " +
                COLUMN_POSTAL_CODE + " TEXT, " +
                COLUMN_UNIT_NUMBER + " TEXT, " +
                COLUMN_LABEL + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void deleteAddress(String addressLabel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_LABEL + " = ?", new String[]{addressLabel});
        db.close();
    }

    public void addAddress(String street, String postalCode, String unitNumber, String label) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STREET, street);
        values.put(COLUMN_POSTAL_CODE, postalCode);
        values.put(COLUMN_UNIT_NUMBER, unitNumber);
        values.put(COLUMN_LABEL, label);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Cursor getAllAddresses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public boolean isLabelUnique(String label) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_LABEL},
                COLUMN_LABEL + " = ?", new String[]{label}, null, null, null);
        boolean unique = (cursor.getCount() == 0);
        cursor.close();
        return unique;
    }

    public boolean isPostalCodeUnique(String postalCode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_POSTAL_CODE},
                COLUMN_POSTAL_CODE + " = ?", new String[]{postalCode}, null, null, null);
        boolean unique = (cursor.getCount() == 0);
        cursor.close();
        return unique;
    }
}
