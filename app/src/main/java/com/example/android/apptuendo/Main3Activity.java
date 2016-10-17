package com.example.android.apptuendo;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;




public class Main3Activity extends AppCompatActivity{

    private final Handler mHandler =  new Handler();
    private final Handler nHandler = new Handler();
    private Double temperatura;
    private DatabaseHelper CLOSEDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        updateWeatherData("La Paz, BO");
        openDB();
    }

    private void openDB(){
        CLOSEDB = new DatabaseHelper(this);
    }


    public void generar(View v){
        new Thread(){
            public void run(){
            nHandler.post(new upd());
            }

        }
        .start();

    }

    private void generarUp()
    {
        ListView listGen = (ListView) findViewById(R.id.list);
        String[] d = CLOSEDB.checkVestuario();

        if (Integer.parseInt(d[0]) == 1){

            String[] Carrusel = CLOSEDB.generarSugerencia(temperatura);
            String [] prendaGen={"","","",""};
            String [] pathGen={"","","",""};
            int i = 0;

                for (int j = 0; j<Carrusel.length/2;j++) {
                    prendaGen[j] = Carrusel[i];
                    pathGen[j] = Carrusel[i + 1];
                    i=i+2;
                }



            AdapterGen adapterGG = new AdapterGen(this,prendaGen,pathGen);
            listGen.setAdapter(adapterGG);


        }
            else {
            Mensaje.message(this,"Se necesitan más datos");}

    }


        private void updateWeatherData(final String city){
            new Thread(){
                public void run(){
                    final JSONObject json = RemoteFetch.getJSON(Main3Activity.this, city);

                    if(json == null) {
                        mHandler.post(new Runnable() {
                            public void run() {
                                Mensaje.message(Main3Activity.this,"No se encontraron datos del clima");
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

        private void renderWeather(JSONObject json){
            try {
                TextView temperatura_edit_Text = (TextView) findViewById(R.id.temperatura_edit_text);
                JSONObject main = json.getJSONObject("main");
                temperatura = main.getDouble("temp");
                temperatura_edit_Text.setText( ""+temperatura+ " C");

            }catch(Exception e){
                Log.e("SimpleWeather", "One or more fields not found in the JSON data");
            }
        }

    private class upd implements Runnable{

        @Override
        public void run() {
            generarUp();
        }
    }

}

class AdapterGen extends ArrayAdapter<String>{
    private final Context context;
    private final String[] imagesGen;
    private final String[] prendaGenStr;
    AdapterGen(Context c, String[] prenda, String [] images)
    {
        super(c,R.layout.row,R.id.prenda_text_view,prenda);
        this.context = c;
        this.imagesGen = images;
        this.prendaGenStr = prenda;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View rowGen = null;
        MyViewHolder holder;
        if (rowGen == null){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rowGen = inflater.inflate(R.layout.row,parent,false);
        holder = new MyViewHolder(rowGen);
        rowGen.setTag(holder);}else
        {holder = (MyViewHolder) rowGen.getTag();}

        holder.prendaGen.setText(prendaGenStr[position]);
        holder.imageGen.setImageURI(Uri.parse(imagesGen[position]));
        return rowGen;
    }

    class MyViewHolder{
        final TextView prendaGen;
        final ImageView imageGen;

        MyViewHolder(View v)
        {
            prendaGen = (TextView) v.findViewById(R.id.prenda_text_view);
            imageGen = (ImageView) v.findViewById(R.id.gen_image_view);
        }
    }



}
