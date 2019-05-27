package com.example.spaceinvaders;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Play extends AppCompatActivity implements View.OnClickListener {

    TextView myGameName;
    Typeface myFont;
    TextView B1;
    TextView B2;
    TextView B3;
    Button play;
    Button leaderboard;
    Button signout;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        // Initialize the firebase auth;
        mAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        String email = currentUser.getEmail();
//        if(email == "guest@wp.pl"){
//        signout.setText("Back");
//        }

        // Set button&text
        play = (Button) findViewById(R.id.b_play);
        leaderboard = (Button) findViewById(R.id.b_leaderboard);
        signout = (Button) findViewById(R.id.b_signout);
        myGameName = (TextView) findViewById(R.id.GameName);
        B1 = (TextView) findViewById(R.id.b_play);
        B2 = (TextView) findViewById(R.id.b_leaderboard);
        B3 = (TextView) findViewById(R.id.b_signout);

        // Set fonts
        myFont = Typeface.createFromAsset(this.getAssets(), "Fonts/ca.ttf");
        myGameName.setTypeface(myFont);
        B1.setTypeface(myFont);
        B2.setTypeface(myFont);
        B3.setTypeface(myFont);

        // Actions
        play.setOnClickListener(this);
        leaderboard.setOnClickListener(this);
        signout.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void playGame(){
        // MICHAŁ TUTAJ TWOJA GRA

        /////////////////////////
        finish();
        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
        //Intent intent = new Intent(this, GameOver.class);
        //startActivity(intent);
    }

    private void showLeaderboard(){
        finish();
        Intent intent = new Intent(this, Leaderboard.class);
        startActivity(intent);
    }

    @Override
    public  boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        if(id == android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if(v == play)
        {
            playGame();
        }

        if(v == leaderboard)
        {
            showLeaderboard();
        }

        if(v == signout)
        {
            mAuth.signOut();
            finish();
            Intent intent = new Intent(Play.this, MainActivity.class);
            startActivity(intent);
        }
    }

}