package com.example.snake;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

public class MyGyroscope implements SensorEventListener {
    public double velocityX,velocityY;
    public double accelerationX,accelerationY;
    public SensorManager sensorManager;
    public Context context;
    public boolean newCalibrate=false;
    public int screenSizeX,screenSizeY;
    public BUTTON CalibrateButton;
    public String calibrateText;
    public Bitmap calibratButt;
    public int posX,posY;
    public int centerX,centerY;
    public int width;
    public MyGyroscope(Context context,String calibrateText,int calibratImgID,int height,int width,int posX,int posY) {
        this.posX=posX;
        this.posY=posY;
        this.width=width;
        this.context = context;
        this.calibrateText=calibrateText;
        sensorManager=(SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSizeY = metrics.heightPixels;
        screenSizeX = metrics.widthPixels;
        CalibrateButton=new BUTTON(200,200,150,150, Color.BLACK,Color.BLACK,"");
        CalibrateButton.setTextCorrigation(400,500);
        CalibrateButton.setTextsize(150);
        this.calibratButt = BitmapFactory.decodeResource(context.getResources(), calibratImgID);
        this.calibratButt = Bitmap.createScaledBitmap(calibratButt, width, height, true);
        calibratButt= BODY.getCircledBitmap(calibratButt);
        centerX=posX+width/2;
        centerY=posY+height/2;
    }

    public boolean NewCalibrate(MotionEvent event){
        double d= SNAKE.distance(event.getX(),event.getY(),centerX,centerY);
        /*
        if(CalibrateButton.onClick(event) || d<width){
            CalibrateButton.setText(calibrateText);
            newCalibrate=true;
            return true;
        }

         */
        if(d<width){
            CalibrateButton.setText(calibrateText);
            newCalibrate=true;
            return true;
        }
        return false;

    }

    public boolean setCalivrate(MotionEvent event){
        if(newCalibrate && event.getAction()==MotionEvent.ACTION_DOWN){
            velocityX=0;                //a szogsebesseget nullara allitja, ezzel biztositja
            velocityY=0;                //mert ezzel biztositja hogy vizszintes lesz a null szog
            CalibrateButton.setText("");
            newCalibrate=false;

            return true;            //sikeres volt a kalibralas
        }
        return  false;          // nem tortent meg a kalibralas
    }

    public void drawCalibrateButton(Canvas canvas) {
            //CalibrateButton.draw(canvas);
            canvas.drawBitmap(calibratButt,posX,posY,null);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = (sensorEvent.values[0]);
        double y = (sensorEvent.values[1]);
        //Z = (sensorEvent.values[2]);
        if (!newCalibrate) {
            accelerationX = Math.toDegrees(x);
            /*
            if(accelerationX<accMin){
                accelerationX=accMin;
            }
            if(accelerationX>accMax){
                accelerationX=accMax;
            }

             */

            accelerationY = Math.toDegrees(y);
            velocityX += accelerationX;
            velocityY += accelerationY;
            //Z=Math.toDegrees(Z);
        }
        else{
            accelerationX =0;
            accelerationY =0;
            velocityX=0;
            velocityY=0;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
