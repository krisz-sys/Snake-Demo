package com.example.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class BULLET extends OBSTACLE {
    public double alfa=0;
    public float movingDestX,movingDestY;
    public double moveX,moveY;
    public float speed=5;
    public Point screenSize=new Point();
    public float posX=0,posY=10;
    public boolean movingEN=true;
    public BULLET(Context context, int color, int posX, int posY, int heigth, int width, int imgID) {
        super(context, color, posX, posY, heigth, width, imgID);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize.y = metrics.heightPixels;
        screenSize.x = metrics.widthPixels;
        this.posX=posX;
        this.posY=posY;
        bmap= BODY.getCircledBitmap(bmap);
    }

    @Override
    public void draw(Canvas canvas) {
        if(movingEN) {
            if (posY + 50 < 0) {
                float x = (float) (Math.random() * screenSize.x);
                float y = (float) 0;
                goTo(x, y);
            }
            posX += moveX;
            posY += moveY;
            setPosition((int) (posX + moveX), (int) (posY + moveY));
        }
        canvas.drawBitmap(bmap,posX,posY,null);
        //canvas.drawCircle(centerX,centerY,10,paint);
    }

    public void goTo(float destX,float destY) {
        movingDestX = destX;
        movingDestY=destY;
        posX=(float) (Math.random()*screenSize.x);
        posY=screenSize.y;
        double alfa= (double) Math.atan((movingDestY-posY)/(movingDestX-posX));
        moveX=  (speed*Math.cos(alfa));
        moveY=  (speed*Math.sin(alfa));
        if(posX>movingDestX){
            moveX*=-1;
            moveY*=-1;
        }
    }

    @Override
    public void setPosition(int x, int y) {
        posX=x;
        posY=y;
        centerX= (int) (posX+width/2);
        centerY= (int) (posY+heigth/2);
    }

    public void setMove(int x,int y){
        moveX=(double)x;
        moveY=(double)y;
    }
}
