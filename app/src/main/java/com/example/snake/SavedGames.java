package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SavedGames extends AppCompatActivity {

    public ListView listview;
    public Context context;
    public ArrayList<String> FileList=new ArrayList<>();
    public ArrayAdapter arrayAdapter;
    public Button loadbutton;
    public Button backtoMain;
    public Button deleteButton;
    public String chosedFile="";
    public int lastelementIndex=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);               // kiveszem a címet és a fejlécet
        getSupportActionBar().hide();
        setContentView(R.layout.activity_saved_games);

        context=this;
        loadbutton=(Button)findViewById(R.id.load);
        backtoMain=(Button)findViewById(R.id.backtoMain);
        deleteButton=(Button)findViewById(R.id.deleteGame);
        listview= (ListView)findViewById(R.id.list);                        // lista deklarálása

        String[] pathlist;
        File file=new File(this.getFilesDir().toString());                      // a fájlista lekéréséhez szügség van egy fájlra
        pathlist=file.list();                                                   //lekérem a fájllistát
        for (String pathname : pathlist) {
            System.out.println(pathname);
            FileList.add(pathname);                                             // a fájlistát átadom a tömbnek
        }

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,FileList);           // hozzáadaom a fájlnekevet ehez. Az Arrayadapter átalakítja úgy hogy a listwiev osztály tudja használni
        listview.setAdapter(arrayAdapter);                                      // megkapja a fájlistát
        listview.setBackgroundColor(Color.WHITE);                               // fehérre állítom a lista hátterét

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(lastelementIndex!=-1) {                                                      // ha ki volt választva valami
                    listview.getChildAt(lastelementIndex).setBackgroundColor(Color.WHITE);      // fehérre állítom az előző elemet
                }
                chosedFile=FileList.get(i);                                                 //kivalaszt egy mentett fajlt
                System.out.println(chosedFile);
                listview.getChildAt(i).setBackgroundColor(Color.CYAN);                      // az új kiválasztott fál szint cserél
                lastelementIndex=i;
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(lastelementIndex!=-1) {                                                      // ha ki van választva egy elem
                    Toast.makeText(SavedGames.this, FileList.get(lastelementIndex) + " deleted", Toast.LENGTH_SHORT).show();// kiiratom hogy törlöm
                    String filename = FileList.get(lastelementIndex);
                    File file = new File(context.getFilesDir(), filename);                              //törlöm magát a fájlt
                    file.delete();
                    FileList.remove(lastelementIndex);                                                      // a listából is törlöm az értéket
                    arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, FileList);    //az arrayadaptert újraalkotom az új listával

                    listview.setAdapter(arrayAdapter);
                    lastelementIndex=-1;                                                    // mivel töröltem az elemet nincsen semmi kiválasztva
                }
                else{
                    Toast.makeText(SavedGames.this, "chose a saved game", Toast.LENGTH_SHORT).show();           // ha nincsen semmi kiválasztva tovább megy
                }
            }
        });


        loadbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!chosedFile.equals("")){
                    Intent i=new Intent(getApplicationContext(), NewGame.class);
                    MyFile myfile=new MyFile(context,chosedFile);                           // egy rombbe kiolvasom az ertekeket a fajlbol
                    ArrayList ar=myfile.readArray();                                        // ezt a NewGame activityben fogom dekodolni
                    //System.out.println("ezt kuldom:  "+ar.toString());
                    i.putExtra("array",ar);                                             // átküldöm az adattömböt
                    // System.out.println(ar.toString());
                    //String.valueOf(R.string.path_array)
                    i.putExtra("new game",chosedFile);                   //a NewGame actitivtynek küldöm  fájlnevet
                    //String.valueOf(R.string.filename)
                    startActivity(i);
                }
                else{
                    Toast.makeText(SavedGames.this, "chose a saved game", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backtoMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}