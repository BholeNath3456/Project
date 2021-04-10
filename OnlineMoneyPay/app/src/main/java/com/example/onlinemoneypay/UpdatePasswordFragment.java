package com.example.onlinemoneypay;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePasswordFragment extends Fragment {
    private static final String TAG = "UpdatePasswordFragment";
    private String previousEmail;
    private String previousName;
    private String previousPhoto;
    private Dialog loadingDialog;
    private ImageView loadingAnim;
    private EditText editTextOldPassword;
    private EditText editTextNewPassword;
    private EditText editTextConfirmNewPassword;
    private Button updatePasswordBtn;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdatePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdatePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePasswordFragment newInstance(String param1, String param2) {
        UpdatePasswordFragment fragment = new UpdatePasswordFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_password, container, false);
        previousName = getArguments().getString("Name");
        previousEmail = getArguments().getString("Email");
        previousPhoto = getArguments().getString("Photo");


        //////////loading dialog
        loadingDialog = new Dialog(getContext());
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getContext().getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingAnim = loadingDialog.findViewById(R.id.loading_anim);
        Glide.with(loadingDialog.getContext()).load(R.drawable.loading).into(loadingAnim);
        //////////loading dialog

        updatePasswordBtn = view.findViewById(R.id.update_password_btn);
        editTextOldPassword = view.findViewById(R.id.old_password);
        editTextNewPassword = view.findViewById(R.id.new_password);
        editTextConfirmNewPassword = view.findViewById(R.id.confirm_new_password);

        editTextNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // checkInputs();

                String password = editTextNewPassword.getText().toString().trim();
                if (password.length() < 6) {
                    editTextNewPassword.setError("Password must be more than 6 character");
                    editTextNewPassword.setFocusable(true);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

                String password = editTextNewPassword.getText().toString().trim();
                if (password.isEmpty()) {
                    editTextNewPassword.setError("Password Required!");
                    editTextNewPassword.setFocusable(true);

                }
            }
        });
        editTextConfirmNewPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // checkInputs();
                String confirmPassword = editTextConfirmNewPassword.getText().toString().trim();
                String password = editTextNewPassword.getText().toString().trim();
                if (!password.equals(confirmPassword)) {
                    editTextConfirmNewPassword.setError("Password doesn't matched!");
                    editTextConfirmNewPassword.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String confirmPassword = editTextConfirmNewPassword.getText().toString().trim();
                if (confirmPassword.isEmpty()) {
                    editTextConfirmNewPassword.setError("Confirm Password required!");
                    editTextConfirmNewPassword.setFocusable(true);
                }

            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextNewPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmNewPassword.getText().toString().trim();
                String oldPassword= editTextOldPassword.getText().toString();
                if(oldPassword.isEmpty()){
                    editTextOldPassword.setError("Password Required!");
                    editTextOldPassword.setFocusable(true);
                }else if (password.length() < 6) {
                    editTextNewPassword.setError("Password must be more than 6 character");
                    editTextNewPassword.setFocusable(true);
                } else if (password.isEmpty()) {
                    editTextNewPassword.setError("Password Required!");
                    editTextNewPassword.setFocusable(true);

                } else if (!password.equals(confirmPassword)) {
                    editTextConfirmNewPassword.setError("Password doesn't matched!");
                    editTextConfirmNewPassword.setFocusable(true);
                }else   if (confirmPassword.isEmpty()) {
                    editTextConfirmNewPassword.setError("Confirm Password required!");
                    editTextConfirmNewPassword.setFocusable(true);
                }else {
                    updatePasswordInFirebase();
                    loadingDialog.show();
                }
            }
        });

        return view;
    }

    private void updatePasswordInFirebase() {

        String oldPassword=editTextOldPassword.getText().toString();
        String newPassword=editTextNewPassword.getText().toString();
        Log.d(TAG, "updatePasswordInFirebase: Your old password is "+oldPassword);
        Toast.makeText(getContext(), "You are changing your password!", Toast.LENGTH_SHORT).show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
         if(oldPassword.isEmpty()){
             editTextOldPassword.setError("Password Required!");
             editTextOldPassword.setFocusable(true);
         }else {
             AuthCredential credential = EmailAuthProvider.getCredential(previousEmail, oldPassword);
             user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if(task.isSuccessful()){
                         user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                             @Override
                             public void onComplete(@NonNull Task<Void> task) {
                                 if(task.isSuccessful()){
                                     loadingDialog.dismiss();
                                     Intent registerIntent=new Intent(getContext(),RegisterActivity.class);
                                     getActivity().startActivity(registerIntent);
                                     getActivity().finish();
                                     Toast.makeText(getContext(), "Your password is updated!", Toast.LENGTH_SHORT).show();
                                 }else {
                                     loadingDialog.dismiss();
                                     Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                 }
                             }
                         });
                     }else {
                         loadingDialog.dismiss();
                         Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                     }
                 }
             });

         }

    }
}
