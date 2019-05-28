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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends AppCompatActivity {

    Typeface myFont;
    TextView B1;
    TextView myText;
    Button comeback;
    ListView myListView;
    TextView textNick;
    TextView textScore;

    private DatabaseReference myRef;

    List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        myRef = FirebaseDatabase.getInstance().getReference();

        // Set widgets
        comeback = (Button) findViewById(R.id.b_comeback);
        myText = (TextView) findViewById(R.id.TextLeaderboard);
        B1 = (TextView) findViewById(R.id.b_comeback);
        myListView = (ListView) findViewById(R.id.leaderboardList);
        textNick = (TextView) findViewById(R.id.textNick);
        textScore = (TextView) findViewById(R.id.textScore);

        userList = new ArrayList<>();

        // Set fonts
        myFont = Typeface.createFromAsset(this.getAssets(), "Fonts/ca.ttf");
        myText.setTypeface(myFont);
        B1.setTypeface(myFont);
        textNick.setTypeface(myFont);
        textScore.setTypeface(myFont);

        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comeBack();
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();

                for(DataSnapshot userSnapshot: dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    userList.add(user);
                }

                LeaderboardList adapter = new LeaderboardList(Leaderboard.this,userList);

                myListView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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









