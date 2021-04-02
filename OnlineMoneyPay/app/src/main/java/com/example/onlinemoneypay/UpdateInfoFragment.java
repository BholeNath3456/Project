package com.example.onlinemoneypay;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateInfoFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private CircleImageView circleImageView;
    private Button removeBtn, updateBtn;
    private EditText nameField, emailField;
    private String inputEmail = "";
    private String inputfullname = "";
    private Button getChangePhotoBtn;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateInfoFragment newInstance(String param1, String param2) {
        UpdateInfoFragment fragment = new UpdateInfoFragment();
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
        View view = inflater.inflate(R.layout.fragment_update_info, container, false);
        circleImageView = view.findViewById(R.id.profile_image);
//        changePhotoBtn = view.findViewById(R.id.change_photo_btn);
        getChangePhotoBtn = view.findViewById(R.id.change_photo_btn);
        removeBtn = view.findViewById(R.id.remove_photo_btn);
        nameField = view.findViewById(R.id.update_name);
        emailField = view.findViewById(R.id.update_email);
        updateBtn = view.findViewById(R.id.update_password_btn);
        String name = getArguments().getString("Name");
        String email = getArguments().getString("Email");
        String photo = getArguments().getString("Photo");
        Glide.with(getContext()).load(photo).into(circleImageView);
        nameField.setText(name);
        emailField.setText(email);
        getChangePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (getContext().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                        galleryIntent.setType("image/*");
                        startActivityForResult(galleryIntent, 1);
                    } else {
                        getActivity().requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
                    }
                } else {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, 1);
                }
                Toast.makeText(getContext(), "You clicked Me!", Toast.LENGTH_SHORT).show();
            }
        });



        removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(R.drawable.profile_placeholder).into(circleImageView);
            }
        });


        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //checkInputs();

                inputfullname = nameField.getText().toString().trim();
                if (inputfullname.length() < 3) {
                    nameField.setError("Name should be greater than 3 character");
                    nameField.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                inputfullname = nameField.getText().toString().trim();
                if (inputfullname.isEmpty()) {
                    nameField.setError("Name required!");
                    nameField.setFocusable(true);
                }
            }
        });
        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //checkInputs();
                //    DrawableRes error_icon= (DrawableRes) getResources().getDrawable(R.drawable.error_icon);
                inputEmail = emailField.getText().toString().trim();
                if (!inputEmail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    emailField.setError("Invalid Email!");
                    emailField.setFocusable(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                inputEmail = emailField.getText().toString().trim();
                if (inputEmail.isEmpty()) {
                    emailField.setError("Email Required!");
                    emailField.setFocusable(true);

                }
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputs();
            }
        });

        return view;
    }

    private void checkInputs() {
        inputEmail = emailField.getText().toString().trim();
        inputfullname = nameField.getText().toString().trim();
        if (inputEmail.isEmpty()) {

            // case 1: If password field is empty..
            emailField.setError("Email Required!");
            emailField.setFocusable(true);


        } else if (!inputEmail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
            // case 2:  If email pattern doesn't matched..
            emailField.setError("Invalid Email!");
            emailField.setFocusable(true);

        } else if (inputfullname.length() < 3) {

            // case 3: If name is less than 3 character.
            nameField.setError("Name should be greater than 3 character");
            nameField.setFocusable(true);

        } else if (inputfullname.isEmpty()) {

            // case 4: If name is empty
            nameField.setError("Name required!");
            nameField.setFocusable(true);
        } else {
            Toast.makeText(getContext(), "Everything is matches!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                if (data != null) {
                    Uri uri = data.getData();
                    Glide.with(getContext()).load(uri).into(circleImageView);
                } else {
                    Toast.makeText(getContext(), "Image not found!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 1);
            } else {
                Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



