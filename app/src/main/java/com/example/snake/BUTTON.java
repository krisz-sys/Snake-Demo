package com.example.snake;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

public class BUTTON {
    public int height,width,centerx,centery;
    public int top,left,bot,right;
    public Paint buttcolor=new Paint();
    public Paint textcolor=new Paint();
    public String text;
    private int textCorrigationX=-55,textCorrigationY=15;

    public BUTTON(int height, int width, int centerx, int centery, int buttcolor, int textcolor, String text) {
        this.height = height;
        this.width = width;
        this.centerx = centerx;
        this.centery = centery;
        this.buttcolor.setColor(buttcolor);
        this.textcolor.setColor(textcolor);
        this.textcolor.setTextSize(50);
        this.text=text;
        left=centerx-width/2;
        top=centery-height/2;
        right=centerx+width/2;
        bot=centery+height/2;
    }

    public void setTextCorrigation(int textCorrigationX,int textCorrigationY) {
        this.textCorrigationX = textCorrigationX;
        this.textCorrigationY = textCorrigationY;
    }


    public void setTextsize(int size){
        textcolor.setTextSize(size);
    }

    public void draw(Canvas canvas){
        canvas.drawRect(left,top,right,bot,buttcolor);
        canvas.drawText(text,centerx+textCorrigationX,centery+textCorrigationY,textcolor);
    }

    public boolean onClick(MotionEvent event){          //1-et térit vissza ha rákattintassz nullát ha nem
        int x=(int)event.getX();
        int y=(int)event.getY();
        if(left<=x && right>=x && top<=y && bot>=y){
            //System.out.println("gg");
            return true;
        }
        return false;
    }

    public void setButtColor(int buttcolor){
        if(buttcolor!=this.buttcolor.getColor()) {
            this.buttcolor.setColor(buttcolor);
        }
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
    public void setTextColor(int textColor){
        textcolor.setColor(textColor);
        textcolor.setTextSize(50);
    }

}