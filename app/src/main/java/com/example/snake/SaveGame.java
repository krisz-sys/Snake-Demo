package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SaveGame extends AppCompatActivity {

    public ArrayList array=new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);       // nincsen fejléc és cím
        getSupportActionBar().hide();
        setContentView(R.layout.activity_save_game);

        EditText editText=(EditText)findViewById(R.id.Gamename);
        Button savebutton=(Button)findViewById(R.id.savegamebutton);
        Button backButton=(Button)findViewById(R.id.back);


        savebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s=editText.getText().toString();
                if(!s.equals("")){                                          // ha be van írva valami fájlnév akkor létre lehet hozni egy új játákot
                    Intent i=new Intent(getApplicationContext(), NewGame.class);
                    s+=".txt";                                                  //átalakítom txt formáttumba és ezzel a névvel hozom létre
                    i.putExtra("new game",s);
                    ArrayList data=new ArrayList();
                    data.add(1);                    // a tomb hossza 1: uj jatekot csinalunk
                    data.add(500);
                    data.add(500);
                    data.add(0);
                    i.putExtra("array",data);                       //a NewGame actitivtynek küldik át az adatot át
                    startActivity(i);
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}