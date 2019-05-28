package com.example.spaceinvaders;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    TextView myView;
    Typeface myFont;
    TextView B1;

    Button registerButton;
    EditText editTextEmail;
    EditText editTextPassword;
    TextView textViewSignIn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);

        // Initialize the firebase auth
        mAuth = FirebaseAuth.getInstance();

        // Set buttons&text
        registerButton = (Button) findViewById(R.id.buttonRegister);
        B1 = (TextView) findViewById(R.id.buttonRegister);
        myView = (TextView) findViewById(R.id.SignUp);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignIn = (TextView)findViewById(R.id.textViewSignIn);

        // Set fonts
        myFont = Typeface.createFromAsset(this.getAssets(), "Fonts/ca.ttf");
        myView.setTypeface(myFont);
        textViewSignIn.setPaintFlags(textViewSignIn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textViewSignIn.setTextColor(Color.parseColor("#FF6199C7"));

        // Actions
        registerButton.setOnClickListener(this);
        textViewSignIn.setOnClickListener(this);
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(SignUp.this, "Registered Successfully",Toast.LENGTH_SHORT).show();
                    //go to game panel
                    finish();
                    startActivity(new Intent(getApplicationContext(), Play.class));
                }
                else{
                    Toast.makeText(SignUp.this, "This email is already registered!",Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        if(v == registerButton)
        {
            registerUser();
        }

        if(v == textViewSignIn)
        {
            finish();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

}
