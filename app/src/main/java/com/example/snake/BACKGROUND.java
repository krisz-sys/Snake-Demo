package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;

public class BACKGROUND {
    public Paint backgroundcolor=new Paint();
    public int backgimgID;
    public int borderImage;
    public Point borderUpLeft=new Point();
    public Point borderDownRight=new Point();
    public Context context;
    public final Point screenSize=new Point();
    public Bitmap backgorundBm;
    public Bitmap[] borderBm;
    public BACKGROUND(Context context, int borderLeft, int borderUp, int borderRight, int borderDown,int color,int backgimgID,int[] borderImage){
        this.backgimgID=backgimgID;
        borderUpLeft.x=borderLeft;
        borderUpLeft.y=borderUp;
        borderDownRight.x=borderRight;
        borderDownRight.y=borderDown;
        //borderDownRight.x=borderDown;
        //borderDownRight.y=borderUp;
        this.context=context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSize.y = metrics.heightPixels;
        screenSize.x = metrics.widthPixels;
        this.backgroundcolor.setColor(color);
        //this.borderImage=borderImage;
        this.borderBm=new Bitmap[4];
        this.backgorundBm = BitmapFactory.decodeResource(context.getResources(), backgimgID);
        this.backgorundBm = Bitmap.createScaledBitmap(backgorundBm, screenSize.x, screenSize.y ,true);
        this.borderBm[0] = BitmapFactory.decodeResource(context.getResources(), borderImage[0]);            //horizontal
        this.borderBm[0] = Bitmap.createScaledBitmap(borderBm[0], screenSize.x, borderUpLeft.y ,true);
        this.borderBm[1] = BitmapFactory.decodeResource(context.getResources(), borderImage[1]);            //vertical
        this.borderBm[1] = Bitmap.createScaledBitmap(borderBm[1], borderUpLeft.x, screenSize.y ,true);
        this.borderBm[2] = BitmapFactory.decodeResource(context.getResources(), borderImage[0]);            //horizontal
        this.borderBm[2] = Bitmap.createScaledBitmap(borderBm[0], screenSize.x, screenSize.y-borderDownRight.y ,true);
        this.borderBm[3] = BitmapFactory.decodeResource(context.getResources(), borderImage[1]);            //vertical
        this.borderBm[3] = Bitmap.createScaledBitmap(borderBm[1], screenSize.x-borderDownRight.x, screenSize.y ,true);
    }

    public void draw(Canvas canvas){
        //canvas.drawColor(Color.YELLOW);
        canvas.drawBitmap(backgorundBm,0,0,null);
        canvas.drawBitmap(borderBm[0],0,0,null);
        canvas.drawBitmap(borderBm[1],0,0,null);
        canvas.drawBitmap(borderBm[2],0,borderDownRight.y,null);
        canvas.drawBitmap(borderBm[3],borderDownRight.x,0,null);
    }

}
