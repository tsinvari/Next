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
    public static final String[] TABLE_NAME = {"cuello", "dorso", "pierna", "semipierna", "todo"};
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_PRENDA = "prenda";
    public static final String COLUMN_MATERIAL = "material";
    public static final String COLUMN_COLOR = "color";
    public static final String COLUMN_OCASION = "ocasion";
    public static final String COLUMN_ABRIGO = "abrigo";
    public static final String COLUMN_TEMPERATURA = "factortemperatura";
    public static final String COLUMN_PATH = "path";
    public static final String DATABASE_CREATE_SQL1 =
            "CREATE TABLE " + TABLE_NAME[0] +
            "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
            +COLUMN_PRENDA+ " TEXT, "
            +COLUMN_MATERIAL+ " INTEGER,"
            +COLUMN_COLOR+ " INTEGER, "
            + COLUMN_OCASION+ " INTEGER, "
            +COLUMN_ABRIGO+ " INTEGER, "
            +COLUMN_TEMPERATURA+ " INTEGER, "
            + COLUMN_PATH +" TEXT, "
            + "PRIMARY KEY (ID));";
    public static final String DATABASE_CREATE_SQL2 =
            "CREATE TABLE " + TABLE_NAME[1] +
                    "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
                    +COLUMN_PRENDA+ " TEXT, "
                    +COLUMN_MATERIAL+ " INTEGER,"
                    +COLUMN_COLOR+ " INTEGER, "
                    + COLUMN_OCASION+ " INTEGER, "
                    +COLUMN_ABRIGO+ " INTEGER, "
                    +COLUMN_TEMPERATURA+ " INTEGER, "
                    + COLUMN_PATH +" TEXT, "
                    + "PRIMARY KEY (ID));";
    public static final String DATABASE_CREATE_SQL3 =
            "CREATE TABLE " + TABLE_NAME[2] +
                    "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
                    +COLUMN_PRENDA+ " TEXT, "
                    +COLUMN_MATERIAL+ " INTEGER,"
                    +COLUMN_COLOR+ " INTEGER, "
                    + COLUMN_OCASION+ " INTEGER, "
                    +COLUMN_ABRIGO+ " INTEGER, "
                    +COLUMN_TEMPERATURA+ " INTEGER, "
                    + COLUMN_PATH +" TEXT, "
                    + "PRIMARY KEY (ID));";
    public static final String DATABASE_CREATE_SQL4 =
            "CREATE TABLE " + TABLE_NAME[3] +
                    "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
                    +COLUMN_PRENDA+ " TEXT, "
                    +COLUMN_MATERIAL+ " INTEGER,"
                    +COLUMN_COLOR+ " INTEGER, "
                    + COLUMN_OCASION+ " INTEGER, "
                    +COLUMN_ABRIGO+ " INTEGER, "
                    +COLUMN_TEMPERATURA+ " INTEGER, "
                    + COLUMN_PATH +" TEXT, "
                    + "PRIMARY KEY (ID));";
    public static final String DATABASE_CREATE_SQL5 =
            "CREATE TABLE " + TABLE_NAME[4] +
                    "(" +COLUMN_ID+" INTEGER AUTO_INCREMENT, "
                    +COLUMN_PRENDA+ " TEXT, "
                    +COLUMN_MATERIAL+ " INTEGER,"
                    +COLUMN_COLOR+ " INTEGER, "
                    + COLUMN_OCASION+ " INTEGER, "
                    +COLUMN_ABRIGO+ " INTEGER, "
                    +COLUMN_TEMPERATURA+ " INTEGER, "
                    + COLUMN_PATH +" TEXT, "
                    + "PRIMARY KEY (ID));";



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
            _db.execSQL(DATABASE_CREATE_SQL1);
            _db.execSQL(DATABASE_CREATE_SQL2);
            _db.execSQL(DATABASE_CREATE_SQL3);
            _db.execSQL(DATABASE_CREATE_SQL4);
            _db.execSQL(DATABASE_CREATE_SQL5);

        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");

            // Destroy old database:
            for (int i = 0 ; i < 5 ; i++) {
                _db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME[i]);
            }
            // Recreate new database:
            onCreate(_db);
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

            return ;

        }

        public String[]  checkVestuario(/*int abrigo*/){
            int data = 1;
            int total = 0;

            SQLiteDatabase db = this.getReadableDatabase();

            for (int i = 0; i < 5 ; i++){
            String sql = "SELECT * FROM "+TABLE_NAME[i]+";";
            Cursor cursor = db.rawQuery(sql,null);
                total = total + cursor.getCount();
                    if (cursor.getCount() == 0) {
                        data = 0;
                        }
            }
            if (total < 20)
            {data = 0;}
            db.close();
            String[] boh = {""+data, ""+total};
            return boh;

        }

        public String [] generarSugerencia(Double temperatura){
            SQLiteDatabase db = this.getReadableDatabase();
            String [] piezas ={"","",""};
            String[] sugerencia = {"","","","","",""};
            String [] piezaSugerida = {"","",""};
            String [] pathSugerido = {"","",""};
            String sql ;
            int i, j = 0;
            if (temperatura > 25)
            {
                piezas[0] = "semipierna";
                piezas[1] = "dorso";
                for ( i = 0; i < piezas.length ; i++) {

                    sql = "SELECT " + COLUMN_PRENDA + " FROM " + piezas[i] + "WHERE id = ' 1 ';" ;
                    piezaSugerida[i] = db.rawQuery(sql, null).toString();
                    sql = "SELECT " + COLUMN_PATH + " FROM " + piezas[i] + "WHERE id = ' 1 ';";
                    pathSugerido[i] = db.rawQuery(sql, null).toString();
                }
                for ( j = 0; j < piezas.length * 2; j=j+2){

                    sugerencia [j] = piezaSugerida[j];
                    sugerencia [j+1] = pathSugerido[j];
                }
            } else {
                piezas[0] = "pierna";
                piezas[1] = "dorso";
                piezas[2] = "dorso";
                for ( i = 0; i < piezas.length ; i++) {

                    sql = "SELECT " + COLUMN_PRENDA + " FROM " + piezas[i] + " WHERE "+COLUMN_ID+" = ' 0 ';" ;
                    piezaSugerida[i] = db.rawQuery(sql, null).toString();
                    sql = "SELECT " + COLUMN_PATH + " FROM " + piezas[i] + " WHERE "+COLUMN_ID+" = ' 0';";
                    pathSugerido[i] = db.rawQuery(sql, null).toString();
                }
                for ( j = 0; j < 3; j=j+2){

                    sugerencia [j] = piezaSugerida[j];
                    sugerencia [j+1] = pathSugerido[j];
                }

            }




            return sugerencia;
        }

}
