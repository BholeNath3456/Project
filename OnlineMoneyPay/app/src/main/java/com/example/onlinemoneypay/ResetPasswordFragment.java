package com.example.onlinemoneypay;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ResetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ResetPasswordFragment extends Fragment {

    // variables
    private EditText editTextregisteremail;
    private Button senklinkbtn;
    private TextView textViewgoback, checkyouremail, mailsenttext;
    private LinearLayout linearLayout;
    private FrameLayout parentframeLayout;
    private ProgressBar progressBar;
    private ViewGroup emailIconContainer;
    private ImageView mailsentimageView;
    // Fireabase Variables;
    private FirebaseAuth firebaseAuth;
    // String variables..
    private static String registeredemail;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ResetPasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ResetPasswordFragment newInstance(String param1, String param2) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        parentframeLayout = getActivity().findViewById(R.id.register_framelayout);
        editTextregisteremail = view.findViewById(R.id.reset_password_email);
        senklinkbtn = view.findViewById(R.id.reset_password_sendlink_btn);
        progressBar = view.findViewById(R.id.progressBar);
        checkyouremail = view.findViewById(R.id.reset_password_check_textview);
        textViewgoback = view.findViewById(R.id.reset_password_goback_textview);
        linearLayout = view.findViewById(R.id.reset_password_linearlayout);
        mailsentimageView = view.findViewById(R.id.mailsentimage);
        mailsenttext = view.findViewById(R.id.mailsentText);
        // Firebase Stuff..
        firebaseAuth = FirebaseAuth.getInstance();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editTextregisteremail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                registeredemail = editTextregisteremail.getText().toString().trim();
                if (!registeredemail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    editTextregisteremail.setError("Invalid Email!");
                    editTextregisteremail.setFocusable(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (registeredemail.isEmpty()) {
                    editTextregisteremail.setError("Required Email!");
                    editTextregisteremail.setFocusable(true);

                }
            }
        });
        senklinkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });
        textViewgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });
    }

    private void checkInputs() {
        registeredemail = editTextregisteremail.getText().toString().trim();

        if (registeredemail.isEmpty()) {

            editTextregisteremail.setError("Required Email!");
            editTextregisteremail.setFocusable(true);

        } else if (!registeredemail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            editTextregisteremail.setError("Invalid Email!");
            editTextregisteremail.setFocusable(true);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            sendLinkFromFirebase(registeredemail);
        }
    }

    private void sendLinkFromFirebase(String registeredemail) {
        checkyouremail.setVisibility(View.GONE);
        firebaseAuth.sendPasswordResetEmail(registeredemail).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    mailsentimageView.setVisibility(View.VISIBLE);
                    mailsenttext.setVisibility(View.VISIBLE);


                } else {
                    String error = task.getException().getMessage();
                    mailsentimageView.setImageDrawable(getActivity().getDrawable(R.drawable.mailunsend));
                    mailsenttext.setText(error);

                }
            }
        });

    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slidein_from_left, R.anim.slideout_from_right);
        fragmentTransaction.replace(parentframeLayout.getId(), fragment);
        fragmentTransaction.addToBackStack(getString(R.string.navigating_back));
        fragmentTransaction.commit();

    }
}