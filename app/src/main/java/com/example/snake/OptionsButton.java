package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;

public class OptionsButton extends BUTTON {
    public Bitmap bmap;
    public Bitmap backbmap;
    public Bitmap actualbmap;
    public boolean active=false;
    public OptionsButton(int height, int width, int centerx, int centery, int buttcolor, int textcolor, String text, int imgID,int backbuttID, Context context) {
        super(height, width, centerx, centery, buttcolor, textcolor, text);
        this.bmap = BitmapFactory.decodeResource(context.getResources(), imgID);
        this.bmap = Bitmap.createScaledBitmap(bmap, width, height, true);
        bmap= BODY.getCircledBitmap(bmap);
        actualbmap=bmap;
        this.backbmap = BitmapFactory.decodeResource(context.getResources(), backbuttID);
        this.backbmap = Bitmap.createScaledBitmap(backbmap, width, height, true);
        backbmap= BODY.getCircledBitmap(backbmap);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawBitmap(actualbmap,left,top,null);
    }

    @Override
    public boolean onClick(MotionEvent event) {             //a map-ben van meghivva
        int x=(int)event.getX();
        int y=(int)event.getY();
        if(left<=x && right>=x && top<=y && bot>=y && !active){
            actualbmap=backbmap;
            active=true;
            return true;
        }
        else if(left<=x && right>=x && top<=y && bot>=y && active){
            actualbmap=bmap;
            active=false;
            return true;
        }
        return false;
    }
}
