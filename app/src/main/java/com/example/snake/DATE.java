package com.example.snake;

import android.graphics.Point;

public class DATE {
    public int x,y,angle;

    public DATE(int posX, int posY, int angle) {
        this.x = posX;
        this.y = posY;
        this.angle = angle;
    }

    public void set(Point p){
        this.x=p.x;
        this.y=p.y;
    }

    public String toString(){
        String s= Integer.toString(x)+" "+Integer.toString(y)+" "+Integer.toString(angle);
        return s;
    }
}
