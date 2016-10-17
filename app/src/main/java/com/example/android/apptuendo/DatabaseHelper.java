package com.example.android.apptuendo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by tsinvari on 27-09-16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {



    private static final String DB_NAME = "closetDB";
    private static final String[] TABLE_NAME = {"cuello", "dorso", "pierna", "semipierna", "todo"};
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_PRENDA = "prenda";
    private static final String COLUMN_MATERIAL = "material";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_OCASION = "ocasion";
    private static final String COLUMN_ABRIGO = "abrigo";
    private static final String COLUMN_PATH = "path";
    private static final int DB_VERSION =1;
        private static DatabaseHelper sInstance;
    private final String[] DATABASE_CREATE_SQL = {"","","","",""};

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
            tableNames();
            for (int i = 0 ; i < TABLE_NAME.length ; i++) {
                try {
                    _db.execSQL(DATABASE_CREATE_SQL[i]);

                }catch (SQLException e){
                    Log.d("SQL", "error al crear");
                }

            }
            Log.d("SQL", "DataBase Creada");
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            try {
                for (int i = 0; i < TABLE_NAME.length; i++) {
                    _db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[i]);
                }
                // Recreate new database:
                onCreate(_db);
            }catch (SQLException e){Log.d("SQL","error al upgrade");

            }
        }


        private void tableNames(){
            for (int i = 0 ; i < TABLE_NAME.length ; i++) {
                DATABASE_CREATE_SQL[i] = "CREATE TABLE " + TABLE_NAME[i] +
                        "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
                        +COLUMN_PRENDA+ " TEXT, "
                        +COLUMN_MATERIAL+ " INTEGER,"
                        +COLUMN_COLOR+ " INTEGER, "
                        + COLUMN_OCASION+ " INTEGER, "
                        +COLUMN_ABRIGO+ " INTEGER, "
                        + COLUMN_PATH +" TEXT, "
                        + "PRIMARY KEY (ID));";}

        }

        public void addPrenda(int cuerpo, String prenda, int material, Integer color, int ocasion, String path){
            int factorCuerpo [] = {0, 1, 2, 4, 2,  3};
            int abrigo= factorCuerpo [cuerpo] * material;
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues Values = new ContentValues();

            Values.put(COLUMN_PRENDA,prenda);
            Values.put(COLUMN_MATERIAL,material);
            Values.put(COLUMN_COLOR,color);
            Values.put(COLUMN_OCASION,ocasion);
            Values.put(COLUMN_ABRIGO,abrigo);
            Values.put(COLUMN_PATH,path);

            db.insert(TABLE_NAME[cuerpo-1], null, Values);
            db.close();

        }

        public String[]  checkVestuario(/*int abrigo*/){
            int data = 1;
            int total = 0;
            Cursor cursor=null;

            SQLiteDatabase db = this.getReadableDatabase();

            for (int i = 0; i < 5 ; i++){
            String sql = "SELECT * FROM "+TABLE_NAME[i]+";";
            cursor = db.rawQuery(sql,null);
                total = total + cursor.getCount();
                    if (cursor.getCount() == 0) {
                        data = 0;
                        }
            }
            if (total < 3)
            {data = 0;}
            db.close();
            cursor.close();
            return new String[] {""+data, ""+total};

        }

        public String [] generarSugerencia(Double temperatura){
            SQLiteDatabase db = this.getWritableDatabase();
            String[] sugerencia = {"","","","","",""};
            String order;
            Cursor c=null;
            int i = 0;
            int j;
            String[] columns = {COLUMN_PRENDA,COLUMN_PATH};
            if (temperatura>15) {
                order = COLUMN_ABRIGO + " ASC";
                for (j = 0; j < 2; j++) {
                    c = db.query(TABLE_NAME[j + 1], columns, null, null, null, null, order);
                    c.moveToFirst();
                    int index1 = c.getColumnIndex(COLUMN_PRENDA);
                    int index2 = c.getColumnIndex(COLUMN_PATH);
                    sugerencia[i] = c.getString(index1);
                    sugerencia[i + 1] = c.getString(index2);
                    i=i+2;

                }
                c.close();
            }else{
                    order= COLUMN_ABRIGO+ " DESC";
                    for (j = 0; j<3; j++) {
                        c = db.query(TABLE_NAME[j], columns, null, null, null, null, order);
                        c.moveToFirst();
                        sugerencia[i] = c.getString(c.getColumnIndex(COLUMN_PRENDA));
                        sugerencia[i + 1] = c.getString(c.getColumnIndex(COLUMN_PATH));
                        i=i+2;
                }
                c.close();
            }

            return sugerencia;
        }

}
