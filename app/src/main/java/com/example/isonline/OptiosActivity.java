package com.example.isonline;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class OptiosActivity extends AppCompatActivity {


    private EditText TimeOutvalue;
    private EditText PacketSizevalue;
    private EditText Peticionvalue;
    String conf_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optios);
        TimeOutvalue = (EditText)findViewById(R.id.TimeOutValue);
        PacketSizevalue = (EditText)findViewById(R.id.PacketSizeValue);
        Peticionvalue = (EditText)findViewById(R.id.PeticionValue);
        String archivos []  = fileList();

        if (FileExist(archivos, "conf.txt")){
            try {
                InputStreamReader archivo = new InputStreamReader(openFileInput("conf.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                conf_txt = linea;
                br.close();
                archivo.close();
                //ping -W 2000 -s 32 -c 10
                Peticionvalue.setText(conf_txt.substring(conf_txt.lastIndexOf('c') + 2, conf_txt.length() - 1));
                PacketSizevalue.setText(conf_txt.substring(conf_txt.lastIndexOf('s') + 2, conf_txt.lastIndexOf('c') - 2));
                TimeOutvalue.setText(conf_txt.substring(conf_txt.lastIndexOf('W') + 2, conf_txt.lastIndexOf('s') - 2));
            }catch (IOException e){

            }
        }
    }

    private boolean FileExist(String[] archivos, String name ) {
        for (int i = 0; i < archivos.length; i++) {
            if (name.equals((archivos[i])))
                return true;
        }
        return false;
    }

    public void OnSaveOptions(View view){

        String tmp = "ping -W " + TimeOutvalue.getText().toString() + " -s " + PacketSizevalue.getText().toString() +
                " -c " + Peticionvalue.getText().toString() + " ";

        Toast.makeText(this, tmp, Toast.LENGTH_SHORT).show();

        try {
            OutputStreamWriter archivo = new OutputStreamWriter(openFileOutput("conf.txt", Activity.MODE_PRIVATE));
            archivo.write(tmp);
            archivo.flush();
            archivo.close();
        } catch (IOException e) {
            Toast.makeText(this, "No se pudo guardar", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
    }
}