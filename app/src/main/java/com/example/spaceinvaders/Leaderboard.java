package com.example.spaceinvaders;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    Typeface myFont;
    TextView B1;
    TextView myText;
    Button comeback;
    ListView myListView;

    FirebaseDatabase database;
    DatabaseReference myRef;

    ArrayList<String> users = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        // Write a message to the database
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        // Set widgets
        comeback = (Button) findViewById(R.id.b_comeback);
        myText = (TextView) findViewById(R.id.TextLeaderboard);
        B1 = (TextView) findViewById(R.id.b_comeback);
        myListView = (ListView) findViewById(R.id.leaderboard);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        // Set fonts
        myFont = Typeface.createFromAsset(this.getAssets(), "Fonts/ca.ttf");
        myText.setTypeface(myFont);
        B1.setTypeface(myFont);

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comeBack();
            }
        });
    }

    public void comeBack()
    {
        finish();
        Intent intent = new Intent(this, Play.class);
        startActivity(intent);
    }
}