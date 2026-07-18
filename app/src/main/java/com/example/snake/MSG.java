package com.example.snake;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MSG {
    public String s;
    public float posX,posY;
    public Paint paint=new Paint();
    public String basictext;
    public int value=0;
    public MSG(String basictext, float posX, float posY,int color,int size) {
        this.basictext = basictext;
        s=basictext;
        this.posX = posX;
        this.posY = posY;
        paint.setColor(color);
        paint.setTextSize(size);
    }

    public void draw(Canvas canvas){
        canvas.drawText(s,posX,posY,paint);
    }

    public void setValue(int v){
        value=v;
        s=""+basictext+Integer.toString(value);
    }
    public void setPos(int x,int y){
        posX=x;
        posY=y;
    }

    public void setTextSize(int size){
        paint.setTextSize(size);
    }
}
