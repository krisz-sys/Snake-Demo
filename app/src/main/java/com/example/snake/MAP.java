package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import java.util.ArrayList;
import java.util.Random;

public class MAP {
    public final Point screenSize = new Point();
    private Context context;
    public BACKGROUND background;
    public ArrayList<OBSTACLE> obstacleArrayList;
    public SNAKE snake;
    public Paint greenpaint=new Paint();
    //public BULLET bullet;
    public MSG lengthMsg,lifePoints,timeCounter,maxLen;
    public OptionsButton optionButt;
    public Options options;
    public boolean stoppedMovement=false;
    public MAP(Context context, BACKGROUND background, String filename) {
        this.context = context;
        this.background = background;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize.y = metrics.heightPixels;
        screenSize.x = metrics.widthPixels;
        obstacleArrayList = new ArrayList<>();
        greenpaint.setColor(Color.GREEN);
        //bullet=new BULLET(context,Color.RED,500,-100,100,100, R.drawable.rock);
        //obstacleArrayList.add(bullet);
        lengthMsg=new MSG("Length of snake: ",screenSize.x/9,50,Color.GREEN,50);
        lifePoints=new MSG("Lifes: ",screenSize.x*2/5,50,Color.GREEN,50);
        lifePoints.setValue(5);                                                                                    // alapbol mennyi elete legyen a kigyonak
        timeCounter=new MSG("Timer: ",screenSize.x*4/7,50,Color.GREEN,50);
        maxLen= new MSG("max length: ",screenSize.x*3/4,50,Color.RED,50);

        optionButt=new OptionsButton(90,90,screenSize.x-50,50,0,0,"", R.drawable.options, R.drawable.backbutton,context);
        options=new Options(context,filename);

        options.setObtacles(obstacleArrayList);
        options.setTimerCounter(timeCounter);
    }

    public void setObstacleArrayList(ArrayList<OBSTACLE> obstacleArrayList) {
        this.obstacleArrayList = obstacleArrayList;

    }

    public  void jumpToSaveGame(){
        Intent in;
        in = new Intent(context.getApplicationContext(), SaveGame.class);
        //i.putExtra("extra",ar);
        context.startActivity(in);
    }

    public void addObstacle(OBSTACLE newObst) {
        obstacleArrayList.add(newObst);
    }

    public void setBackground(BACKGROUND background) {
        this.background = background;
    }

    public void setSnake(SNAKE snake) {
        this.snake = snake;
        this.snake.setBorders(background.borderUpLeft.x,background.borderUpLeft.y,background.borderDownRight.x,background.borderDownRight.y);
        lengthMsg.setValue(this.snake.BodyParts.size());
        options.setSnake(snake);
        snake.setBackground(background);
        lifePoints.setValue(snake.lifepoints);
    }

    public void draw(Canvas canvas) {
        if(stoppedMovement && !options.finishgame){
            background.draw(canvas);
            //bullet.movingEN=false;

            for(int i=0;i<obstacleArrayList.size();++i){
                if(obstacleArrayList.get(i).getClass().equals(BULLET.class)){
                    BULLET bull=(BULLET)obstacleArrayList.get(i);
                    bull.movingEN=false;
                }
            }
            snake.movingEN=false;
            for (int i = 0; i < obstacleArrayList.size(); ++i) {
                obstacleArrayList.get(i).draw(canvas);              // az etel es a golyo kirajzolasa
            }
            snake.drawSnake(canvas);
            snake.setSpeed(options.speed);
        }
        else if(!options.finishgame){
            background.draw(canvas);
            for (int i = 0; i < obstacleArrayList.size(); ++i) {
                obstacleArrayList.get(i).draw(canvas);              // az etel es a golyo kirajzolasa
            }
            snake.drawSnake(canvas);
            snake.setSpeed(options.speed);
            collision();
            lengthMsg.draw(canvas);             // a kigyo merete
            lifePoints.draw(canvas);            // eletpontok
            timeCounter.draw(canvas);           // idozito
            maxLen.draw(canvas);
            maxLen.setValue(snake.maxlength);
            optionButt.draw(canvas);
            options.draw(canvas);
            //bullet.movingEN=!optionButt.active;

            for(int i=0;i<obstacleArrayList.size();++i){
                if(obstacleArrayList.get(i).getClass().equals(BULLET.class)){
                    BULLET bull=(BULLET)obstacleArrayList.get(i);
                    bull.movingEN=!optionButt.active;
                }
            }

            snake.movingEN=!optionButt.active;
            options.active=optionButt.active;
            snake.controlMode=options.controlMode;
        }
    }

    public void onClick(MotionEvent event){
        optionButt.onClick(event);
        if(options.active) {
            options.onClick(event);
        }
        snake.onClick(event);
    }


    public void collision() {
        //ha a kigyo utkozik valamelyik akadalyal akkor ez lekezeli
        int d = snake.BodyParts.get(0).optimalDist+20;
        boolean voltUtkozes = false;


        for (int i = snake.BodyParts.size()-1; i >5 ; --i) {                    // az önmagával való ütközés implementációja
            double dist= SNAKE.distance((float)snake.BodyParts.get(0).positionx,(float)snake.BodyParts.get(0).positiony,(float)snake.BodyParts.get(i).positionx,(float)snake.BodyParts.get(i).positiony);

            if (dist<50) {                      // TODO: itt lepjen ki a jatekbol
                snake.lifepoints--;
                lifePoints.setValue(snake.lifepoints);
                while(snake.BodyParts.size()>1){
                    snake.BodyParts.remove(snake.BodyParts.size()-1);
                    snake.removeLastPathElements(5);
                }
                lengthMsg.setValue(snake.BodyParts.size());
                voltUtkozes=true;
                break;
            }
        }

        if (!voltUtkozes) {
            for (int i = 0; i < obstacleArrayList.size(); i++) {            // az étellel való ütközés implementációja
                //System.out.println(obstacleArrayList.get(i).centerX+"   "+obstacleArrayList.get(i).centerY);
                int dist = (int) SNAKE.distance(obstacleArrayList.get(i).centerX, obstacleArrayList.get(i).centerY, (int) snake.BodyParts.get(0).centerX, (int) snake.BodyParts.get(0).centerY);
                if (dist < d+snake.BodyParts.get(0).SnakeSize.x/2 && obstacleArrayList.get(i).getClass()== OBSTACLE.class) {
                    snake.grow();
                    Random r = new Random();
                    int x = r.nextInt(background.borderDownRight.x - obstacleArrayList.get(i).width - background.borderUpLeft.x) + background.borderUpLeft.x;
                    int y = r.nextInt(background.borderDownRight.y - obstacleArrayList.get(i).heigth - background.borderUpLeft.y) + background.borderUpLeft.y;
                    obstacleArrayList.get(i).setPosition(x, y);
                    voltUtkozes=true;
                    lengthMsg.setValue(snake.BodyParts.size());
                    break;
                }
            }
        }

            for (int i = 0; i < obstacleArrayList.size(); i++) {      // a golyóval való ütközés implementációja
                for (int j = 0; j < snake.BodyParts.size(); ++j) {
                   // System.out.println(obstacleArrayList.get(i).centerX + "   " + obstacleArrayList.get(i).centerY);
                    int dist = (int) SNAKE.distance(obstacleArrayList.get(i).centerX, obstacleArrayList.get(i).centerY, (int) snake.BodyParts.get(j).centerX, (int) snake.BodyParts.get(j).centerY);
                    if (dist < d && obstacleArrayList.get(i).getClass() == BULLET.class) {
                        snake.lifepoints--;
                        lifePoints.setValue(snake.lifepoints);
                        if(j==0){
                            j=1;                    // TODO: itt lepjen ki a jatekbol
                        }
                        int len=snake.BodyParts.size();
                        //System.out.println(snake.BodyParts.size() +"   "+j);
                        for(int k=0;k<len-1;++k){
                            snake.BodyParts.remove(snake.BodyParts.size()-1);
                            snake.removeLastPathElements(5);
                            //System.out.print(k+" ");
                        }
                        //System.out.println();
                        lengthMsg.setValue(snake.BodyParts.size());
                        break;
                    }
                }
            }
    }
}