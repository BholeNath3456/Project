package com.example.onlinemoneypay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

public class RegisterActivity extends AppCompatActivity {


    private static final String TAG = "RegisterActivity";
    public static boolean setSignUpFragment=false;

    FrameLayout frameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        frameLayout=findViewById(R.id.register_framelayout);
        if(setSignUpFragment){
            setSignUpFragment=false;
            setFragment(new SignUpFragment());
        }else {
            setFragment(new SignInFragment());
        }

    }

    private void setFragment(Fragment fragment) {
        Log.d(TAG, "setFragment: SettingUp fragment ");
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(frameLayout.getId(), fragment);
        fragmentTransaction.commit();

    }
}