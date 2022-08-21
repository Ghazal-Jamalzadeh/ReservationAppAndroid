package ir.tamuk.reservation.fragments.ui.reservation.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ir.tamuk.reservation.fragments.ui.reservation.adapter.ReserveModel;

public class SqlDatabaseReserve extends SQLiteOpenHelper {
    String TABLE_NAME = "Reserved";
    String TABLE_NAME_2 = "orderd";

    public SqlDatabaseReserve(Context context) {
        super(context, "sqliteReserved", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME +
                "(ida TEXT PRIMARY KEY , " +
                " time TEXT ," +
                " reserved INTEGER , " +
                " service TEXT)");

        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_NAME_2 + "( id TEXT PRIMARY KEY )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<ReserveModel> getData() {

        ArrayList<ReserveModel> productsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (c.moveToFirst()) {
            do {
                // Passing values
                String id = c.getString(0);
                String time = c.getString(1);
                int reserved = c.getInt(2);
                String service = c.getString(3);
                ReserveModel reserveModel = new ReserveModel(id, time, reserved, service);
                productsList.add(reserveModel);

                // Do something Here with values
            } while (c.moveToNext());
        }
        c.close();

        return productsList;


    }

    public ArrayList<ReserveModel> getDataId() {

        ArrayList<ReserveModel> productsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);
        if (c.moveToFirst()) {
            do {
                // Passing values
                String id = c.getString(0);
                ReserveModel reserveModel = new ReserveModel(id);
                productsList.add(reserveModel);

                // Do something Here with values
            } while (c.moveToNext());
        }
        c.close();

        return productsList;


    }

    public boolean getByIdO(String id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select id from orderd where id = ?", new String[]{id + ""});
        if (c.moveToFirst()) {
            return true;


        }
        c.close();

        return false;
    }

    //SELECT Find ID
    public String getById(String id) {
        SQLiteDatabase db = getReadableDatabase();
        @SuppressLint("Recycle") Cursor c = db.rawQuery("Select ida from Reserved where ida = ?", new String[]{id + ""});
//        if (c.moveToFirst()){
//        }while(c.moveToNext());
//        c.close();
//        return id;
        if (c != null && c.moveToFirst()) {
            return c.getString(0);
        } else {
            return null;  // because you have to return something
        }
    }

    //DELETE
    public void delete(String id) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(TABLE_NAME, "ida=?", new String[]{id + ""});

    }

    public void deleteId(String id) {
        SQLiteDatabase database = this.getReadableDatabase();
        database.delete(TABLE_NAME_2, "id=?", new String[]{id + ""});

    }

    //INSERT
    public void Insert(String ida, String time, int reserved, String service) {

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ida", ida);
        contentValues.put("time", time);
        contentValues.put("reserved", reserved);
        contentValues.put("service", service);
        database.insert(TABLE_NAME, null, contentValues);

    }

    public void Insert(String id) {

        SQLiteDatabase database = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        database.insert(TABLE_NAME_2, null, contentValues);

    }
}
