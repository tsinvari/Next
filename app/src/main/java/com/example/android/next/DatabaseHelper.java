package com.example.android.next;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by tsinvari on 27-09-16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    public static final String DB_NAME = "closetDB";
    public static final String TABLE_NAME = "vestuario";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRENDA = "prenda";
    public static final String COLUMN_MATERIAL = "material";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_OCASION = "ocasion";
    public static final String COLUMN_ABRIGO = "abrigo";
    public static final String COLUMN_CUERPO = "cuerpo";
    public static final String COLUMN_PATH = "path";
    private static final String DATABASE_CREATE_SQL = "CREATE TABLE " +TABLE_NAME
            +"(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "+COLUMN_PRENDA+
            " TEXT, " +COLUMN_MATERIAL+ " INTEGER,"+COLUMN_COLOR+ " INTEGER, "+
            COLUMN_OCASION+ " INTEGER, "+COLUMN_ABRIGO+ " INTEGER, "+COLUMN_CUERPO +
            " INTEGER, "+ COLUMN_PATH +" TEXT, PRIMARY KEY (ID));";

        private static final int DB_VERSION =2;


        static DatabaseHelper sInstance;
        SQLiteDatabase db;

        DatabaseHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public static DatabaseHelper getsInstance(Context context){
        if (sInstance == null){
            sInstance = new DatabaseHelper(context);
        }
        return sInstance;
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            _db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

            // Recreate new database:
            onCreate(_db);
        }




        public void addPrenda(String prenda, int material, int color, int ocasion, int abrigo, int cuerpo, String path){

            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues Values = new ContentValues();


            Values.put(COLUMN_PRENDA,prenda);
            Values.put(COLUMN_MATERIAL,material);
            Values.put(COLUMN_COLOR,color);
            Values.put(COLUMN_OCASION,ocasion);
            Values.put(COLUMN_ABRIGO,abrigo);
            Values.put(COLUMN_CUERPO,cuerpo);
            Values.put(COLUMN_PATH,path);

            db.insert(TABLE_NAME, null, Values);
            db.close();

            return ;

        }

        public int getPrenda(/*int abrigo*/){
            SQLiteDatabase db = this.getReadableDatabase();
            String sql = "SELECT * FROM "+TABLE_NAME+";";
            Cursor cursor = db.rawQuery(sql,null);
            int data = cursor.getCount();

            db.close();
            return data;
        }

}
