package com.example.onlinemoneypay;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignInFragment extends Fragment {

    //variables...
    private TextView noaccountyet,skipSignIn,tvforgotpassword;
    private FrameLayout parentframeLayout;
    private EditText editTextemail;
    private EditText editTextpassword;
    private Button signinBtn;
    private ProgressBar progressBar;

    // firebase variables..
    private FirebaseAuth firebaseAuth;
   // private FirebaseUser currentUser;

    // String Vars..
    private static String email = "";
    private static String password = "";

   ////////////////////// public static boolean disableCloseBtn=false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignInFragment newInstance(String param1, String param2) {
        SignInFragment fragment = new SignInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        noaccountyet = view.findViewById(R.id.noaccountyet);
        parentframeLayout = getActivity().findViewById(R.id.register_framelayout);
        editTextemail = view.findViewById(R.id.sign_in_email);
        editTextpassword = view.findViewById(R.id.sign_in_password);
        signinBtn = view.findViewById(R.id.sign_in_btn);
        progressBar=view.findViewById(R.id.sign_in_progressbar);
        skipSignIn=view.findViewById(R.id.signin_skip_btn);
        tvforgotpassword=view.findViewById(R.id.sign_in_forgot_password);
        // Firebase ..
        firebaseAuth = FirebaseAuth.getInstance();


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvforgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ResetPasswordFragment());
            }
        });
        noaccountyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });
        editTextemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                email = editTextemail.getText().toString().trim();
                if (!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    editTextemail.setError("Invalid Email!");
                    editTextemail.setFocusable(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                email = editTextemail.getText().toString().trim();
                if (email.isEmpty()) {
                    editTextemail.setError("Email Required!");
                    editTextemail.setFocusable(true);

                }
            }
        });
        editTextpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                password = editTextpassword.getText().toString().trim();
                if (password.length() < 6) {
                    editTextpassword.setError("Password must be more than 6 character");
                    editTextpassword.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                password = editTextpassword.getText().toString().trim();
                if (password.isEmpty()) {
                    editTextpassword.setError("Password Required!");
                    editTextpassword.setFocusable(true);

                }

            }
        });
        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.VISIBLE);
                checkInputs();
            }
        });
        skipSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // progressBar.setVisibility(View.VISIBLE);
                mainIntent();
            }
        });
    }

    private void checkInputs() {
        email = editTextemail.getText().toString().trim();
        password = editTextpassword.getText().toString().trim();
        if (email.isEmpty()) {

            // case 1: If password field is empty..
            editTextemail.setError("Email Required!");
            editTextemail.setFocusable(true);


        } else if (!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            // case 2:  If email pattern doesn't matched..
            editTextemail.setError("Invalid Email!");
            editTextemail.setFocusable(true);

        }else if (password.length() < 6) {

            // case 3: If password length is less than 6 character.
            editTextpassword.setError("Password must be more than 6 character");
            editTextpassword.setFocusable(true);

        } else if (password.isEmpty()) {

            // case 4: If password is Empty.
            editTextpassword.setError("Password Required!");
            editTextpassword.setFocusable(true);

        }else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Everythings is matches..", Toast.LENGTH_SHORT).show();
               signInToDatabase(email ,password);
        }
    }

    private void signInToDatabase(String email, String password) {
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                         mainIntent();
                     }else {
                         String error=task.getException().getMessage();
                         Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                         progressBar.setVisibility(View.INVISIBLE);
                     }
                    }
                });
    }

    private void mainIntent() {
        if(firebaseAuth.getCurrentUser()!=null){
            getActivity().finish();
            progressBar.setVisibility(View.INVISIBLE);

        }
        startActivity(new Intent(getActivity(),MainActivity.class));
        getActivity().finish();
        progressBar.setVisibility(View.INVISIBLE);

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_right, R.anim.slideout_from_left);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(getString(R.string.navigating_back));
        fragmentTransaction.commit();

    }

}