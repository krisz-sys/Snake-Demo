package com.example.snake;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class GameView extends SurfaceView {

    private SurfaceHolder holder;
    private GameThread gameThread;

    public Paint paint=new Paint();
    public Paint TextPaint=new Paint();
    public Point pos=new Point();
    public SNAKE mySnake;
    public MyGyroscope mygir;
    public OBSTACLE testOBS;
    public BACKGROUND demoBg;
    public MAP map;
    public int screenSizeY,screenSizeX;
    public Context context;
    public GameView(Context context, String filename, ArrayList<DATE> d, ArrayList<Point> bodypartpos, ArrayList others, ArrayList<OBSTACLE> obstacles) {
        super(context);
        this.context=context;
        TextPaint.setColor(Color.BLACK);        //a szoveg szine
        TextPaint.setTextSize(50);
        pos.set(100,500);
        SurfaceAndThreaad();                //ehez a függvényhez ne nyujj, azzel valós időben ki lehet íratni az elemeket
                                            //mozgást lehet megvalósítani

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSizeY = metrics.heightPixels;
        screenSizeX = metrics.widthPixels;
        paint.setColor(Color.RED);

        paint.setTextSize(70);
        //sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        int a[]={R.drawable.brickhor, R.drawable.brickver};
        demoBg=new BACKGROUND(context,70,70,screenSizeX-100,screenSizeY-70,Color.GREEN, R.drawable.backg,a);
        map=new MAP(context,demoBg,filename);

        mygir=new MyGyroscope(context,"Calibration", R.drawable.calibrate,100,100,demoBg.borderDownRight.x,demoBg.borderUpLeft.y+100);

        buildSnake(d,bodypartpos);                      // az utat és a yestrlszpoziciókat lapja meg
        mySnake.ControlWithGyroscope(mygir);
        map.setSnake(mySnake);

        if(obstacles==null) {                           // uj jatek eseten
            testOBS = new OBSTACLE(context, Color.BLUE, 500, 500, 100, 100, R.drawable.apple);          // az etel deklaralasa
            map.addObstacle(testOBS);
            BULLET bullet = new BULLET(context, Color.RED, 500, -100, 100, 100, R.drawable.fireball);   // golyo
            map.addObstacle(bullet);
        }
        else{                                             // mentett jatek adatainak a betoltese
            for(int i=0;i<obstacles.size();++i){
                map.addObstacle(obstacles.get(i));
            }
            //System.out.println("loading: ");
            //System.out.println("obst size; "+obstacles.size());
            mySnake.maxlength= (int) others.get(0);                 // mentett jatek eseten hozzaadjuk a maximalis meretet a kigyonak
            mySnake.lifepoints=(int) others.get(1);
            map.timeCounter.setValue((int)others.get(2));
            map.lifePoints.setValue(mySnake.lifepoints);
            map.maxLen.setValue(mySnake.maxlength);
        }

    }

    public void buildSnake(ArrayList<DATE> d, ArrayList<Point> bodypos){
        mySnake=new SNAKE(d.get(d.size()-1).x,d.get(d.size()-1).y,0,context, R.drawable.snake_body, R.drawable.snake_head);
        mySnake.path=d;

        int n=bodypos.size();
        //System.out.println(n+" db body");
        for(int i=0;i<n;i++){
            if(i%2==1){
                mySnake.BodyParts.get(mySnake.BodyParts.size()-1).oldDist=bodypos.get(i).x;
                mySnake.BodyParts.get(mySnake.BodyParts.size()-1).optdistIndex=bodypos.get(i).y;
            }
            else{
                mySnake.BodyParts.get(mySnake.BodyParts.size()-1).positionx=bodypos.get(i).x;
                mySnake.BodyParts.get(mySnake.BodyParts.size()-1).positiony=bodypos.get(i).y;
                mySnake.grow();
            }
    }
    }
public boolean endGame(){                                   // a NewGame szamlaloban van meghivva
        if(map.snake.lifepoints<=0){
            map.stoppedMovement=true;
            map.options.active=false;
            return true;
        }
        return false;
}

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(map.stoppedMovement && !map.options.finishgame){                                        // ha vege a jateknak akkor kiirja az eredmenyt
            map.draw(canvas);
            map.lengthMsg.setPos(screenSizeX/4,screenSizeY/5);
            map.lengthMsg.setTextSize(100);
            map.lengthMsg.draw(canvas);
            MSG maxlength=new MSG("Max length: ",screenSizeX/4,screenSizeY*2/5,Color.RED,100);
            maxlength.setValue(mySnake.maxlength);
            maxlength.draw(canvas);

            map.timeCounter.setPos(screenSizeX/4,screenSizeY*3/5);
            map.timeCounter.setTextSize(100);
            map.timeCounter.draw(canvas);

            MSG continueMsg=new MSG("Tap the screen to continue!", screenSizeX/8,screenSizeY*4/5,Color.RED,120);
            continueMsg.draw(canvas);
        }
        else if(!map.options.finishgame){               // ha tart még a játék akkor kirajzolja a páját
            map.draw(canvas);
            /*
                String s = "szoggyorsulas:  X : " + Integer.toString((int) mygir.accelerationX) + ",  Y: " + Integer.toString((int) mygir.accelerationY);
                canvas.drawText(s, 500, 200, paint);
                s = "szogsebesseg:  X : " + Integer.toString((int) mygir.velocityX) + ",  Y: " + Integer.toString((int) mygir.velocityY);
                canvas.drawText(s, 500, 450, paint);
                s = "pozicio:  X : " + Integer.toString((int) mySnake.BodyParts.get(0).getposX()) + ",  Y: " + Integer.toString((int) mySnake.BodyParts.get(0).getposY());
                canvas.drawText(s, 500, 700, paint);
                s = "angle: " + Integer.toString((int) mySnake.BodyParts.get(0).Angle);
                canvas.drawText(s, 500, 950, paint);
             */

            mygir.drawCalibrateButton(canvas);
        }
        }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_DOWN) {
            if(map.stoppedMovement){
                map.options.finishgame=true;
                Intent intent=new Intent(context.getApplicationContext(), MainActivity.class) ;                    // ha vege  jateknak akkor kilep
                context.startActivity(intent);
            }

        //if(event.getAction()==MotionEvent.ACTION_MOVE) {
            int x=(int)event.getX();
            int y=(int)event.getY();
            if(!mygir.setCalivrate(event)) {
                mygir.NewCalibrate(event);
            }
            map.onClick(event);
        }
        invalidate();
        return true;
    }



   public void SurfaceAndThreaad(){

        gameThread = new GameThread(this);
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                gameThread.setRunning(true);
                gameThread.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                boolean retry = true;
                gameThread.setRunning(false);
                while (retry) {
                    try {
                        gameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {

                    }
                }

            }
        });
    }

}
