package com.example.isonline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView objtext;
    EditText url;
    ScrollView scroll;
    String command = "ping -c 10 ";
    Spinner combobox;
    ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        url = (EditText) findViewById(R.id.edt1);
        objtext = (TextView) findViewById(R.id.textView);
        scroll = (ScrollView) findViewById(R.id.scroll);
        combobox = (Spinner) findViewById(R.id.Combobox);
        InitSpinner();
    }
    //********************************************************************************************** Initialize Functions

    private void InitSpinner(){
        String archivos [] = fileList();

        if (FileExist(archivos, "URLs.txt")){
            try {
                InputStreamReader archivo = new InputStreamReader(openFileInput("URLs.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                command = linea;
                br.close();
                archivo.close();
            }catch (IOException e){

            }
        }

        arrayList.add("127.0.0.1");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arrayList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        combobox.setAdapter(arrayAdapter);
        combobox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                url.setText(parent.getItemAtPosition(position).toString());
                PingRun();
            }
            @Override
            public void onNothingSelected(AdapterView <?> parent) {
            }
        });

    }

    //********************************************************************************************** Buttons...

    public void onClickPingButton(View view){
        PingRun();
    }

    private void PingRun(){
        if (!arrayList.contains(url.getText().toString())){
            arrayList.add(url.getText().toString());
        }

        String archivos [] = fileList();

        if (FileExist(archivos, "conf.txt")){
            try {
                InputStreamReader archivo = new InputStreamReader(openFileInput("conf.txt"));
                BufferedReader br = new BufferedReader(archivo);
                String linea = br.readLine();
                command = linea;
                br.close();
                archivo.close();
            }catch (IOException e){

            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                validarPingIP(command, url.getText().toString());
            }
        }).start();
    }

    private boolean FileExist(String[] archivos, String name ){
        for (int i = 0; i<archivos.length; i++){
            if (name.equals((archivos[i])))
                return true;
        }
        return  false;
    }

    public void ChangeActivityOptions(){
        Intent siguiente = new Intent(this, OptiosActivity.class);
        startActivity(siguiente);
    }

    //********************************************************************************************** MENU

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.item1){
            ChangeActivityOptions();
        }else if (id == R.id.item2){
            objtext.setText("");
        }

        return super.onOptionsItemSelected(item);
    }

    //********************************************************************************************** Ping Functions!!!
    public void setObjtext(String text){
        objtext.post(new Runnable() {
            @Override
            public void run() {
                objtext.append(text);
            }
        });
        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }

    public void validarPingIP(String command, String IP ){
        String pingCMD = "/system/bin/" + command + IP;
        String Minimo="", Maximo="", Media="";

        try{
            Runtime ejecuta = Runtime.getRuntime();
            Process proceso = ejecuta.exec( pingCMD );

            InputStreamReader entrada = new InputStreamReader( proceso.getInputStream() );
            BufferedReader buffer = new BufferedReader( entrada );

            String linea = "";

            for( ;(linea = buffer.readLine() ) != null; ){

                setObjtext(linea + "\n");

            }

            buffer.close();
        }
        catch( IOException e ){
//System.out.println( e );
            setObjtext(e.toString() + "\n");
        }
        catch( Exception e ){
//System.out.println( e );
            setObjtext(e.toString() + "\n");
        }

        setObjtext("Done!\n");
    }
}