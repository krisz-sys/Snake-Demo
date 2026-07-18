package com.example.snake;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import android.view.MotionEvent;

import java.util.ArrayList;

public class SNAKE {
    public Context context;
    public int screenSizeX,screenSizeY;
    public int BodyImageID;
    public int HeadImageID;
    //public int LifePoints;
    //public int timesDamaged = 0;
   // public int NumberOfLifes;         //if LifePoints > "bizonyos pontszam" {NumberOfLifes++;}  - majd a foprogramban
    private final int BodyPartHeigth=70,BodyPartWidth=70;
    public MyGyroscope myGyroscope;
    //private final int Limit=50;
    ArrayList<BODY> BodyParts=new ArrayList<>();
    public ArrayList<DATE> path=new ArrayList<>();           // a nulladik elem szamit az utolso elenek
    public int maxPathindex=70;
    private int minDistbetweenBudyP=100;
    public int borderLeft=0,borderRight=0,borderUp=0,borderDown=0;
    public boolean movingEN=true;
    public int speed=10;
    public int manualspeed=20;
    public boolean controlMode=false;
    public float movingDestX,movingDestY;
    public double moveX=0,moveY=0;
    public Paint paint=new Paint();
    public BACKGROUND background;
    public int maxlength=1;
    public int lifepoints=5;
    public SNAKE(int defaultposX, int defaultposY, int color, Context context,int BodyImageID,int HeadImageID) {     //teljesen új kígyó létrehozása
        this.BodyImageID=BodyImageID;
        this.HeadImageID=HeadImageID;
        this.context=context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSizeY = metrics.heightPixels;
        screenSizeX = metrics.widthPixels;
        //this.NumberOfLifes=3;
        BodyParts=new ArrayList<>();
        BodyParts.add(new BODY(HeadImageID,defaultposX,defaultposY,BodyPartWidth,BodyPartHeigth,context));
        path=new ArrayList<>();
        path.add(new DATE(defaultposX,defaultposY,0));

        paint.setColor(Color.RED);
        paint.setTextSize(70);
    }
    public void setBackground(BACKGROUND background) {
        this.background = background;
    }

    public SNAKE(ArrayList<DATE> path, Context context, int BodyImageID, int HeadImageID){                 // mentett kígyó betöltése
        this.BodyImageID=BodyImageID;
        this.HeadImageID=HeadImageID;
        this.path=path;
        this.context=context;
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        screenSizeY = metrics.heightPixels;
        screenSizeX = metrics.widthPixels;

        BodyParts=new ArrayList<>();
        BodyParts.add(new BODY(HeadImageID,path.get(path.size()-1).x,path.get(path.size()-1).y,BodyPartWidth,BodyPartHeigth,context));
        paint.setColor(Color.RED);
        paint.setTextSize(70);
    }

    public void setPosition(double velocityX,double velocityY){
        move();
        BodyParts.get(0).optdistIndex=BodyParts.size();
        BodyParts.get(0).setposX(BodyParts.get(0).getposX()+velocityX);
        BodyParts.get(0).setposY(BodyParts.get(0).getposY()-velocityY);
        //BodyParts.get(0).addPathPoint(BodyParts.get(0).getposX(),BodyParts.get(0).getposY());
       // System.out.println(BodyParts.get(0).positionx+"   "+BodyParts.get(0).positiony);
        addPathPoint((int)BodyParts.get(0).positionx,(int)BodyParts.get(0).positiony,velocityX,velocityY);
        if(BodyParts.get(0).getposX()>=borderRight-BodyParts.get(0).SnakeSize.x){
            BodyParts.get(0).setposX(borderRight-BodyParts.get(0).SnakeSize.x);
        }
         if(BodyParts.get(0).getposX()<=borderLeft){
            BodyParts.get(0).setposX(borderLeft);
        }
         if(BodyParts.get(0).getposY()>=borderDown-BodyParts.get(0).SnakeSize.y){
            BodyParts.get(0).setposY(borderDown-BodyParts.get(0).SnakeSize.y);
        }
         if(BodyParts.get(0).getposY()<=borderUp){
            BodyParts.get(0).setposY(borderUp);
        }

    }


    public void drawSnake(Canvas canvas) {
        //System.out.println("movingEN: "+movingEN);
        //drawPath(canvas);
        for (int i = BodyParts.size()-1; i >=0; --i) {
            BodyParts.get(i).draw(canvas);
            //canvas.drawText(Integer.toString(i),(int)BodyParts.get(i).centerX,(int)BodyParts.get(i).centerY,paint);
        }

        if(movingEN && !controlMode) {
            setPosition(myGyroscope.velocityX/speed, myGyroscope.velocityY/speed);
        }
        else if(movingEN && controlMode){
            setPosition(moveX,moveY);
            //System.out.println(moveX+"      "+moveY);
        }
        //localPathindex= (int) distance((int) myGyroscope.velocityX,(int) myGyroscope.velocityY,0,0)*15/10;

/*
        //System.out.println("angle: "+BodyParts.get(0).Angle);
        if(!state){
            if(BodyParts.get(0).positionx>1200) {
                setPosition(-4, 12);
            }
            else if(BodyParts.get(0).positionx>800){
                setPosition(-10, -5);
            }
            else if(BodyParts.get(0).positionx>300){
                setPosition(-15, 7);
            }
            else if(BodyParts.get(0).positionx>120){
                setPosition(-4, -7);
            }
            else{
                state=true;
            }
        }
        else{
            if(BodyParts.get(0).positionx<120) {
                setPosition(4, 12);
            }
            else if(BodyParts.get(0).positionx<300){
                setPosition(10, -5);
            }
            else if(BodyParts.get(0).positionx<800){
                setPosition(15, 7);
            }
            else if(BodyParts.get(0).positionx<1200){
                setPosition(5, -10);
            }
            else{
                state=false;
            }
        }
 */
    }

    public void setSpeed(int speed){
        this.speed=speed;
        if(speed==30){
            manualspeed=4;
        }
        else if(speed==20){
            manualspeed=8;
        }
        else if(speed==10){
            manualspeed=15;
        }
        else if(speed==4){
            manualspeed=25;
        }

    }

    public void goTo(float destX,float destY) {
        if(destX>background.borderDownRight.x){
            return;
        }
        movingDestX = destX;
        movingDestY=  destY;
        double posX=BodyParts.get(0).positionx;
        double posY=BodyParts.get(0).positiony;
        double alfa= (double) Math.atan((movingDestY-posY)/(movingDestX-posX));
        alfa=-alfa;
        if(movingDestX-posX<0){
            alfa-=Math.PI;
        }
        moveX=  (manualspeed*Math.cos(alfa));
        moveY=  (manualspeed*Math.sin(alfa));
        /*
                if(posX>movingDestX){
            moveX*=-1;
            moveY*=-1;
        }
         */
    }

    public void ControlWithGyroscope(MyGyroscope myG){
        myGyroscope=myG;
    }


    public void move(){

        BodyParts.get(0).optdistIndex=path.size()-1;
        for(int i=BodyParts.size()-1;i>0;--i){
            BodyParts.get(i).calculatDistanceIndex(path,(int)BodyParts.get(i-1).positionx,(int)BodyParts.get(i-1).positiony,BodyParts.get(i-1).optdistIndex);
        }
    }

    public void onClick(MotionEvent event){
        int x=(int)event.getX();
        int y=(int)event.getY();
        goTo(x,y);
    }

    public void grow(){
        maxPathindex+=5;
        //ha a kigyo no egyet akkor a BodyParts tömböt megnöveli, lefoglalja a szökséges adatterületet
        BODY newbody;
        newbody = new BODY(this.BodyImageID, (int)BodyParts.get(BodyParts.size() - 1).getposX(), (int)BodyParts.get(BodyParts.size() - 1).getposY(), BodyParts.get(0).SnakeSize.x, BodyParts.get(0).SnakeSize.y, context);
        BodyParts.add(newbody);
        if(BodyParts.size()>maxlength){
            maxlength=BodyParts.size();
        }
    }

    public void grow2(int x,int y){
        BODY newbody;
        newbody = new BODY(this.BodyImageID, x, y, BodyParts.get(0).SnakeSize.x, BodyParts.get(0).SnakeSize.y, context);
        BodyParts.add(newbody);
    }



    public static double distance(float x1,float y1,float x2,float y2){
        return  Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
    }


    public void addPathPoint(int x,int y,double velocityX,double velocityY) {
        //System.out.println("x, y"+ x+ "  "+y+",  "+path.get(path.size()-1).x+"   "+path.get(path.size()-1).y);
        if (!(x == path.get(path.size()-1).x && y == path.get(path.size()-1).y)) {
            //Point p = new Point();
            //p.set(x, y);

            int ang=BodyParts.get(0).calculateRotation(velocityX,velocityY);
            path.add(new DATE(x,y,ang));
            if (path.size() == maxPathindex + 1) {          // ha az ut megegyezik a felso hatarral akkor csak az utolso eleemt torli
                path.remove(0);
            }
        }
    }

    public void removeLastPathElements(int n){
        for(int i=0;i<n;++i){
            path.remove(0);
        }
        maxPathindex-=n;
    }

    public   void drawPath(Canvas canvas){
        Paint paint=new Paint();
        paint.setColor(Color.RED);
        for(int i=0;i<path.size();++i){        // a nulladik az utolso elem
            //System.out.print(path.get(i).x+" "+path.get(i).y+"    ");
            canvas.drawCircle(path.get(i).x,path.get(i).y,4,paint);
        }
        //System.out.println();
    }

    public void setBorders(int borderLeft,int borderUp,int borderRight,int borderDown){
        this.borderLeft=borderLeft;
        this.borderRight=borderRight;
        this.borderUp=borderUp;
        this.borderDown=borderDown;
    }

}
