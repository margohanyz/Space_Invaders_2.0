package com.example.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PictureDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

public class SpaceInvadersView extends SurfaceView implements Runnable{

    private Context context;
    private Thread gameThread = null;
    private boolean lock = false;
    private boolean lock2 = false;
    private SurfaceHolder ourHolder;
    private volatile boolean playing;
    private boolean paused = true;
    private Canvas canvas;
    private Paint paint;

    private long fps;
    private long timeThisFrame;
    private int screenX;
    private int screenY;

    private PlayerShip playerShip;
    private DrugiShip druga;
    private buttonreplay button;
    private buttongo button2;

    private Bullet bullet;
    private Bullet bullet2;

    private Bullet[] invadersBullets = new Bullet[200];
    private int nextBullet;
    private int maxInvaderBullets = 10;


    private Invader[] invaders = new Invader[12];
    private int numInvaders = 0;



    private SoundPool soundPool;
    private int playerExplodeID = -1;
    private int invaderExplodeID = -1;
    private int shootID = -1;
    private int uhID = -1;
    private int ohID = -1;

    private int score = 0;
    private int score2 = 0;

    private int lives = 3;
    private int lives2 = 3;

    private long menaceInterval = 1000;

    private boolean uhOrOh;

    private long lastMenaceTime = System.currentTimeMillis();
    private Bitmap bMap;

    public SpaceInvadersView(Context context, int x, int y) {


        super(context);
        bMap = BitmapFactory.decodeResource(context.getResources(), R.drawable.heart);

        this.context = context;


        ourHolder = getHolder();
        paint = new Paint();

        screenX = x;
        screenY = y;


        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC,0);

        try{

            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;


            descriptor = assetManager.openFd("shoot.ogg");
            shootID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("invaderexplode.ogg");
            invaderExplodeID = soundPool.load(descriptor, 0);



            descriptor = assetManager.openFd("playerexplode.ogg");
            playerExplodeID = soundPool.load(descriptor, 0);



            descriptor = assetManager.openFd("uh.ogg");
            uhID = soundPool.load(descriptor, 0);

            descriptor = assetManager.openFd("oh.ogg");
            ohID = soundPool.load(descriptor, 0);

        }catch(IOException e){
            // Print an error message to the console
            Log.e("error", "failed to load sound files");
        }

        prepareLevel();
    }

    private void prepareLevel(){

        score = 0;
        score2 = 0;
        lives = 3;
        lives2 = 3;
        lock=false;
        lock2=false;
        button = new buttonreplay(context, screenX, screenY);
        button2 = new buttongo(context, screenX, screenY);
        Log.e("Screen:" + screenY, "tutaj");
        playerShip = new PlayerShip(context, screenX, screenY);
        druga = new DrugiShip(context, screenX, screenY);
        bullet = new Bullet(screenY,0);
        bullet2 = new Bullet(screenY,1);


        for(int i = 0; i < invadersBullets.length; i++){
            invadersBullets[i] = new Bullet(screenY,2);
        }


        numInvaders = 0;                //TUTAJ!!!
        for(int column = 0; column < 6; column ++ ){
            for(int row = 0; row < 2; row ++ ){
                if(row==0){
                invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),false);}
                else{ invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),true);}
                numInvaders ++;
            }
        }



        menaceInterval = 1000;

    }

    @Override
    public void run() {
        while (playing) {

            long startFrameTime = System.currentTimeMillis();
            if(!paused){
                update();

            }               //Update
            draw();         //Rysuj klatkę

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }                                       //Obliczanie "prędkości" urządzenia
            if(!paused) {
                if ((startFrameTime - lastMenaceTime)> menaceInterval) {
                    if (uhOrOh)
                    { soundPool.play(uhID, 1, 1, 0, 0, 1);}
                     else
                    { soundPool.play(ohID, 1, 1, 0, 0, 1);}
                    lastMenaceTime = System.currentTimeMillis();
                    uhOrOh = !uhOrOh;
                }
            }
        }
    }

    private void update(){
        paint.setColor(Color.argb(255,  249, 129, 0));
        paint.setTextSize(100);

        boolean bumped = false;


        boolean lost = false;


        playerShip.update(fps);
        druga.update(fps);


        if(bullet.getStatus()){
            bullet.update(fps);
        }
        if(bullet2.getStatus()){
            bullet2.update(fps);
        }

        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()) {
                invadersBullets[i].update(fps);
            }
        }


        for(int i = 0; i < numInvaders; i++){
            if(invaders[i].getVisibility()) {


                invaders[i].update(fps);


                if(invaders[i].takeAim(playerShip.getX(), playerShip.getLength()) && invaders[i].change()==true){
                    if(invadersBullets[nextBullet].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), bullet.DOWN)) {
                        nextBullet++;

                        if (nextBullet == maxInvaderBullets) {
                            nextBullet = 0;
                        }
                    }
                }
                if(invaders[i].takeAim(druga.getX(), druga.getLength()) && invaders[i].change()==false){     if(invadersBullets[nextBullet].shoot(invaders[i].getX() + invaders[i].getLength() / 2, invaders[i].getY(), bullet.UP)) {
                    nextBullet++;

                    if (nextBullet == maxInvaderBullets) {
                        nextBullet = 0;
                    }
                }}


                if (invaders[i].getX() > screenX - invaders[i].getLength()
                        || invaders[i].getX() < 0){

                    bumped = true;
                }




            }

        }



        if(bumped){

            for(int i = 0; i < numInvaders; i++){
                invaders[i].dropDownAndReverse();

            }

            menaceInterval = menaceInterval - 30;


        }








        if(bullet.getImpactPointY() < 0 || bullet.getImpactPointY() > screenY){
            bullet.setInactive();
        }
        if(bullet2.getImpactPointY() < 0 || bullet2.getImpactPointY() > screenY){
            bullet2.setInactive();
        }


        for(int i = 0; i < invadersBullets.length; i++){

            if(invadersBullets[i].getImpactPointY() > screenY || invadersBullets[i].getImpactPointY() < 0 ){
                invadersBullets[i].setInactive();
            }
        }


        if(bullet.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet.setInactive();

                            score = score + 10;
                        boolean znajdz = false;

                        for (int x = 0; x < numInvaders; x++) {
                            if (invaders[x].getVisibility() == true) {
                                znajdz = false;
                                break;
                            }
                            if (invaders[x].getVisibility() == false) {
                                znajdz = true;
                            }
                        }
                        if(znajdz==true){
                            numInvaders = 0;                //TUTAJ!!!
                            for(int column = 0; column < 6; column ++ ){
                                for(int row = 0; row < 2; row ++ ){
                                    if(row==0){
                                        invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),false);}
                                    else{ invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),true);}
                                    numInvaders ++;
                                }
                            }


                        }
                        // Has the player won
                        if(score > 4000){
                            lock=true;
                            paused = true;

                          //  prepareLevel();
                        }
                    }
                }
            }
        }

        if(bullet2.getStatus()) {
            for (int i = 0; i < numInvaders; i++) {
                if (invaders[i].getVisibility()) {
                    if (RectF.intersects(bullet2.getRect(), invaders[i].getRect())) {
                        invaders[i].setInvisible();
                        soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                        bullet2.setInactive();

                            score2=score2 + 10;
                         boolean znajdz = false;

                        for (int x = 0; x < numInvaders; x++) {
                            if (invaders[x].getVisibility() == true) {
                                Log.d("znalazlem", "numer" + x + "tru");
                                znajdz = false;
                                break;
                            }
                            if (invaders[x].getVisibility() == false) {
                                Log.d("znalazlem", "numer" + x + "fals");
                                znajdz = true;
                            }
                        }
                        if(znajdz==true){
                            numInvaders = 0;                //TUTAJ!!!
                            for(int column = 0; column < 6; column ++ ){
                                for(int row = 0; row < 2; row ++ ){
                                    if(row==0){
                                        invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),false);}
                                    else{ invaders[numInvaders] = new Invader(context, row +4, column +1, screenX, (screenY),true);}
                                    numInvaders ++;
                                }
                            }


                        }


                        // Has the player won
                        if(score > 4000/*numInvaders * 10*/){
                            lock=true;
                            paused = true;

                        //    prepareLevel();
                        }
                        if(score2>4000){
                            lock2=true;
                            paused = true;


                        }
                    }
                }
            }
        }


        if(bullet.getStatus()) {
            {
                if (RectF.intersects(bullet.getRect(),  druga.getRect())) {

                    soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                    bullet.setInactive();
                        score = score + 50;
                        lives2 --;
                    if (score >4000) {
                        lock=true;
                        paused = true;
                        //prepareLevel();
                    }
                    if(lives2==0){

                        lock=true;
                        paused = true;

                        //prepareLevel();
                    }
                }
            }
        }

        if(bullet2.getStatus()) {
            {
                if (RectF.intersects(bullet2.getRect(),  playerShip.getRect())) {

                    soundPool.play(invaderExplodeID, 1, 1, 0, 0, 1);
                    bullet2.setInactive();

                    score2 = score2 + 50;
                    lives --;
                    if (lives==0) {
                        lock2=true;
                        paused = true;

                    //    prepareLevel();
                    }
                    if (score2 > 4000) {
                        lock2=true;
                        paused = true;

                       // prepareLevel();
                    }
                }
            }
        }


        // Has an invader bullet hit the player ship
        for(int i = 0; i < invadersBullets.length; i++){
            if(invadersBullets[i].getStatus()){
                if(RectF.intersects(playerShip.getRect(), invadersBullets[i].getRect())){
                    invadersBullets[i].setInactive();
                    lives --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);

                    // Is it game over?
                    if(lives == 0){
                        paused = true;
                        lock2=true;

                      //  prepareLevel();

                    }
                }
                if(RectF.intersects(druga.getRect(), invadersBullets[i].getRect())){
                    invadersBullets[i].setInactive();
                    lives2 --;
                    soundPool.play(playerExplodeID, 1, 1, 0, 0, 1);

                    // Is it game over?
                    if(lives2 == 0){


                        paused = true;
                        lock=true;
                        //prepareLevel();

                    }
                }
            }
        }

        if(druga.getX()<screenX/30 || druga.getX()> screenX-screenX/10){
            druga.setMovementState(druga.STOPPED);}

        if(playerShip.getX()<screenX/30 || playerShip.getX()> screenX-screenX/10){
            playerShip.setMovementState(playerShip.STOPPED);}


    }



    private void draw(){
        // Make sure our drawing surface is valid or we crash
        if (ourHolder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            canvas = ourHolder.lockCanvas();

            // Draw the background color
            canvas.drawColor(Color.argb(255, 0, 0, 0));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  255, 255, 255));

            // Now draw the player spaceship
            canvas.drawBitmap(playerShip.getBitmap(), playerShip.getX(), screenY - screenY/20, paint);
            canvas.drawBitmap(druga.getBitmap(), druga.getX(), -screenY/20, paint);


            // Draw the invaders
            for(int i = 0; i < numInvaders; i++){
                if(invaders[i].getVisibility()) {
                    if(uhOrOh) {
                        canvas.drawBitmap(invaders[i].getBitmap(), invaders[i].getX(), invaders[i].getY(), paint);
                    }else{
                        canvas.drawBitmap(invaders[i].getBitmap2(), invaders[i].getX(), invaders[i].getY(), paint);
                    }
                }
            }





            if(bullet.getStatus()){
                canvas.drawRect(bullet.getRect(), paint);
            }
            if(bullet2.getStatus()){
                canvas.drawRect(bullet2.getRect(), paint);
            }

            for(int i = 0; i < invadersBullets.length; i++){
                if(invadersBullets[i].getStatus()) {
                    canvas.drawRect(invadersBullets[i].getRect(), paint);
                }
            }





            if(lock==true || lock2==true){
            canvas.drawBitmap(buttonreplay.getBitmap(), screenX/4,(screenY/4)-screenY/20, paint);
            canvas.drawBitmap(buttongo.getBitmap(), (screenX/2)+(screenY/4),(screenY/4)-screenY/20, paint);}

            if(lock==true){   paint.setColor(Color.argb(255,  255, 255, 255));
                paint.setTextSize((screenY/15) + 1);
                canvas.drawText("Wygrał gracz pierwszy", screenX/5 + screenX/8,screenY/2, paint);
            }
            if(lock2==true){   paint.setColor(Color.argb(255,  255, 255, 255));
                paint.setTextSize((screenY/15) + 1);
                canvas.drawText("Wygrał gracz drugi", screenX/5 + screenX/8,screenY/2, paint);
            }


            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(screenY/15);


            if(lock==true){ canvas.drawText("Wygrał gracz pierwszy", screenX/5 + screenX/8,screenY/2, paint);}
            if(lock2==true){ canvas.drawText("Wygrał gracz drugi", screenX/5 + screenX/8,screenY/2, paint);}



            paint.setColor(Color.argb(255,  249, 129, 0));
            paint.setTextSize(80);
            canvas.drawText("" + score , 15,screenY-10, paint);
            for(int i=0; i<lives; i++){
            canvas.drawBitmap(bMap, screenX - screenX/18 - (i*100), screenY - screenY/10, paint);}
           // canvas.save();
            canvas.scale(-1, -1);
            canvas.drawText("" + score2, -screenX + 15,-30, paint);
            for(int i=0; i<lives2; i++){
                canvas.drawBitmap(bMap, -(screenX/18 + (i*100)), -(screenY/10), paint);}
           // canvas.restore();

            canvas.scale(-1, -1);

            // Draw everything to the screen
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

        // If SpaceInvadersActivity is pausedstopped
    // shutdown our thread.
    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            Log.e("Error:", "joining thread");
        }

    }


    public void resume() {
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        if(paused==true && (lock==true || lock2==true)){
            switch (motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {

                case MotionEvent.ACTION_DOWN:
                        if(motionEvent.getX()>screenX/2){
                            paused=false;

                            if(lock==true){ Game.zakoncz(context,score); }
                            if(lock2==true){ Game.zakoncz(context, score2); }
                            lock=false;
                            lock2=false;
                            //return true;

                    }
                        if(motionEvent.getX()<=screenX/2){
                            paused=false;
                            lock=false;
                            lock2=false;
                            prepareLevel();
                            return true;
                        }
            }

        }

        switch (motionEvent.getActionMasked() & MotionEvent.ACTION_MASK) {


            case MotionEvent.ACTION_DOWN:

                paused = false;

                if(motionEvent.getY() > screenY - (screenY / 8) && (motionEvent.getX() > playerShip.getX() + 150 || motionEvent.getX() < playerShip.getX() - 150)) {
                    if (motionEvent.getX() > playerShip.getX() && playerShip.getX() < screenX) {
                        playerShip.setMovementState(playerShip.RIGHT);

                    } else {
                        playerShip.setMovementState(playerShip.LEFT);

                    }

                }
                if(motionEvent.getY() < screenY - 7*(screenY / 8)&& (motionEvent.getX() > druga.getX() + 150 || motionEvent.getX() < druga.getX() - 150)) {
                    if (motionEvent.getX() > druga.getX()){
                    druga.setMovementState(druga.RIGHT);}
                    else { druga.setMovementState(druga.LEFT);
                    }
                }

                if(motionEvent.getY() > screenY - screenY / 8)
                {}


                if(motionEvent.getY() > screenY - screenY / 8 && motionEvent.getX() < playerShip.getX() + 150 && motionEvent.getX() > playerShip.getX() - 150) {
                    // Shots fired
                    if(bullet.shoot(playerShip.getX()+ playerShip.getLength()/2,screenY,bullet.UP)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }

                if(motionEvent.getY()< screenY - 7*(screenY / 8) && motionEvent.getX() < druga.getX() + 150 && motionEvent.getX() > druga.getX() - 150) {
                    // Shots fired
                    if(bullet2.shoot(druga.getX()+ druga.getLength()/2,0,bullet2.DOWN)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }

                break;


            case MotionEvent.ACTION_POINTER_DOWN:
                int index = (motionEvent.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;


                if(motionEvent.getY(index) > screenY - (screenY / 8) && (motionEvent.getX(index) > playerShip.getX() + 200 || motionEvent.getX(index) < playerShip.getX() - 200)) {
                    if (motionEvent.getX(index) > playerShip.getX() && playerShip.getX() < screenX) {
                        playerShip.setMovementState(playerShip.RIGHT);

                    } else {
                        playerShip.setMovementState(playerShip.LEFT);

                    }

                }
                if(motionEvent.getY(index) < screenY - 7*(screenY / 8)&& (motionEvent.getX(index) > druga.getX() + 200 || motionEvent.getX(index) < druga.getX() - 200)) {
                    if (motionEvent.getX(index) > druga.getX()){
                        druga.setMovementState(druga.RIGHT);}
                    else { druga.setMovementState(druga.LEFT);
                    }
                }

                if(motionEvent.getY(index) > screenY - screenY / 8)
                {}


                if(motionEvent.getY(index) > screenY - screenY / 8 && motionEvent.getX(index) < playerShip.getX() + 200 && motionEvent.getX(index) > playerShip.getX() - 200) {
                    // Shots fired
                    if(bullet.shoot(playerShip.getX()+ playerShip.getLength()/2,screenY,bullet.UP)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }

                if(motionEvent.getY(index)< screenY - 7*(screenY / 8) && motionEvent.getX(index) < druga.getX() + 200 && motionEvent.getX(index) > druga.getX() - 200) {
                    // Shots fired
                    if(bullet2.shoot(druga.getX()+ druga.getLength()/2,0,bullet2.DOWN)){
                        soundPool.play(shootID, 1, 1, 0, 0, 1);
                    }
                }

                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:

                //if(motionEvent.getY() > screenY - screenY / 10) {
                    if(motionEvent.getY()>screenY/2){
                    //    Log.d("lodka", "miejsce" + motionEvent.getY());
                    playerShip.setMovementState(playerShip.STOPPED);}
                if(motionEvent.getY()<=screenY/2){
                    druga.setMovementState(playerShip.STOPPED);}
                //}
                break;
            case MotionEvent.ACTION_POINTER_UP:
                if(motionEvent.getY()>screenY/2){
                  //  Log.d("lodka", "miejsce" + motionEvent.getY());
                    playerShip.setMovementState(playerShip.STOPPED);}
                if(motionEvent.getY()<=screenY/2){
                    druga.setMovementState(playerShip.STOPPED);}
                //}
                break;
        }
        return true;
    }
}