package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;

public class OBSTACLE {
    public Point position=new Point();
    public Paint paint=new Paint();
    public int heigth,width;            //center
    public Context context;
    public int imgID;
    public Bitmap bmap;
    public int centerX,centerY;
    public OBSTACLE(Context context,int color, int posX, int posY, int heigth, int width,int imgID) {
        this.imgID=imgID;
        paint.setColor(color);
        position.set(posX,posY);
        this.heigth=heigth;
        this.width=width;
        this.context=context;
        this.bmap = BitmapFactory.decodeResource(context.getResources(), imgID);
        this.bmap = Bitmap.createScaledBitmap(bmap, width, heigth, true);
        centerX=posX+width/2;
        centerY=posY+heigth/2;
        bmap=BODY.getCircledBitmap(bmap);
    }

    public void draw(Canvas canvas){
        //canvas.drawRect(position.x,position.y,position.x+width,position.y+heigth,paint);
        canvas.drawBitmap(bmap,position.x,position.y,null);
    }

    public void setPosition(int x,int y){
        position.set(x,y);
        centerX=position.x+width/2;
        centerY=position.y+heigth/2;
    }

}
