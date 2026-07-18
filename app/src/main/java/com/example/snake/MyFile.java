package com.example.snake;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFile {
    public Context context;
    public String filename;

    public MyFile(Context context, String filename) {
        this.context = context;
        this.filename = filename;
    }


    public boolean isexternalstoragewritable(){
        if(Environment.MEDIA_MOUNTED.equals((Environment.getExternalStorageState()))){
            Log.i("State","Yes it is writable");
            return  true;
        }
        else{
            return false;
        }
    }

    public void writeArray(ArrayList a){
        if (isexternalstoragewritable()) {
            File file = new File(context.getFilesDir(), filename);          // a kapott fájlnévvel megnyitom a fájlt
            //System.out.println(context.getFilesDir());
            try {
                FileWriter writer = new FileWriter(file);
                for(int i=0;i<a.size();++i){
                    int num= (int) a.get(i);
                    writer.write(Integer.toString(num)+" ");                // beír egy számot
                }
                writer.close();
            } catch (IOException e) {
                System.out.println("problema az irassal");
            }
        }
    }

    public ArrayList readArray() {
        File file = new File(context.getFilesDir(), filename);
        ArrayList a;
        a = new ArrayList<>();
        //String s = "";
        try {
            Scanner sc = new Scanner(file);
            int num;
            while (sc.hasNextInt()) {
                num = sc.nextInt();                             // kiolvassa a következő számot
                a.add(num);
                //s += Integer.toString(num) + " ";
            }
            sc.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }


}
