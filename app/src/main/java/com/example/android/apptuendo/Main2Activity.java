package com.example.android.apptuendo;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class Main2Activity extends AppCompatActivity {

    private final int REQUEST_CAMERA = 0;
    private final int SELECT_FILE = 1;
    private final Handler gHandler = new Handler();
    private int counter = 0;
    private String userChoosenTask;
    private ImageView ivImage;
    private Bitmap bm = null;
    private DatabaseHelper CLOSEDB;

    private Integer colorInt = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Spinner prendaSpinner = (Spinner) findViewById(R.id.prenda_spinner);
        Spinner materialSpinner = (Spinner) findViewById(R.id.material_spinner);
        Spinner ocasionSpinner = (Spinner) findViewById(R.id.ocasion_spinner);


        ArrayAdapter<CharSequence> prendaAdapter = ArrayAdapter.createFromResource(this, R.array.main2_prenda, android.R.layout.simple_spinner_item);
        prendaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        prendaSpinner.setAdapter(prendaAdapter);


        ArrayAdapter<CharSequence> adapterMaterial = ArrayAdapter.createFromResource(this, R.array.main2_material, android.R.layout.simple_spinner_item);
        adapterMaterial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        materialSpinner.setAdapter(adapterMaterial);


        ArrayAdapter<CharSequence> ocasionAdapter = ArrayAdapter.createFromResource(this, R.array.main2_ocasion, android.R.layout.simple_spinner_item);
        ocasionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ocasionSpinner.setAdapter(ocasionAdapter);
        CLOSEDB = new DatabaseHelper(this);

    }


    public void save (View v)
    {
        new Thread(){
            public void run(){
                gHandler.post(new guardar());
            }

        }
                .start();
    }


    private void sav(){
        Spinner prendaSpinner = (Spinner) findViewById(R.id.prenda_spinner);
        Spinner materialSpinner = (Spinner) findViewById(R.id.material_spinner);
        Spinner ocasionSpinner = (Spinner) findViewById(R.id.ocasion_spinner);


        if (prendaSpinner.getSelectedItemPosition()==0 || materialSpinner.getSelectedItemPosition()==0 || ocasionSpinner.getSelectedItemPosition()==0 || bm == null) {
            Mensaje.message(this, "Debes llenar todos los campos");

        }else {
            int prendaInt = prendaSpinner.getSelectedItemPosition();
            int materialInt = materialSpinner.getSelectedItemPosition();
            int ocasionInt = ocasionSpinner.getSelectedItemPosition();

            String prendaStr = prendaSpinner.getSelectedItem().toString();

            int cuerpo [] = {0, 1, 2, 2, 2, 2, 2, 3, 3, 4, 4, 5, 5};
            int material[] = {0,6,5,4,3,2,1};
            int cuerpoInt = cuerpo[prendaInt];
            materialInt = material[materialInt];

            counter++;
            try {
                String path = Environment.getExternalStorageDirectory().toString();
                OutputStream fOut;
                File NEXT = new File(path, "NEXT");

                if (!NEXT.exists()) {

                    NEXT.mkdir();
                }

                String pathPrenda = Environment.getExternalStorageDirectory().toString() + "/NEXT";
                File newPrendaFile = new File(pathPrenda, "Prenda_" + counter + ".jpg");
                fOut = new FileOutputStream(newPrendaFile);
                Bitmap newPrendaBM = Bitmap.createBitmap(bm);
                newPrendaBM.compress(Bitmap.CompressFormat.JPEG, 95, fOut);
                MediaStore.Images.Media.insertImage(getContentResolver(), newPrendaBM, newPrendaFile.getAbsolutePath(), newPrendaFile.getName());
                CLOSEDB.addPrenda(cuerpoInt, prendaStr, materialInt, colorInt, ocasionInt, newPrendaFile.getAbsolutePath());
                Mensaje.message(this, "Done! ");
            } catch (IOException e) {

            }


        }
    }

    public void selectColor (View v){

        HSVColorPickerDialog cpd = new HSVColorPickerDialog( Main2Activity.this, 0xFF4488CC, new HSVColorPickerDialog.OnColorSelectedListener() {
            @Override
            public void colorSelected(Integer color) {
               colorInt = color;
             }
        });
        cpd.setTitle( "Pick a color" );
        cpd.show();



    }

    public void open(View v) {
        selectImage();
        ivImage = (ImageView) findViewById(R.id.prenda_view);

        }

    private void selectImage() {
        final CharSequence[] items = {"Usar cámara", "Elegir de Galeria",
                "Cancelar"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
        builder.setTitle("Añadir foto de prenda");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(Main2Activity.this);

                if (items[item].equals("Usar cámara")) {
                    userChoosenTask = "Usar cámara";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Elegir de Galeria")) {
                    userChoosenTask = "Elegir de Galeria";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancelar")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Seleccionar Imagen"), SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        bm = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        ivImage.setImageBitmap(bm);
    }

    private void onSelectFromGalleryResult(Intent data) {

        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ivImage.setImageBitmap(bm);
    }
    private class guardar implements Runnable{

        @Override
        public void run() {
            sav();
        }
    }

}
