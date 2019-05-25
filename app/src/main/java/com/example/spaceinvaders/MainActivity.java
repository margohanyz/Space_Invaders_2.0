package com.example.spaceinvaders;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    TextView myView;
    Typeface myFont;
    TextView B1;
    TextView B2;
    TextView B3;
    private Button login;
    private Button register;
    private Button guest;

    private FirebaseAuth mAuth;

    private static final String TAG = "MainActivity";

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize buttons&text
        login = (Button) findViewById(R.id.bsignin);
        register = (Button) findViewById(R.id.bsignup);
        guest = (Button) findViewById(R.id.bguestmode);
        B1 = (TextView) findViewById(R.id.bsignin);
        B2 = (TextView) findViewById(R.id.bsignup);
        B3 = (TextView) findViewById(R.id.bguestmode);
        myView = (TextView) findViewById(R.id.GameName);

        // Set fonts
        myFont = Typeface.createFromAsset(this.getAssets(), "Fonts/ca.ttf");
        myView.setTypeface(myFont);
        B1.setTypeface(myFont);
        B2.setTypeface(myFont);
        B3.setTypeface(myFont);

        // Set actions
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGuest();
            }
        });

        // Ad
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    public void openLogin()
    {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void openRegister()
    {
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }

    public void openGuest() {
        // Signing in as a guest
        mAuth.signInWithEmailAndPassword("guest@wp.pl", "guestguest")
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // go to Play panel
                            Toast.makeText(MainActivity.this, "Guest...",Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), Play.class));
                        } else {
                            Toast.makeText(MainActivity.this, "Something went wrong. Please try again.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
