package com.example.snake;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NewGame extends AppCompatActivity {


    public GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);           // kiveszi a fejlécet és a címet
        getSupportActionBar().hide();
        Intent i=getIntent();
        String s=i.getStringExtra("new game");
        //String.valueOf(R.string.filename)


            Intent intent = getIntent();                                                             //ha letrehoz vagy betölt egy játékot
            ArrayList arr = intent.getIntegerArrayListExtra("array");                    //átveszem az int tömböt
            Arrays arrays =decodeArray(arr);                                                    //az int tömböt átalakítom DATE formátumba
            gameView=new GameView(this,s,arrays.path,arrays.bodypos,arrays.others,arrays.obstacles);                // deklarálja a saját grafikus felületet
            setContentView(gameView);


        Timer T=new Timer();
        T.scheduleAtFixedRate(new TimerTask() {                     //elinditok egy szamalot egy threadel
            @Override
            public void run() {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {

                        if ( !gameView.map.optionButt.active) {                                             // ez addig fog működni amig ez igaz nem lesz
                            System.out.println("counter: " + gameView.map.timeCounter.value);
                            gameView.map.timeCounter.setValue(gameView.map.timeCounter.value + 1);        // meghivja a grafikus felulet fuggvenyeit
                        }
                        if(gameView.endGame()){
                            T.cancel();                                                                     // leálítja a számlálót
                        }
                    }
                });
            }
        }, 1000, 1000);         // egy ms-kent szamol egyet

    }




    public Arrays decodeArray(ArrayList arr){                                           // létrehozzuk az Arrays adatstruktura elemeit
        ArrayList<DATE> d;                                                              // ide a pájapoziciók értékei jönnek
        ArrayList<Point> positions;                                                     // ide a kígyó testrészeinek a poziciói jönnek
        ArrayList others=new ArrayList();                                               // egyébb adatok pl számláló értéke
        ArrayList<OBSTACLE> obstacles=new ArrayList<>();                                // az étel és a golyó poziciói jönnek ide
        d=new ArrayList<>();
        positions=new ArrayList<>();
        Arrays arrays=new Arrays(d,positions,others,obstacles);
        int n= (int) arr.get(0);

        if(n==1){
            d.add(new DATE((int)arr.get(1),(int)arr.get(2),(int)arr.get(3)));       //ha a tömb hossza 1 akkor azt jelenti hogy nem egy mentett játékot nyitunk meg hanem újat csináltunk
            Point p=new Point();
            p.set((int)arr.get(1),(int)arr.get(2));
            positions.add(p);
            arrays.others=null;
            arrays.obstacles=null;
            return arrays;
        }
        int i=1;
        for( i=1;i<n;){
            DATE date=new DATE((int)arr.get(i++),(int)arr.get(i++),(int)arr.get(i++));      // az út adatainak az átmásolása
            //System.out.println(i+": "+date.toString());
            d.add(date);
        }


        n= (int) arr.get(i);
        n+=i;
        i++;
        for(;i<n;){
            int x=(int)arr.get(i++);
            int y=(int)arr.get(i++);
            Point p=new Point();
            p.set(x,y);
            positions.add(p);                                               // a kígyó testrészeinek a koordinátaáinak az átmásolása
        }

        others.add((int)arr.get(i++));                // ez a kigyo max hossza
        others.add((int)arr.get(i++));                // ez a kigyo aktualis hossza
        others.add((int)arr.get(i++));                // ez a szamlalo erteke

        n+=3;
        System.out.println("obst: "+(int)arr.get(i));
        n+=(int)arr.get(i++)*3;
        System.out.println("n: "+n);
        for(;i<n;){                                                                     // a BULLET típusú elemeket létrehozom a kimásolt adatok alapján
            if((int)arr.get(i++)==1){
                BULLET obs;
                n+=2;
                int x=(int)arr.get(i++);
                int y=(int)arr.get(i++);
                obs=new BULLET(this, Color.RED,x,y,100,100, R.drawable.fireball);
                obs.setMove((int)arr.get(i++),(int)arr.get(i++));
                obstacles.add(obs);
            }
            else {
                OBSTACLE obs;
                int x=(int)arr.get(i++);
                int y=(int)arr.get(i++);
                obs=new OBSTACLE(this, Color.RED,x,y,100,100, R.drawable.apple);
                obstacles.add(obs);
            }
            System.out.println("x: "+ obstacles.get(obstacles.size()-1).position.x+ "  y: "+obstacles.get(obstacles.size()-1).position.y);

        }

        System.out.println("decoding: ");
        System.out.println("obs size: "+obstacles.size());
        return arrays;
    }

    public class Arrays{
        public ArrayList<DATE> path=new ArrayList<>();                  // kigyó út
        public ArrayList<Point> bodypos=new ArrayList<>();              //kígyó koordinátái
        public ArrayList others=new ArrayList();                        // egyebb ertek
        public ArrayList<OBSTACLE> obstacles= new ArrayList<>();        // a golyó és az étel
        public Arrays(ArrayList<DATE> path, ArrayList<Point> bodypos,ArrayList others, ArrayList<OBSTACLE> obstacles) {
            this.path = path;
            this.bodypos = bodypos;
            this.others=others;
            this.obstacles=obstacles;
        }
    }

}