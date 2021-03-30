package com.example.onlinemoneypay;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignUpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignUpFragment extends Fragment {
    //variables...
    private static final String TAG = "SignUpFragment";
    private TextView alreadyhaveaccount, skipSignUp;
    private FrameLayout parentframeLayout;
    private EditText editTextemail;
    private EditText editTextfullname;
    private EditText editTextpassword;
    private EditText editTextconfirmPassword;
    private Button signUpBtn;
    private ProgressBar progressBar;


    //Firebase Variables...
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String mFireabaseUserID;

    // String Vars..
    private static String email = "";
    private static String fullname = "";
    private static String password = "";
    private static String confirmPassword = "";


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SignUpFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SignUpFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SignUpFragment newInstance(String param1, String param2) {
        SignUpFragment fragment = new SignUpFragment();
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
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        alreadyhaveaccount = view.findViewById(R.id.tv_already_have_an_account);
        parentframeLayout = getActivity().findViewById(R.id.register_framelayout);
        editTextemail = view.findViewById(R.id.sign_up_email);
        editTextfullname = view.findViewById(R.id.sign_up_fullname);
        editTextpassword = view.findViewById(R.id.sign_up_password);
        editTextconfirmPassword = view.findViewById(R.id.sign_up_confirm_password);
        signUpBtn = view.findViewById(R.id.sign_up_btn);
        progressBar = view.findViewById(R.id.sign_up_progressbar);
        skipSignUp = view.findViewById(R.id.signup_skip_btn);


        // firebase stuff..
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alreadyhaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
        editTextemail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //checkInputs();
                //    DrawableRes error_icon= (DrawableRes) getResources().getDrawable(R.drawable.error_icon);
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
        editTextfullname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //checkInputs();

                fullname = editTextfullname.getText().toString().trim();
                if (fullname.length() < 3) {
                    editTextfullname.setError("Name should be greater than 3 character");
                    editTextfullname.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                fullname = editTextfullname.getText().toString().trim();
                if (fullname.isEmpty()) {
                    editTextfullname.setError("Name required!");
                    editTextfullname.setFocusable(true);
                }
            }
        });
        editTextpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // checkInputs();

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
        editTextconfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // checkInputs();
                confirmPassword = editTextconfirmPassword.getText().toString().trim();
                password = editTextpassword.getText().toString().trim();
                if (!password.equals(confirmPassword)) {
                    editTextconfirmPassword.setError("Password doesn't matched!");
                    editTextconfirmPassword.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                confirmPassword = editTextconfirmPassword.getText().toString().trim();
                if (confirmPassword.isEmpty()) {
                    editTextconfirmPassword.setError("Confirm Password required!");
                    editTextconfirmPassword.setFocusable(true);
                }

            }
        });
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  progressBar.setVisibility(View.VISIBLE);
                checkInputs();
            }
        });
        skipSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   progressBar.setVisibility(View.VISIBLE);
                mainIntent();
            }
        });
    }

    private void checkInputs() {
        email = editTextemail.getText().toString().trim();
        fullname = editTextfullname.getText().toString().trim();
        password = editTextpassword.getText().toString().trim();
        confirmPassword = editTextconfirmPassword.getText().toString().trim();
        if (email.isEmpty()) {

            // case 1: If password field is empty..
            editTextemail.setError("Email Required!");
            editTextemail.setFocusable(true);


        } else if (!email.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            // case 2:  If email pattern doesn't matched..
            editTextemail.setError("Invalid Email!");
            editTextemail.setFocusable(true);

        } else if (fullname.length() < 3) {

            // case 3: If name is less than 3 character.
            editTextfullname.setError("Name should be greater than 3 character");
            editTextfullname.setFocusable(true);

        } else if (fullname.isEmpty()) {

            // case 4: If name is empty
            editTextfullname.setError("Name required!");
            editTextfullname.setFocusable(true);
        } else if (password.length() < 6) {

            // case 5: If password length is less than 6 character.
            editTextpassword.setError("Password must be more than 6 character");
            editTextpassword.setFocusable(true);

        } else if (password.isEmpty()) {

            // case 6: If password is Empty.
            editTextpassword.setError("Password Required!");
            editTextpassword.setFocusable(true);

        } else if (!password.equals(confirmPassword)) {

            // case 7: If password does not match to confirm password.
            editTextconfirmPassword.setError("Password doesn't matched!");
            editTextconfirmPassword.setFocusable(true);

        } else if (confirmPassword.isEmpty()) {

            // case 8: If confirmPassword is empty.
            editTextconfirmPassword.setError("Confirm Password required!");
            editTextconfirmPassword.setFocusable(true);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Toast.makeText(getActivity(), "Everythings is matches..", Toast.LENGTH_SHORT).show();
            signUpToDatabase(email, password, fullname);
        }

    }

    private void signUpToDatabase(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mFireabaseUserID = FirebaseAuth.getInstance().getUid();
                    Log.d(TAG, "onComplete: " + mFireabaseUserID);
                    storeData(name, email, mFireabaseUserID);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    String error = task.getException().getMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void storeData(String name, String email, String userId) {

        final Map<String, Object> userdata = new HashMap<>();
        userdata.put("name", name);
        userdata.put("email", email);
        userdata.put("profile", "");

        firebaseFirestore.collection("USERS").document(userId)
                .set(userdata)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(userId).collection("USER_DATA");

                            Map<String, Object> wishlistMap = new HashMap<>();
                            wishlistMap.put("list_size", (long) 0);

                            Map<String, Object> ratingsMap = new HashMap<>();
                            ratingsMap.put("list_size", (long) 0);

                            Map<String, Object> cartMap = new HashMap<>();
                            cartMap.put("list_size", (long) 0);

                            Map<String, Object> myAddressesMap = new HashMap<>();
                            myAddressesMap.put("list_size", (long) 0);

                            Map<String, Object> notificationMap = new HashMap<>();
                            notificationMap.put("list_size", (long) 0);

                            List<String> documentNames = new ArrayList<>();
                            documentNames.add("MY_WISHLIST");
                            documentNames.add("MY_RATINGS");
                            documentNames.add("MY_CART");
                            documentNames.add("MY_ADDRESSES");
                            documentNames.add("MY_NOTIFICATIONS");

                            final List<Map<String, Object>> documentFields = new ArrayList<>();
                            documentFields.add(wishlistMap);
                            documentFields.add(ratingsMap);
                            documentFields.add(cartMap);
                            documentFields.add(myAddressesMap);
                            documentFields.add(notificationMap);

                            int i = 0;
                            while (i < 5) {
                                userDataReference.document(documentNames.get(i))
                                        .set(documentFields.get(i));

                                i++;
                            }
                            if(i==5){
                                mainIntent();
                                Toast.makeText(getActivity(), "SignUp Successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            String error = task.getException().getMessage();
                            Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void mainIntent() {
        if (firebaseAuth.getCurrentUser() != null) {
            getActivity().finish();
            progressBar.setVisibility(View.INVISIBLE);

        }
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);

        fragmentTransaction.addToBackStack(getString(R.string.navigating_back));
        fragmentTransaction.commit();

    }
}