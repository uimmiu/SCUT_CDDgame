package com.example.scut_cddgame;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class Menu extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{
    SurfaceHolder surfaceHolder;
    Canvas canvas;
    boolean threadFlag=true;
    Bitmap background;
    Context context;
    private int x=270,y=50;
    private Bitmap[] menuItems=new Bitmap[5];

    public Menu(Context context){
        super(context);
        this.context=context;
        surfaceHolder=getHolder();
        // TODO: 19-5-25 目录界面
        //background= BitmapFactory.decodeResource(context.getResources())

        this.getHolder().addCallback(this);
        this.setOnTouchListener(this);
    }

    Thread menuThread=new Thread(){
        @SuppressLint("WrongCall")
        @Override
        public void run(){
            while (threadFlag){
                try{
                    canvas=surfaceHolder.lockCanvas();
                    synchronized (this){
                        onDraw(canvas);
                    }
                }finally {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    };

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas){
        Rect src=new Rect(),des=new Rect();
        src.set(0,0,background.getWidth(),background.getHeight());
        des.set(0,0,MainActivity.SCREEN_WIDTH,MainActivity.SCREEN_HEIGHT);
        Paint paint=new Paint();
        canvas.drawBitmap(background,src,des,paint);
        for (int i=0;i<menuItems.length;i++){
            canvas.drawBitmap(menuItems[i],(int)(x*MainActivity.SCALE_HORIAONTAL+200),(int)((i*43+y)*MainActivity.SCALE_VERTICAL+150),paint);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        threadFlag=true;
        menuThread.start();
        System.out.println("surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        //nothing
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        threadFlag=false;
        boolean retry=true;
        while (retry){
            try{
                menuThread.join();
                retry=false;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        int ex=(int)motionEvent.getX(),ey=(int)motionEvent.getY(),selectIndex=-1;
        for (int i=0;i<menuItems.length;i++){
            // TODO: 19-5-25
        }
        return false; //临时写的
    }
}
