package com.example.onlinemoneypay;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private static int SPLASH_TIMEOUT = 5000;
    // Animations
    Animation topAnimation, bottomAnimation, middleAnimation;
    //Hooks
    View first, second, third, fourth, fifth, sixth, seventh;
    TextView a, slogan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //Firebase Stuff..
        firebaseAuth = FirebaseAuth.getInstance();

        //Animation Stuff...
        topAnimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        middleAnimation = AnimationUtils.loadAnimation(this, R.anim.middle_animation);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        // Hooks
        first = findViewById(R.id.firstline);
        second = findViewById(R.id.secondline);
        third = findViewById(R.id.thirdline);
        fourth = findViewById(R.id.fourthline);
        fifth = findViewById(R.id.fifthline);
        sixth = findViewById(R.id.sixthline);
        seventh = findViewById(R.id.seventhline);


        a = findViewById(R.id.a);
        slogan = findViewById(R.id.tag_line);

        first.setAnimation(topAnimation);
        second.setAnimation(topAnimation);
        third.setAnimation(topAnimation);
        fourth.setAnimation(topAnimation);
        fifth.setAnimation(topAnimation);
        sixth.setAnimation(topAnimation);
        seventh.setAnimation(topAnimation);
        a.setAnimation(middleAnimation);
        slogan.setAnimation(bottomAnimation);

// Splash

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onStartCheckCurrentUser();
            }
        }, SPLASH_TIMEOUT);
    }


    protected void onStartCheckCurrentUser() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            FirebaseFirestore.getInstance().collection("USERS").document(currentUser.getUid()).update("Last_seen", FieldValue.serverTimestamp()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                  if(task.isSuccessful()){

                      Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                      startActivity(intent);
                      finish();
                  }else {
                      String error=task.getException().getMessage();
                      Toast.makeText(SplashActivity.this, error, Toast.LENGTH_SHORT).show();
                  }
                }
            });

        } else {
            Intent intent = new Intent(SplashActivity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

    }
}