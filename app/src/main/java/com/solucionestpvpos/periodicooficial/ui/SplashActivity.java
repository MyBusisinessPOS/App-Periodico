package com.solucionestpvpos.periodicooficial.ui;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.solucionestpvpos.periodicooficial.R;
import com.solucionestpvpos.periodicooficial.utils.Utils;

public class SplashActivity extends AppCompatActivity {

    //PERMISSION request constant, assign any value
    private static final int STORAGE_PERMISSION_CODE = 100;
    private static final String TAG = "PERMISSION_TAG";

    private AlertDialog.Builder alerDialoBuilder;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Utils.isPermissionGranted(this)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                alerDialoBuilder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.all_files_permission))
                        .setMessage(getString(R.string.restrictions_files_permission))
                        .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })
                        .setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), getString(R.string.app_name) + " cannot function without the permission", Toast.LENGTH_LONG).show();
                                exitApp();
                            }
                        })
                        .setIcon(R.drawable.logo);
                         alertDialog = alerDialoBuilder.create();
            }else{

                alerDialoBuilder = new AlertDialog.Builder(this)
                        .setTitle(getString(R.string.all_files_permission))
                        .setMessage(getString(R.string.please_allow_storage_permission))
                        .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermission();
                            }
                        })
                        .setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), getString(R.string.app_name) + " cannot function without the permission", Toast.LENGTH_LONG).show();
                                exitApp();
                            }
                        })
                        .setIcon(R.drawable.logo);
                alertDialog = alerDialoBuilder.create();
            }


            Thread splashThread = new Thread() {
                @Override
                public void run() {
                    try {
                        int waited = 0;
                        while (waited < 4000) {
                            sleep(100);
                            waited += 100;
                        }

                    } catch (InterruptedException e) {
                        // do nothing
                    } finally {

                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                alertDialog.show();
                            }
                        });


                    }
                }
            };
            splashThread.start();


        } else {
            goToMainScreen();
        }
    }

    private void goToMainScreen() {
        //Creamos el hilo para controlar la visibilidad de la actividad
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (waited < 5000) {
                        sleep(100);
                        waited += 100;
                    }
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    //Abrimos la actividad del login
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        splashThread.start();
    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d(TAG, "onActivityResult: ");
                    //here we will handle the result of our intent
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                        //Android is 11(R) or above
                        if (Environment.isExternalStorageManager()){
                            //Manage External Storage Permission is granted
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is granted");
                            //createFolder();
                        }
                        else{
                            //Manage External Storage Permission is denied
                            Log.d(TAG, "onActivityResult: Manage External Storage Permission is denied");
                            Toast.makeText(SplashActivity.this, "Manage External Storage Permission is denied", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        //Android is below 11(R)
                    }
                }
            }
    );


    private void requestPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            try {
                Log.d(TAG, "requestPermission: try");

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }
            catch (Exception e){
                Log.e(TAG, "requestPermission: catch", e);
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }
        else {
            //Android is below 11(R)
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    STORAGE_PERMISSION_CODE
            );
        }
    }


    public boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            //Android is 11(R) or above
            return Environment.isExternalStorageManager();
        }
        else{
            //Android is below 11(R)
            int write = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED && read == PackageManager.PERMISSION_GRANTED;
        }
    }

    /*Handle permission request results*/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE){
            if (grantResults.length > 0){
                //check each permission if granted or not
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (write && read){
                    //External Storage permissions granted
                    Log.d(TAG, "onRequestPermissionsResult: External Storage permissions granted");
                  //  createFolder();
                }
                else{
                    //External Storage permission denied
                    Log.d(TAG, "onRequestPermissionsResult: External Storage permission denied");
                    Toast.makeText(this, "External Storage permission denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void exitApp() {
        finish();
    }
}