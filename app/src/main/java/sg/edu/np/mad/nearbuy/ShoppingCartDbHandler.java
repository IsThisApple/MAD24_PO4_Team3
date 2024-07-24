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
                    + PRODUCT_IMAGE + " INTEGER, "
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

    public boolean isProductExist(String productName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + PRODUCT_NAME + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{productName});

        boolean exists = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return exists;
    }

    public void addQuantity(Product product, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        Integer quantityToAdd = product.getQuantity();
        double totalprice = product.getTotalprice();
        Cursor cursor = db.query(TABLE_NAME, new String[]{PRODUCT_QUANTITY, TOTAL_PRODUCT_PRICE},
                PRODUCT_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int currentquantity = cursor.getInt(0);
            double currenttotalprice = cursor.getDouble(1);
            int newquantity = currentquantity + quantityToAdd;
            double newtotalprice = currenttotalprice + totalprice;
            values.put(PRODUCT_QUANTITY, newquantity);
            values.put(TOTAL_PRODUCT_PRICE, newtotalprice);
            db.update(TABLE_NAME, values, PRODUCT_NAME + "=?" ,new String[]{name});
        }
        db.close();
    }

    public void addSingleQuantity(Product product, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double price = Double.parseDouble(product.getPrice());
        Cursor cursor = db.query(TABLE_NAME, new String[]{PRODUCT_QUANTITY, TOTAL_PRODUCT_PRICE},
                PRODUCT_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int currentquantity = cursor.getInt(0);
            double currenttotalprice = cursor.getDouble(1);
            int newquantity = currentquantity + 1;
            double newtotalprice = currenttotalprice + price;
            values.put(PRODUCT_QUANTITY, newquantity);
            values.put(TOTAL_PRODUCT_PRICE, newtotalprice);
            db.update(TABLE_NAME, values, PRODUCT_NAME + "=?" ,new String[]{name});
        }
        db.close();
    }

    public void subtractSingleQuantity(Product product, String name) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        double price = Double.parseDouble(product.getPrice());
        Cursor cursor = db.query(TABLE_NAME, new String[]{PRODUCT_QUANTITY, TOTAL_PRODUCT_PRICE},
                PRODUCT_NAME + "=?", new String[]{name}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int currentquantity = cursor.getInt(0);
            double currenttotalprice = cursor.getDouble(1);
            int newquantity = currentquantity - 1;
            double newtotalprice = currenttotalprice - price;
            values.put(PRODUCT_QUANTITY, newquantity);
            values.put(TOTAL_PRODUCT_PRICE, newtotalprice);
            db.update(TABLE_NAME, values, PRODUCT_NAME + "=?" ,new String[]{name});
        }
        db.close();
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> productList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(1);
                Integer img = cursor.getInt(2);
                String price = String.format("%.2f",cursor.getDouble(3));
                Integer quantity = cursor.getInt(4);
                Double total_price = cursor.getDouble(5);
                Product product = new Product(name,price,img);
                product.quantity = quantity;
                product.totalprice = total_price;
                productList.add(product);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void deleteProduct(String productname){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PRODUCT_NAME + "=?", new String[]{productname});
        db.close();
    }

    public void clearAllProducts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }



}