package com.example.spaceinvaders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

public class Game extends AppCompatActivity {
    SpaceInvadersView spaceInvadersView;
    private static Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getApplicationContext();
        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();
        // Load the resolution into a Point object
        Point size = new Point();
        display.getSize(size);

        // Initialize gameView and set it as the view
        spaceInvadersView = new SpaceInvadersView(this, size.x, size.y);


     //   setContentView(R.layout.activity_game);
        setContentView(spaceInvadersView);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
    @Override
    protected void onResume() {
        super.onResume();

        // Tell the gameView resume method to execute
        spaceInvadersView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();


        // Tell the gameView pause method to execute
        spaceInvadersView.pause();
    }

    protected static void zakoncz(Context mContext, int score){

        //finish();
        Log.d("Zakoncz", String.valueOf(score));
        Intent intent = new Intent(mContext, GameOver.class);
        int scoret = score;
        intent.putExtra("jeden",scoret);
        mContext.startActivity(intent);
    }

}
