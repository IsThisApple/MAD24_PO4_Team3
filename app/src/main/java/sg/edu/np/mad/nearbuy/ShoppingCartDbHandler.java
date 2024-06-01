package sg.edu.np.mad.nearbuy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class ShoppingCartDbHandler extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shoppingcart.db";
    public static final String TABLE_NAME = "shoppingcartproducts";
    public static final String COLUMN_ID = "id";
    public static final String PRODUCT_NAME = "nameofproduct";
    public static final String PRODUCT_IMAGE = "imageofproduct";
    public static final String PRODUCT_PRICE = "priceofproduct";
    public static final String PRODUCT_QUANTITY = "quantityofproduct";
    public static final String TOTAL_PRODUCT_PRICE = "totalpriceofproduct";
    public ShoppingCartDbHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        try {
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_NAME +  "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + PRODUCT_NAME + " TEXT, "
                    + PRODUCT_IMAGE + "INTEGER, "
                    + PRODUCT_PRICE + " DOUBLE, "
                    + PRODUCT_QUANTITY + " INTEGER, "
                    + TOTAL_PRODUCT_PRICE + " DOUBLE)";
            db.execSQL(CREATE_USERS_TABLE);
            Log.d("Database Operations", "Table created successfully.");
        }catch (SQLiteException e){
            Log.e("Database Operations", "Error creating table: " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addProduct(Product product){
        ContentValues values = new ContentValues();
        values.put(PRODUCT_NAME, product.getName());
        values.put(PRODUCT_IMAGE, product.getProductimg());
        values.put(PRODUCT_PRICE, String.valueOf(product.getPrice()));
        values.put(PRODUCT_QUANTITY, product.getQuantity());
        values.put(TOTAL_PRODUCT_PRICE, product.getTotalprice());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addQuantity(String productName, int quantityToAdd) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{PRODUCT_QUANTITY, PRODUCT_PRICE},
                PRODUCT_NAME + "=?",
                new String[]{productName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int quantityIndex = cursor.getColumnIndex(PRODUCT_QUANTITY);
            int priceIndex = cursor.getColumnIndex(PRODUCT_PRICE);

            if (quantityIndex != -1 && priceIndex != -1) {
                int currentQuantity = cursor.getInt(quantityIndex);
                double pricePerUnit = cursor.getDouble(priceIndex);

                int newQuantity = currentQuantity + quantityToAdd;
                double newTotalPrice = newQuantity * pricePerUnit;

                ContentValues values = new ContentValues();
                values.put(PRODUCT_QUANTITY, newQuantity);
                values.put(TOTAL_PRODUCT_PRICE, newTotalPrice);

                db.update(TABLE_NAME, values, PRODUCT_NAME + "=?", new String[]{productName});
                Log.d("Database Operations", "Product quantity updated successfully.");
            } else {
                Log.e("Database Operations", "Error: Invalid column index.");
            }

            cursor.close();
        } else {
            Log.e("Database Operations", "Product not found.");
        }
        db.close();
    }

    public void subtractQuantity(String productName, int quantityToSubtract) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,
                new String[]{PRODUCT_QUANTITY, PRODUCT_PRICE},
                PRODUCT_NAME + "=?",
                new String[]{productName}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int quantityIndex = cursor.getColumnIndex(PRODUCT_QUANTITY);
            int priceIndex = cursor.getColumnIndex(PRODUCT_PRICE);

            if (quantityIndex != -1 && priceIndex != -1) {
                int currentQuantity = cursor.getInt(quantityIndex);
                double pricePerUnit = cursor.getDouble(priceIndex);

                int newQuantity = currentQuantity - quantityToSubtract;
                if (newQuantity < 0) {
                    Log.e("Database Operations", "Error: Quantity cannot be negative.");
                    newQuantity = 0; // Ensure the quantity does not go below 0
                }
                double newTotalPrice = newQuantity * pricePerUnit;

                ContentValues values = new ContentValues();
                values.put(PRODUCT_QUANTITY, newQuantity);
                values.put(TOTAL_PRODUCT_PRICE, newTotalPrice);

                db.update(TABLE_NAME, values, PRODUCT_NAME + "=?", new String[]{productName});
                Log.d("Database Operations", "Product quantity updated successfully.");
            } else {
                Log.e("Database Operations", "Error: Invalid column index.");
            }

            cursor.close();
        } else {
            Log.e("Database Operations", "Product not found.");
        }
        db.close();
    }

    public Cursor getAllProducts() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

}
