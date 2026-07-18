package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.util.ArrayList;

public class BODY {
    public int BodyImageID;
    public double positionx,positiony;
    public int Angle;
    public Context context;
    public Bitmap bmap;
    public Point SnakeSize;
    public int optimalDist;
    public int oldDist=0;
    public int optdistIndex=0;
    public double centerX,centerY;
    public BODY(int bodyImageID, int positionX, int positionY,int snakesizeX,int snakesizeY,Context context) {
        BodyImageID = bodyImageID;
        //position = new Point();
        positionx = positionX;
        positiony = positionY;
        SnakeSize = new Point();
        SnakeSize.x = snakesizeX;
        SnakeSize.y = snakesizeY;
        this.context = context;
        this.bmap = BitmapFactory.decodeResource(context.getResources(), BodyImageID);
        this.bmap = Bitmap.createScaledBitmap(bmap, snakesizeX, snakesizeY, true);
        bmap=getCircledBitmap(bmap);
        optimalDist=snakesizeX/2;
        centerX=positionx+SnakeSize.x/2;
        centerY=positiony+SnakeSize.y/2;
    }

    public void draw(Canvas canvas) {

        canvas.rotate(Angle,(int)positionx+SnakeSize.x/2,(int)positiony+SnakeSize.y/2);
        canvas.drawBitmap(bmap, (int)positionx, (int)positiony, null);
        canvas.rotate(-Angle,(int)positionx+SnakeSize.x/2,(int)positiony+SnakeSize.y/2);

        /*
                Paint paint=new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle((int)centerX,(int)centerY,10, paint);
         */
    }


    public void setAngle(int angle) {
        Angle = angle;
    }

    public int getAngle() {
        return Angle;
    }

    public double getposX(){
        return positionx;
    }
    public double getposY(){
        return positiony;
    }

    public void  setposX(double x){
        positionx=x;
        centerX=positionx+SnakeSize.x/2;
    }
    public void  setposY(double y) {
        positiony = y;
        centerY=positiony+SnakeSize.y/2;

    }
    public void free()
    {
        this.BodyImageID = 0;
        this.positionx = 0;
        this.positiony = 0;
        setAngle(0);
        this.SnakeSize.x = 0;
        this.SnakeSize.y = 0;
    }

    public void setPosition(double positionX,double positionY) {
        this.positionx=positionX;
        this.positiony=positionY;
        centerX=positionx+SnakeSize.x/2;
        centerY=positiony+SnakeSize.y/2;
    }


    public void calculatDistanceIndex(ArrayList<DATE> p , int posX, int posY, int j) {                      //kiszámítja hogy a úton hól helyezkedik el a testrész
        //System.out.println(j+"   "+p.size());
        int d;
        //int dold=0;
        int error=10000;
        if(j>=p.size()){
            j=p.size()*3/4;
        }
        for (int i = j; i > 0; --i) {

            d = (int) SNAKE.distance(posX, posY, p.get(i).x,p.get(i).y);            // tavolsag az aktualis pozicio es a vizsgalt pont kozott
            int newerr=Math.abs(d-optimalDist);                                     // hiba szamitas
            if(newerr<error) {                                                      // osszehasonlitom a regi hibaval
                error = newerr;
                oldDist = d;
                //positionx = p.get(i).x;
                //positiony = p.get(i).y;
                setPosition(p.get(i).x,p.get(i).y);
                optdistIndex = i;
                Angle = p.get(i).angle;
            }
            if(newerr>60){
                break;
            }
        }
    }

    int  calculateRotation(double velocityX, double velocityY){
        Angle= (int) Math.toDegrees(Math.atan(velocityY/velocityX));
        //Angle=90- Angle;
        Angle=- Angle;
        if(velocityX<0){
            Angle-=180;
        }

        return Angle;
    }

    public static Bitmap getCircledBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}


