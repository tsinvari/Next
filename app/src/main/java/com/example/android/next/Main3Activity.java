package com.example.android.next;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import static android.widget.Toast.makeText;


public class Main3Activity extends AppCompatActivity {

    Double temperatura;
    int abrigo = 0;
    int cuerpo = 0;
    int prenda = 0;
    DatabaseHelper CLOSEDB;
    private Handler mHandler =  new Handler();
    private Bitmap bm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        openDB();


    }



    private void openDB(){
        CLOSEDB = new DatabaseHelper(this);
    }


    public void generar (View v){

        updateWeatherData("La Paz, BO");
        String[] d = CLOSEDB.checkVestuario();
        /*Double t = Double.parseDouble(temperatura_edit_Text.getText().toString());*/
        if (Integer.parseInt(d[0]) == 1){
            String[] Carrusel = CLOSEDB.generarSugerencia(20.0);
            ImageView prendaGen = (ImageView) findViewById(R.id.gen_image_view);
            TextView prendaNombre = (TextView) findViewById(R.id.prenda_text_view);
            /*int f = (Carrusel.length+1)/2;
            for (int i = 0; i < f; i++)
            {*/
                prendaNombre.setText(Carrusel[0]);
                prendaGen.setImageURI(Uri.parse(Carrusel[1]));


        }
        else {
            Toast toast = makeText(Main3Activity.this, "Se necesitan mas datos"+d[1], Toast.LENGTH_SHORT);
            toast.show();
        }

    }


        public void updateWeatherData(final String city){
            new Thread(){
                public void run(){
                    final JSONObject json = RemoteFetch.getJSON(Main3Activity.this, city);

                    if(json == null) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                Toast.makeText(Main3Activity.this,
                                        Main3Activity.this.getString(R.string.place_not_found),
                                        Toast.LENGTH_LONG).show();
                            }
                        });

                    } else {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                renderWeather(json);
                            }
                        });


                    }

                }
            }.start();
        }

        public void renderWeather(JSONObject json){
            try {
                TextView temperatura_edit_Text = (TextView) findViewById(R.id.temperatura_edit_text);
                JSONObject main = json.getJSONObject("main");
                temperatura = main.getDouble("temp");;
                temperatura_edit_Text.setText( ""+temperatura+ " C");

            }catch(Exception e){
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            };
        }
}
