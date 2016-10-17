package com.example.android.apptuendo;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sir.tsinvari on 10/9/16.
 */

class Mensaje {
    public static void message(Context c, String message){
        Toast.makeText(c,message,Toast.LENGTH_SHORT).show();
    }

}
