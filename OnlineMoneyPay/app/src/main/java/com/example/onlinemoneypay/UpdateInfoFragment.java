package com.example.onlinemoneypay;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
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
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateInfoFragment extends Fragment {
    private static final String TAG = "UpdateInfoFragment";
    private Button selectPhoto, updatePhoto, updateName, updateEmail;
    private EditText editTextinputName, editTextinputEmail;
    private CircleImageView userPhoto;
    private FirebaseUser user;
    private Uri uri = null;
    private boolean isUpdatePhoto = false;
    private StorageReference mStorageReference;
    private String imgUrl = null;
    StorageReference fileReference;
    private Dialog passwordDialog;
    private EditText editTextpassword;
    private Button doneBtn;
    private String previousEmail;
    private String previousName;
    private String previousPhoto;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
        userPhoto = view.findViewById(R.id.profile_image);
        selectPhoto = view.findViewById(R.id.select_photo);
        updatePhoto = view.findViewById(R.id.update_photo);
        updateName = view.findViewById(R.id.name_update_btn);
        updateEmail = view.findViewById(R.id.email_update_btn);
        editTextinputName = view.findViewById(R.id.user_updated_name);
        editTextinputEmail = view.findViewById(R.id.user_updated_email);
        user = FirebaseAuth.getInstance().getCurrentUser();
        mStorageReference = FirebaseStorage.getInstance().getReference("UserProfile");

        previousName = getArguments().getString("Name");
        previousEmail = getArguments().getString("Email");
        previousPhoto = getArguments().getString("Photo");

        Glide.with(getContext()).load(previousPhoto).into(userPhoto);
        editTextinputEmail.setText(previousEmail);
        editTextinputName.setText(previousName);
        //////////password dialog

        passwordDialog = new Dialog(getContext());
        passwordDialog.setContentView(R.layout.password_confirmation_dialog);
        passwordDialog.setCancelable(true);
        passwordDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.slider_background));
        passwordDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editTextpassword = passwordDialog.findViewById(R.id.password);
        doneBtn = passwordDialog.findViewById(R.id.done_btn);
        //////////password dialog

        selectPhoto.setOnClickListener(new View.OnClickListener() {
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
                Toast.makeText(getContext(), "Choose your photo!", Toast.LENGTH_SHORT).show();
            }
        });
        updatePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhotoInFirebase();
            }
        });
        String inputName = editTextinputName.getText().toString().trim();
        String inputEmail = editTextinputEmail.getText().toString().trim();

        editTextinputName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //checkInputs();

                String inputName = editTextinputName.getText().toString().trim();
                if (inputName.length() < 3) {
                    editTextinputName.setError("Name should be greater than 3 character");
                    editTextinputName.setFocusable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                String inputName = editTextinputName.getText().toString().trim();
                if (inputName.isEmpty()) {
                    editTextinputName.setError("Name required!");
                    editTextinputName.setFocusable(true);
                }
            }
        });
        editTextinputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String inputEmail = editTextinputEmail.getText().toString().trim();
                if (!inputEmail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    editTextinputEmail.setError("Invalid Email!");
                    editTextinputEmail.setFocusable(true);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputEmail = editTextinputEmail.getText().toString().trim();

                if (inputEmail.isEmpty()) {
                    editTextinputEmail.setError("Email Required!");
                    editTextinputEmail.setFocusable(true);

                }
            }
        });

        updateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = editTextinputName.getText().toString().trim();
                if (inputName.length() < 3) {
                    editTextinputName.setError("Name should be greater than 3 character");
                    editTextinputName.setFocusable(true);
                } else if (inputName.isEmpty()) {
                    editTextinputName.setError("Name required!");
                    editTextinputName.setFocusable(true);
                } else if (inputName.equals(previousName)) {
                    Toast.makeText(getContext(), "You do not change your name!", Toast.LENGTH_SHORT).show();
                } else {
                    updateNameInFirebase(inputName);
                }

            }
        });
        updateEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputEmail = editTextinputEmail.getText().toString().trim();
                if (!inputEmail.matches(String.valueOf(Patterns.EMAIL_ADDRESS))) {
                    editTextinputEmail.setError("Invalid Email!");
                    editTextinputEmail.setFocusable(true);
                } else if (inputEmail.isEmpty()) {
                    editTextinputEmail.setError("Email Required!");
                    editTextinputEmail.setFocusable(true);
                } else if (inputEmail.equals(previousEmail)) {
                    Toast.makeText(getContext(), "You do not change your Email!", Toast.LENGTH_SHORT).show();
                } else {
                    updateEmailInFirebase();
                }

            }
        });


        return view;
    }

    private void updateEmailInFirebase() {
        Toast.makeText(getContext(), "You are changing your Email.", Toast.LENGTH_SHORT).show();
    }

    private void updateNameInFirebase(String name) {
        Toast.makeText(getContext(), "You are changing your Name.", Toast.LENGTH_SHORT).show();
        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                .update("name", name).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getContext(), "Name updated successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadPhotoInFirebase() {
        if (uri != null) {
            fileReference = mStorageReference.child(user.getUid() + "" + getFileExtension(uri));
            fileReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Log.d(TAG, "onSuccess: " + taskSnapshot.getMetadata());
                            isUpdatePhoto = true;
                            fileReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        imgUrl = task.getResult().toString();
                                        FirebaseFirestore.getInstance().collection("USERS").document(user.getUid())
                                                .update("profile", imgUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getContext(), "Image uploaded Successfully.", Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "onComplete:Image url is : " + imgUrl);
                                                } else {
                                                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    }
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No File Selected.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
                if (data != null) {
                    uri = data.getData();

                    Log.d(TAG, "onActivityResult: " + uri);
                    Glide.with(getContext()).load(uri).into(userPhoto);

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