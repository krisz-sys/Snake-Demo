package com.example.snake;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public Point saveButtpos=new Point();
    public Point newGameButtpos=new Point();
    public Point exitButtPos=new Point();
    public Point cancelButtpos=new Point();
    public boolean exitstate=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);           // nincsen cim és fejléc
        getSupportActionBar().hide();
        //setContentView(new GameView(this));
        setContentView(R.layout.activity_main);


        Button savedgames=(Button)findViewById(R.id.savedgamesbutton);              // gombok deklarálása
        Button newgame=(Button)findViewById(R.id.newgamebutton);
        Button exit=(Button)findViewById(R.id.gamexit);
        Button cancelButton=(Button)findViewById(R.id.cancel);
        cancelButton.setX(-500);                                    // a cancel gomb alapból ezen a koordinátán kezd
        cancelButton.setY(-500);


        savedgames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), SavedGames.class);             //átugrik a mentett játékokra
                startActivity(i);
            }
        });

        newgame.setOnClickListener(new View.OnClickListener() {                             // új játékot kezdünk
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(), SaveGame.class);
                startActivity(i);
            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButtpos.set((int)savedgames.getX(),(int)savedgames.getY());     // lementjük a gombok pozicióit
                newGameButtpos.set((int)newgame.getX(),(int)newgame.getY());
                exitButtPos.set((int)exit.getX(),(int)exit.getY());
                cancelButtpos.set((int)cancelButton.getX(),(int)cancelButton.getY());
                System.out.println(exitButtPos.toString());
                newgame.setX(-500);                         // amig a felhasználó eldönti hogy tényleg ki akar-e lépni, ezeket a gombokat elrejti a játék
                newgame.setY(-500);
                savedgames.setX(-500);
                savedgames.setY(-500);
                cancelButton.setX(900);                     // A cancel gomb kell látszodjon ezért más poziciót kap
                cancelButton.setY(500);
                exit.setY(500);
                if(exitstate) {
                    finishAffinity();                                   // kilép az egész játékból
                }
                exitstate=true;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitstate=false;
                newgame.setX(newGameButtpos.x);             // úgy döntött a felhasználó, hogy mégsem lép ki a játkból, ezért minden gomb visszakapja az eredeti pozicióját
                newgame.setY(newGameButtpos.y);
                savedgames.setX(saveButtpos.x);
                savedgames.setY(saveButtpos.y);
                cancelButton.setX(cancelButtpos.x);
                cancelButton.setY(cancelButtpos.y);
                exit.setX(exitButtPos.x);
                exit.setY(exitButtPos.y);
            }
        });

    }

}