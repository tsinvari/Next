package com.example.android.next;


import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import static android.widget.Toast.makeText;


public class Main3Activity extends AppCompatActivity {

    int temperatura = 0;
    int abrigo = 0;
    int cuerpo = 0;
    int prenda = 0;
    DatabaseHelper CLOSEDB;
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

       /* EditText temperatura_edit_Text = (EditText) findViewById(R.id.temperatura_edit_text);
        temperatura = Integer.parseInt(temperatura_edit_Text.getText().toString());


        if (temperatura == 15)
        {   abrigo = 3;}
        else {if (temperatura > 15)
                    {abrigo = 2;}
                    else {if (temperatura < 15)
                                {abrigo = 4;}
                        }
            }*/
        int d = CLOSEDB.getPrenda();
        Toast toast = makeText(Main3Activity.this, "" + d, Toast.LENGTH_SHORT);
        toast.show();


    }





}
