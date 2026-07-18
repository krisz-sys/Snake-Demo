package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.io.File;
import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class Options {
    public BUTTON saveButton;
    public BUTTON changecontrolmode;
    public boolean controlMode=true;           //false gyroscope control, true tuchcontrol
    public BUTTON[] sensitivityorspeed;         // 0: easy,   1: medium,   2:height,   3: extreme
    public int sensitiviytState=1;
    public BUTTON exitButton;
    private int buttonstate=-1;
    public int  activateButtonColor;
    public int  activateTextColor;
    public int  noneClickesColor;
    public int  noneClickesTextColor;
    public Context context;
    public Point screenSize=new Point();
    public boolean active=false;
    public int speed=10;
    public ArrayList<BUTTON> optionButtons;
    public SNAKE snake;
    public String filename;
    public boolean finishgame=false;
    public ArrayList<OBSTACLE> obtacles;
    public MSG timerCounter;

    public Options(Context context,String filename){
        this.filename=filename;
        this.context=context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize.y = metrics.heightPixels;
        screenSize.x = metrics.widthPixels;
        activateButtonColor= Color.RED;
        activateTextColor=Color.BLACK;
        noneClickesColor=Color.BLACK;
        noneClickesTextColor=Color.RED;
        saveButton=new BUTTON(150,400,screenSize.x/2,100,noneClickesColor,noneClickesTextColor,"SAVE");
        changecontrolmode=new BUTTON(150,400,screenSize.x/2,300,noneClickesColor,noneClickesTextColor,"CHANGE");
        changecontrolmode.setTextCorrigation(-85,20);
        sensitivityorspeed=new BUTTON[4];
        int a=screenSize.x/4;
        sensitivityorspeed[0]=new BUTTON(150,200,a,500,noneClickesColor,noneClickesTextColor,"SLOW");
        sensitivityorspeed[1]=new BUTTON(150,200,a+200,500,activateButtonColor,activateTextColor,"MOD");
        sensitivityorspeed[2]=new BUTTON(150,200,a+400,500,noneClickesColor,noneClickesTextColor,"FAST");
        sensitivityorspeed[3]=new BUTTON(150,350,a+500+175,500,noneClickesColor,noneClickesTextColor,"EXTREM");
        sensitivityorspeed[3].setTextCorrigation(-80,15);
        exitButton=new BUTTON(150,400,screenSize.x/2,700,noneClickesColor,noneClickesTextColor,"EXIT");
        optionButtons=new ArrayList<>();
        optionButtons.add(sensitivityorspeed[0]);
        optionButtons.add(sensitivityorspeed[1]);
        optionButtons.add(sensitivityorspeed[2]);
        optionButtons.add(sensitivityorspeed[3]);
        optionButtons.add(changecontrolmode);
        optionButtons.add(saveButton);
        optionButtons.add(exitButton);
    }

    public void setTimerCounter(MSG timerCounter) {
        this.timerCounter = timerCounter;
    }

    public void setSnake(SNAKE snake){
        this.snake=snake;
    }

    public void setObtacles(ArrayList<OBSTACLE> obtacles) {
        this.obtacles = obtacles;
    }

    public void draw(Canvas canvas) {
        if (active) {
            saveButton.draw(canvas);
            changecontrolmode.draw(canvas);
            sensitivityorspeed[0].draw(canvas);
            sensitivityorspeed[1].draw(canvas);
            sensitivityorspeed[2].draw(canvas);
            sensitivityorspeed[3].draw(canvas);
            exitButton.draw(canvas);
        }
    }

    public boolean onClick(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();

        BUTTON b;
        for(int i=0;i<optionButtons.size();++i){
            b=optionButtons.get(i);
            if(b.left<=x && b.right>=x && b.top<=y && b.bot>=y) {        //save the actual game
                if (b.buttcolor.getColor() == noneClickesColor && i<=3) {
                    switch (i){
                        case 0: speed=30 ;break;
                        case 1: speed=20 ;break;
                        case 2: speed=10 ;break;
                        case 3: speed=4 ;break;
                    }
                    sensitivityorspeed[sensitiviytState].setButtColor(noneClickesColor);
                    sensitivityorspeed[sensitiviytState].setTextColor(noneClickesTextColor);
                    b.setButtColor(activateButtonColor);
                    b.setTextColor(activateTextColor);
                    sensitiviytState=i;
                    break;
                }
                else if(b.buttcolor.getColor() == noneClickesColor){
                    if(buttonstate!=-1){
                        optionButtons.get(buttonstate).setButtColor(noneClickesColor);
                        optionButtons.get(buttonstate).setTextColor(noneClickesTextColor);
                    }
                    b.setButtColor(activateButtonColor);
                    b.setTextColor(activateTextColor);
                    buttonstate=i;
                    if(i==4){
                        controlMode=true;
                    }
                    break;
                }
                else if(b.buttcolor.getColor() == activateButtonColor){
                    b.setButtColor(noneClickesColor);
                    b.setTextColor(noneClickesTextColor);
                    if(i==4){
                        controlMode=false;
                        break;
                    }
                    else if(i==5){
                        savethegame();           // elmenti az aktualis ertekeket
                    }
                    else if(i==6){
                        finishgame=true;
                         Intent intent=new Intent(context.getApplicationContext(), MainActivity.class) ;                    // itt visszalep a mainAcivityba
                        context.startActivity(intent);
                    }
                }
            }
        }
        return false;
    }
    public  void savethegame(){
        snake.moveX=0;
        snake.moveY=0;
        ArrayList ar;
        ar=new ArrayList();
        int n=snake.path.size();
        //System.out.println(n*3+" szamot ment el");
        ar.add(n*3);
        for(int i=0;i<n;++i) {                      // elmentem a a kigyo utjat
            DATE d = snake.path.get(i);
            ar.add(d.x);
            ar.add(d.y);
            ar.add(d.angle);
            int m=ar.size();
            //System.out.println(ar.get(m-3)+"  "+ar.get(m-2)+"  "+ar.get(m-1));
        }

        n=snake.BodyParts.size();
        ar.add(n*4);
        for(int i=0;i<n;++i){                               // elmentem a kigyo pozicioit
               BODY body=snake.BodyParts.get(i);
               int x= (int) body.positionx;
               int y= (int) body.positiony;
               ar.add(x);
               ar.add(y);
               ar.add((int)snake.BodyParts.get(i).oldDist);
               ar.add((int)snake.BodyParts.get(i).optdistIndex);
        }

        ar.add(snake.maxlength);                            // eltarolom a kigyo maximalis meretet
        ar.add(snake.lifepoints);                           // elmentem hogy aktualisan hany elete van a kigyonak
        ar.add(timerCounter.value);
        System.out.println("snake lifes: "+snake.lifepoints);

        ar.add(obtacles.size());                            // a tomb meretet eltarolom

        System.out.println("saving: ");
        System.out.println("obst size: "+obtacles.size());
        for(int i=0;i<obtacles.size();++i){

            if(obtacles.get(i).getClass().equals(BULLET.class)){
                BULLET bull= (BULLET) obtacles.get(i);
                ar.add(1);                                  // az 1 azt jelenti hogy egy BULLET tipusu elem ertekeit taroljuk el
                ar.add((int)bull.posX);                 //eltarolom a koordinatakat
                ar.add((int)bull.posY);
                ar.add((int)bull.moveX);                // a poziciojuk mellett a sebesseguket is el kell menteni
                ar.add((int)bull.moveY);
                System.out.println((int)bull.posX+"     "+(int)bull.posY);
            }
            else{
                ar.add(0);                                   // OBSTACLE tipusu elem ertekeit taroljuk el
                ar.add(obtacles.get(i).position.x);                 //eltarolom a koordinatakat
                ar.add(obtacles.get(i).position.y);
                System.out.println(obtacles.get(i).position.x + "     " + obtacles.get(i).position.y);
            }

        }


        MyFile myfile= new MyFile(context,filename);            //elmentem az adatokat egy txt fájlba, ezt a SavedGames activityben olvasom ki
        myfile.writeArray(ar);
        ar.clear();
    }

}
